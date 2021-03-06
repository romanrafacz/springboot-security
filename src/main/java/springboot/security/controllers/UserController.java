package springboot.security.controllers;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import springboot.security.service.user.UserService;

@Controller
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final UserCreateFormValidator userCreateFormValidator;
	
	@Autowired
	public UserController(UserService userService, UserCreateFormValidator userCreateFormValidator){
		this.userService = userService;
		this.userCreateFormValidator = userCreateFormValidator;
	}
	
	@InitBinder("form")
	public void initBinder(WebDataBinder binder){
		binder.addValidators(userCreateFormValidator);
	}
	
	@PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
	@RequestMapping("/user/{id}")
	public ModelAndView getUserPage(@PathVariable Long id) {
		LOGGER.debug("Getting user page for user={}", id);
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
		LOGGER.debug("Processing user create form={}", form, bindingResult);
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

