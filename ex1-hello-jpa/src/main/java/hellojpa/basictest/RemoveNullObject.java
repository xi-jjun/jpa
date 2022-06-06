package hellojpa.basictest;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 요즘 공들이고 있는 community 토이 프로젝트를 하다가 의문이 들어서 테스트 진행.
 * 의문점 : EntityManager 의 remove method 의 parameter 로 null 을 넣으면 어떻게 될까?
 *
 * => IllegalArgumentException 발생.
 */
public class RemoveNullObject {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			System.out.println("====");
			em.remove(null); // 오류 발생

			tx.commit();
		} catch (Exception e) {
			System.out.println("it is not ok");
			System.out.println("===========");
			System.out.println(e.toString());
			System.out.println("===========");
			System.out.println(e.getMessage());
			System.out.println("===========");
			/**
			 * ====
			 * it is not ok
			 * ===========
			 * java.lang.IllegalArgumentException: attempt to create delete event with null entity
			 * ===========
			 * attempt to create delete event with null entity
			 * ===========
			 */
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
