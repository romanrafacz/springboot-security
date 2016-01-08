package springboot.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import springboot.security.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByEmail(String email);
}
