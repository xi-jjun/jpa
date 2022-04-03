package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * proxy class 를 비교할 때 == 을 사용하게 되면 안된다.
 * instanceof 를 사용해야 함을 보여주는 코드
 */
public class ProxyClassTypeCheck {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
//			Member member1 = em.find(Member.class, 150L);
//			Member member2 = em.find(Member.class, 151L);
//			System.out.println("m1 == m2 : " + (member1.getClass() == member2.getClass()));
			// result : m1 == m2 : true

			Member member1 = em.find(Member.class, 150L);
			Member member2 = em.getReference(Member.class, 151L);
			System.out.println("member1.class = " + member1.getClass());
			System.out.println("member2.class = " + member2.getClass());
			System.out.println("m1 == m2 : " + (member1.getClass() == member2.getClass()));
			System.out.println("m1 instanceof Member : " + (member1 instanceof Member));
			System.out.println("m2 instanceof Member : " + (member2 instanceof Member));
//			member1.class = class hellojpa.domain.Member
//			member2.class = class hellojpa.domain.Member$HibernateProxy$Q4zBKJ8c
			// result : m1 == m2 : false
//			m1 instanceof Member : true
//			m2 instanceof Member : true
			/**
			 * 따라서 어떤 class 가 올지 모르니, type 을 비교할 일이 생길 때에는 instanceof 로 비교하여라.
			 */


			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
