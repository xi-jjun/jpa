package hellojpa;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaPersistenceContextMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager entityManager = emf.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		try {
			// 비영속 상태
			Member member = new Member();
			member.setUsername("KJJ");
			member.setIdx(4L);

			// 이 때 영속 상태 된다.
			entityManager.persist(member); // 주의 : 이 때 DB 에 저장되는게 아니다!!! 증거 : query 가 안날라감

			System.out.println("=== Query Check ===");
			transaction.commit(); // query 날라간다ㅏㅏ
			System.out.println("=== Query Check ===");
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			entityManager.close();
		}

		emf.close();
	}
}
