package hellojpa;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DirtyCheck {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member findMember = em.find(Member.class, 101L);


			findMember.setUsername("Updated Name");
			// persist() 같은 것도 안썼는데 그저 '바꿨다는 사실' 하나만으로 알아서 update query 가 날라간다!

			System.out.println("=== Update Query ===");
			tx.commit();
			System.out.println("=== Update Query ===");
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

//		Hibernate:
//			select
//				member0_.idx as idx1_0_0_,
//				member0_.username as username2_0_0_
//			from
//				Member member0_
//			where
//				member0_.idx=?
//		=== Update Query ===
//		Hibernate:
//    		/* update
//        		hellojpa.domain.Member */ update
//					Member
//				set
//					username=?
//				where
//					idx=?
//		=== Update Query ===

		emf.close();
	}
}
