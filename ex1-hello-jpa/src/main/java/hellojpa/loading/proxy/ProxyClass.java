package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Proxy class 가 초기화 될 때 실제 Entity 로 바뀌지 않음을 보여주는 코드
 */
public class ProxyClass {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
			Member proxyMember = em.getReference(Member.class, 200L);
			System.out.println("proxyMember.getClass() = " + proxyMember.getClass());
			String username = proxyMember.getUsername(); // 이 때 Proxy 객체 초기화 발생. 해당 과정은 ProxyMain.java 에 자세히 기술.
			System.out.println("username = " + username);
			System.out.println("proxyMember.getClass() = " + proxyMember.getClass());
			// 출력 결과 Proxy 객체가 초기화 되더라도, 실제 Entity class 로 바뀌지 않음을 확인할 수 있다.
//			proxyMember.getClass() = class hellojpa.domain.Member$HibernateProxy$8701UONW
//			Hibernate:
//				select
//					member0_.idx as idx1_0_0_,
//					member0_.username as username2_0_0_
//				from
//					Member member0_
//				where
//					member0_.idx=?
//			username = Hello Proxy
//			proxyMember.getClass() = class hellojpa.domain.Member$HibernateProxy$8701UONW


			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
