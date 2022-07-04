package practice.jpa.springdatajpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.jpa.springdatajpa.domain.User;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager em;

	@BeforeAll
	void initData() {
		final String[] NAMES = {"kjj", "hsj"};
		for (int i = 0; i < 2; i++) {
			User user = User.builder()
					.name(NAMES[i])
					.password("1" + i)
					.build();

			userRepository.save(user);
		}
	}

	@Test
	void findByName() {
		final String NAME = "kjj";
		User findUser = userRepository.findByName(NAME);
		assertThat(findUser).isNotNull();

		System.out.println("findUser.getName() = " + findUser.getName());
	}

	@Test
	void findByNameButNotExisted() {
		final String NOT_EXISTED_NAME = "pqwqwqw";
		User user = userRepository.findByName(NOT_EXISTED_NAME);
		
		System.out.println("user = " + user);
		assertThat(user).isNull();
	}
}