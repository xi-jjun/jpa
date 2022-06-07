package hellojpa.lecture;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager entityManager = emf.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction(); // db connection 하나 얻기!

		// tx start
		transaction.begin();

		// 저장할 Entity 생성
		User user = new User();
		user.setId(1L);
		user.setUsername("Kim Jae Jun");

		transaction.commit();

		entityManager.close();
		emf.close();
	}
}
