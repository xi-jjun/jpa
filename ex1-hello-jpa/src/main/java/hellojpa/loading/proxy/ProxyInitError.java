package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때 Proxy 객체를 초기화하면 오류가 발생한다.
 */
public class ProxyInitError {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
			Member refMember = em.getReference(Member.class, 150L);
			System.out.println("refMember.getClass() = " + refMember.getClass());
			/**
			 * 이 때에 미리 초기화를 해두면 준영속 상태가 되더라도 오류가 안난다.
			 * 영속성 컨텍스트에 해당 값이 없어도, proxy 객체가 Member 를 상속받아 메모리에 할당되어 있기 때문이다.
			 */

			em.detach(refMember); // 준영속 상태로 만들기

			String username1 = refMember.getUsername(); // 준영속 상태가 된 후 초기화 시도. 그러나 오류 발생
			/**
			 * org.hibernate.LazyInitializationException: could not initialize proxy [hellojpa.domain.Member#150] - no Session
			 * 	at org.hibernate.proxy.AbstractLazyInitializer.initialize(AbstractLazyInitializer.java:176)
			 * 	at org.hibernate.proxy.AbstractLazyInitializer.getImplementation(AbstractLazyInitializer.java:322)
			 * 	at org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor.intercept(ByteBuddyInterceptor.java:45)
			 * 	at org.hibernate.proxy.ProxyConfiguration$InterceptorDispatcher.intercept(ProxyConfiguration.java:95)
			 */

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
