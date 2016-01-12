package springboot.security.service.currentuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import springboot.security.domain.CurrentUser;
import springboot.security.domain.Role;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServiceImpl.class);
	
	@Override
	public boolean canAccessUser(CurrentUser currentUser, Long userId){
		LOGGER.debug("Check if user={} has access to current user={}", currentUser, userId);
		return currentUser != null
				&& (currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(userId));
	}
	
	
}
