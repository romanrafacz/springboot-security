package springboot.security.service.currentuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import springboot.security.domain.CurrentUser;
import springboot.security.domain.Role;

public class CurrentUserServiceImpl implements CurrentUserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserServiceImpl.class);
	
	@Override
	public boolean canAccessUser(CurrentUser currentUser, Long userid){
		LOGGER.debug("Check if user={} has access to current user={}", currentUser, userid);
		return currentUser != null
				&& (currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(userid));
	}
	
	
}
