package springboot.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import springboot.security.service.UserService;

@Controller
public class UsersController {
	
	private UserService userService;

	@Autowired
	public UsersController(UserService userService){
		this.userService = userService;
	}
	
	@RequestMapping("/users")
	public ModelAndView getUsersPage() {
		return new ModelAndView("users", "users", userService.getAllUsers());
	}
}
