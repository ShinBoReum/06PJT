package spring.web.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import spring.domain.User;
import spring.service.user.UserService;

@Controller
public class UserController {
	/*003에서 워크플로워 컨트롤 파트를 지움 interceptor가 해주니까*/
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	
	public UserController() {
		System.out.println("==>UserController default Constructor call.....");
	}
	
	@RequestMapping("/logon.do")
	public ModelAndView logon() {
		
		System.out.println("\n::==>logon()start...");
		
		String message="[logon()]아이디 패스워드 3자이상 입력하세요";
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/user/logon.jsp");
		modelAndView.addObject("message",message);
		
		System.out.println("[logon() end......]");
		
		return modelAndView;
	}
	
	@RequestMapping("/home.do")
	public ModelAndView home(HttpSession session) {
		System.out.println("\n::==>home()strart...");
		
		String message="[home() WELCOME]";
	

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/user/home.jsp");
		modelAndView.addObject("message",message);
		
		System.out.println("[home() end...]\n");
		
		return modelAndView;
	}
	
	@RequestMapping(value="/logonAction.do",method=RequestMethod.GET)
	public ModelAndView logonAction() {
		System.out.println("::==>logonAction get method()start...");

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/logon.do");
		
		System.out.println("::==>logonAction get method()end...\n");
		
		return modelAndView;
	}
	
	@RequestMapping(value="/logonAction.do",method=RequestMethod.POST)
	public ModelAndView logonAction(@ModelAttribute("user") User user,
													HttpSession session) throws Exception {
		System.out.println("::==>logonAction post method()start...");

		/*if(session.isNew() || session.getAttribute("sessionUser") == null) {
			session.setAttribute("sessionUser", new User());
		}
		User sessionUser = (User)session.getAttribute("sessionUser");*/
		
		String viewName="/user/logon.jsp";
		
		User returnUser = userService.getUser(user.getUserId());
		if(returnUser.getPassword().equals(user.getPassword())){
			returnUser.setActive(true);
			user=returnUser;
		}

		if(user.isActive()) {
			viewName = "/user/home.jsp";
			session.setAttribute("sessionUser", user);
		}
		System.out.println("[action:"+viewName+"]");
		
		String message=null;
		if(viewName.equals("/user/home.jsp")){
			message="[logon()]WELCOME";
		}else {
			message="[logon()]아이디 패스워드 3자이상 입력하세요";
		}


		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(viewName);
		modelAndView.addObject("message",message);

		
		System.out.println("[logon() post end...]\n");
		
		return modelAndView;
	}
	
	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpSession session) {
		System.out.println("\n::==>logout()strart...");
		
		session.removeAttribute("sessionUser");
		
		String message="[LogonAction()]아이디 패스워드 3자이상 입력하세요";
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/user/logout.jsp");
		modelAndView.addObject("message",message);
		
		System.out.println("[logout() end...]\n");
		
		return modelAndView;
	}

}
