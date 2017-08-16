package projectbase.mvc;

import java.lang.annotation.Annotation;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.OrderComparator;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * add interceptor for transaction and authorization
 * 
 * @author tudoubaby
 *
 */
public class CustomRequestMappingHandlerMapping extends
		RequestMappingHandlerMapping {

	private static AnnotationParserHandlerInterceptor[] annotationParsers;

	@Override
	protected HandlerExecutionChain getHandlerExecutionChain(Object handler,
			HttpServletRequest request) {
		HandlerExecutionChain chain = super.getHandlerExecutionChain(handler,
				request);
		if (handler instanceof HandlerExecutionChain) {
			handler = ((HandlerExecutionChain) handler).getHandler();
		}
		if (handler instanceof HandlerMethod) {
			HandlerMethod m = (HandlerMethod) handler;
			OrderComparator.sort(annotationParsers);
			for (AnnotationParserHandlerInterceptor parser : annotationParsers)
				AddAnnotationParserHandlerInterceptor(m, chain, parser);
			Transaction tr = m.getMethodAnnotation(Transaction.class);
			if (tr != null)
				chain.addInterceptor(new TransactionParser(tr));
		}
		return chain;
	}

	// 添加由标记表示的interceptor，
	private void AddAnnotationParserHandlerInterceptor(
			HandlerMethod handler, HandlerExecutionChain chain, AnnotationParserHandlerInterceptor parser) {
		Class<? extends Annotation> annotationClass=parser.getAnnotaionClass();
		Annotation an = handler.getMethodAnnotation(annotationClass);
		if (an == null && parser.ShouldParseControllerDefault())
			an = handler.getMethod().getDeclaringClass()
					.getAnnotation(annotationClass);
		if (an != null) {
			parser.setAnnotaion(an);
			chain.addInterceptor(parser);
		}
	}

	public static AnnotationParserHandlerInterceptor[] getAnnotationParsers() {
		return annotationParsers;
	}

	public static void setAnnotationParsers(
			AnnotationParserHandlerInterceptor[] value) {
		annotationParsers = value;
	}
}
