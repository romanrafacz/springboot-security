package springboot.security.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import springboot.security.domain.UserCreateForm;
import springboot.security.domain.validator.UserCreateFormValidator;
import springboot.security.service.UserService;

@Controller
public class UserController {
	
	private static final Logger LOGGER = Logger.getLogger(UserController.class);
	private final UserService userService;
	private final UserCreateFormValidator userCreateFormValidator;

	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator){
		this.userService = userService;
		this.userCreateFormValidator = userCreateFormValidator;
	}
	
	@InitBinder("form")
	public void initBinder(WebDataBinder binder){
		binder.addValidators(userCreateFormValidator);
	}
	
	@PreAuthorize("@curentUserServiceImp.canAccessUser(principal, #id)")
	@RequestMapping("/user/{id}")
	public ModelAndView getUserPage(@PathVariable Long id) {
		//logger.debug giving an issue with id afer user
		LOGGER.debug("Getting user page for user={}");
		return new ModelAndView("user", "user", userService.getUserById(id)
				.orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id))));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/user/create", method=RequestMethod.GET)
	public ModelAndView getUserCreatePage() {
		LOGGER.debug("Getting user create form");
		return new ModelAndView("user_create", "form", new UserCreateForm());
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
		//Should be below but debug acting funky
		//LOGGER.debug("Processing user create form={}", form, bindingResult);
		LOGGER.debug("Processing user create form={}");
		if (bindingResult.hasErrors()){
			//failed validations
			return "user_create";
		}
		try {
			userService.create(form);
		} catch (DataIntegrityViolationException e) {
			//probably email exists.
			LOGGER.warn("Exception occured whtn trying to save the user, assuming email duplicate");
			bindingResult.reject("email.exists", "email arealdy exists");
			return "user_create";
		}
			return "redirect:/users";
	}
}

