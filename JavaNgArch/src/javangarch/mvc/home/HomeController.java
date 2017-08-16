package javangarch.mvc.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import projectbase.bd.BizException;
import projectbase.domain.ICommonBD;
import projectbase.domain.customcollection.IDomainList;
import projectbase.mvc.BaseController;
import projectbase.mvc.DisplayExtension;
import projectbase.mvc.Transaction;
import projectbase.mvc.WebConfigurationManager;
import projectbase.mvc.result.ActionResult;
import projectbase.utils.Util;
import javangarch.domain.bdinterface.IAdminBD;
import javangarch.domain.domainmodel.gn.Corp;
import javangarch.domain.domainmodel.gn.Dept;
import javangarch.domain.domainmodel.gn.Func;
import javangarch.domain.domainmodel.gn.User;
import javangarch.mvc.shared.AppBaseController;

public class HomeController extends BaseController {
	@Resource
	public IAdminBD AdminBD;

	@Resource
	public ICommonBD<Corp> CorpBD;
	@Resource
	public ICommonBD<Dept> DeptBD;
	@Resource
	public ICommonBD<User> UserBD;

	
	public ActionResult ShowLogin(HttpSession session) {
		if (session.getAttribute("LoginInfo") != null)
			return ClientRedirect("MainFrameLoggedIn");
		LoginAttemptViewModel m=new LoginAttemptViewModel();
		m.setCorpCode("1");
		m.setCode("test");
		m.setPassword("1");
		return ForView("Login", NoMaster,m );
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transaction
	public ActionResult Login(@Valid LoginAttemptViewModel attempt,
			HttpSession session,
			@CookieValue(required = false) Cookie loginMark,
			HttpServletResponse response) {
		if (session.getAttribute("LoginInfo") != null)
			return ClientRedirect("MainFrameLoggedIn");

		User loginUser = AdminBD.GetLoginUser(attempt.getCorpCode(),
				attempt.getCode(), attempt.getPassword());
		if (loginUser == null) {
			SetViewMessage("Login_Wrong");
			return ClientShowMessage();
		}
		if (!loginUser.getIsActive()) {
			SetViewMessage("Login_IsNotActive");
			return ClientShowMessage();
		}

	/*	if (loginUser.getLoginMark() != null) {// 检查本次登录是否为上次的登录的会话延续
			if (loginMark == null)
				return ClientShowMessage("Login_AlreadyIn");
			String lastsessionId = loginMark.getValue();
			if (!lastsessionId.equalsIgnoreCase(loginUser.getLoginMark()))
				return ClientShowMessage("Login_AlreadyIn");
		}

		loginMark = new Cookie("LoginMark", session.getId());
		loginMark.setHttpOnly(true);
		loginMark.setMaxAge(2 * 24 * 60 * 60);
		response.addCookie(loginMark);

		loginUser.setLoginMark(session.getId());*/
		LoginInfoViewModel loginInfo = new LoginInfoViewModel();
		loginInfo.setLoginUser(loginUser);
		loginInfo.setLoginCorp(loginUser.getDept().getCorp());
		String tmp = loginInfo.getLoginCorp().getName();// make proxy load the
														// real entity
		int i = 0;
		for (Func func : loginInfo.getLoginCorp().getFuncs()) {
			loginInfo.AddCorpFuncCode(func.getCode());
		}
		for (Func func : loginUser.getDept().getFuncs()) {
			loginInfo.AddDeptFuncCode(func.getCode());
		}
		for (Func func : loginUser.getFuncs()) {
			loginInfo.AddUserFuncCode(func.getCode());
		}
		session.setAttribute("LoginInfo", loginInfo);

		Util.AddLog("login=" + loginInfo.getLoginUser().getId());
		return ClientRedirect("MainFrameLoggedIn");
	}

	public ActionResult MainFrame(HttpSession session) {
		LoginInfoViewModel loginInfo = ((LoginInfoViewModel) session
				.getAttribute("LoginInfo"));
		if (loginInfo != null){
			AdminBD.RefreshUser(loginInfo.getLoginUser());
		}
		return  ForView(null, NoMaster, loginInfo);
	}

	public ActionResult Logout(HttpSession session) {
		session.invalidate();
		return ClientRedirect("ShowLogin");
	}


}
