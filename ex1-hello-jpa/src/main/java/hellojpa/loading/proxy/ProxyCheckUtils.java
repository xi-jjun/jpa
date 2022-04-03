package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Proxy 객체의 상태를 확인해주는 Utils 가 있다. 한번 보도록 하자
 */
public class ProxyCheckUtils {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
			Member refMember = em.getReference(Member.class, 150L);

			// Proxy instance 초기화 여부 확인하는 방법
			System.out.println("Proxy instance 초기화 여부 확인 : " + emf.getPersistenceUnitUtil().isLoaded(refMember)); // false
			refMember.getUsername(); // 초기화 진행
			System.out.println("Proxy instance 초기화 여부 확인 : " + emf.getPersistenceUnitUtil().isLoaded(refMember)); // true


			// Proxy class 확인 방법
			System.out.println("Proxy class 확인 : " + refMember.getClass());
			// Proxy class 확인 : class hellojpa.domain.Member$HibernateProxy$27JMsdEh

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace(); // 오류는 이 코드로 확인이 가능
			transaction.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
