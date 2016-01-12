package springboot.security.domain.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import springboot.security.domain.UserCreateForm;
import springboot.security.service.user.UserService;

@Component
public class UserCreateFormValidator implements Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCreateFormValidator.class);
	private final UserService userService;
	
	@Autowired
	public UserCreateFormValidator(UserService userService){
		this.userService = userService;
	}
	
	@Override
	public boolean supports(Class<?> clazz){
		return clazz.equals(UserCreateForm.class);
	}
	
	@Override
	public void validate(Object target, Errors errors){
		LOGGER.debug("Validating {}", target);
		UserCreateForm form = (UserCreateForm) target;
		validatePasswords(errors, form);
		validateEmail(errors, form);
		
	}
	
	private void validatePasswords(Errors errors, UserCreateForm form){
		if (!form.getPassword().equals(form.getPasswordRepeated())){
			errors.reject("Password.no_match", "passwords do not match");
		}
	}
	
	private void validateEmail(Errors error, UserCreateForm form){
		if (userService.getUserByEmail(form.getEmail()).isPresent()){
			error.reject("email.exists", "User with this email already exists");
		}
	}
	
}
