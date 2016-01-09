package springboot.security.service.currentuser;

import springboot.security.domain.CurrentUser;

public interface CurrentUserService {

	boolean canAccessUser(CurrentUser currentUser, Long userId);
}
