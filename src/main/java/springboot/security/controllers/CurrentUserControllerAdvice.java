package springboot.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import springboot.security.domain.CurrentUser;

@ControllerAdvice
public class CurrentUserControllerAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserControllerAdvice.class);

	@ModelAttribute("currentUser")
	public CurrentUser getCurrentUser(Authentication auth){
		return (auth == null) ? null : (CurrentUser) auth.getPrincipal();
	}
}
