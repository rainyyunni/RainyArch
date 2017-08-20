package projectbase.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import projectbase.mvc.result.RichClientJsonResult;
import projectbase.sharparch.hibernate.HibernateSession;
import projectbase.sharparch.hibernate.SessionFactoryKeyHelper;

//I think we need to modify the sharparch transaction to commit when actionexecuted instead of resultexecuted
public class TransactionParser extends HandlerInterceptorAdapter{
	private Transaction transaction;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HibernateSession.CurrentFor(this.GetEffectiveFactoryKey())
				.beginTransaction();
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String effectiveFactoryKey = this.GetEffectiveFactoryKey();
		org.hibernate.Transaction currentTransaction = HibernateSession
				.CurrentFor(effectiveFactoryKey).getTransaction();

			if (currentTransaction.isActive()) {
				if (modelAndView.getView() instanceof RichClientJsonResult && ((RichClientJsonResult)modelAndView.getView()).IsErrorResult() ) {
					currentTransaction.rollback();
				}else{
					currentTransaction.commit();
				}
			}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if(ex==null)return;
		
		String effectiveFactoryKey = this.GetEffectiveFactoryKey();
		org.hibernate.Transaction currentTransaction = HibernateSession
				.CurrentFor(effectiveFactoryKey).getTransaction();

		if (currentTransaction.isActive()) {
			if (ex != null || this.ShouldRollback(request)) {
				currentTransaction.rollback();
				if (ex != null) {
					throw ex;
				}
			}
		}

	}

	public TransactionParser(Transaction transaction) {
		this.transaction = transaction;
	}

	private String GetEffectiveFactoryKey() {
		return StringUtils.isEmpty(transaction.FactoryKey()) ? SessionFactoryKeyHelper
				.GetKey() : transaction.FactoryKey();
	}

	private boolean ShouldRollback(HttpServletRequest request) {
		Exception ex=(Exception)request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
		if(ex==null) return false;
		return transaction.RollbackOnModelStateError()&& ex instanceof BindException;
	}
}
