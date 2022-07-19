package practice.jpa.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.jpa.springdatajpa.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
