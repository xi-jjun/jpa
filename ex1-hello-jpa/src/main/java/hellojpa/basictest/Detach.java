package hellojpa.basictest;

import hellojpa.domain.Member;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Detach {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = em.find(Member.class, 150L);

			member.setUsername("일단 바꿔볼려고. 아직까지 현재 이 member 객체는 영속상태임"); // 변경사항 생김. Dirty Checking 에서 감지된다.

			em.detach(member); // 준영속상태가 됨
			// em.clear(); // 통으로 다 지워버리는 것

			System.out.println("=== Update Query ===");
			tx.commit(); // update query 가 안날라가게 된다. → member 객체는 더 이상 JPA 에서 관리하는 객체가 아니기 때문이다.
			System.out.println("=== Update Query ===");
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
//	Hibernate:
//		select
//			member0_.idx as idx1_0_0_,
//			member0_.username as username2_0_0_
//		from
//			Member member0_
//		where
//			member0_.idx=?
//	=== Update Query ===
//	=== Update Query ===
}
