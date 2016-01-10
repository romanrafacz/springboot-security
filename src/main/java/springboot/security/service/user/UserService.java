package springboot.security.service.user;

import java.util.Collection;
import java.util.Optional;

import springboot.security.domain.User;
import springboot.security.domain.UserCreateForm;

public interface UserService {
	
	Optional<User> getUserById(Long id);
	
	Optional<User> getUserByEmail(String email);
	
	Collection<User> getAllUsers();
	
	User create(UserCreateForm form);

}
