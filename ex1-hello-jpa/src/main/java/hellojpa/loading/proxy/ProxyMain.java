package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ProxyMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
			// 이미 DB 에 저장했기 때문에 중복된 PK 를 피하기 위해 주석처리를 해놓은 것입니다.
//			Member member = new Member();
//			member.setIdx(200L);
//			member.setUsername("Hello Proxy");
//
//			em.persist(member);
//
//			em.flush();
//			em.clear();
			// Persistence Context 초기화 됨.


			// Proxy 객체를 가져온다면?
			System.out.println("=== query check1 ===");
			Member member = em.getReference(Member.class, 200L);
			System.out.println("=== query check2 ===");
			member.getIdx();
			System.out.println("=== query check3 ===");
			String username = member.getUsername(); // Proxy 객체 초기화 시작.
			System.out.println("username = " + username);
			System.out.println("=== query check4 ===");
			// 출력 결과
//			=== query check1 ===
//			=== query check2 ===
//			=== query check3 ===
//			Hibernate:
//				select
//					member0_.idx as idx1_0_0_,
//					member0_.username as username2_0_0_
//				from
//					Member member0_
//				where
//					member0_.idx=?
//			username = Hello Proxy
//			=== query check4 ===
			/**
			 * 위 출력 결과를 보다시피 마지막 'getUsername()' 때 Query 가 날라감을 알 수 있다.
			 * Idx 값은 찾을 때 넣어줬기에 굳이 query 를 날릴 필요가 없었던 것.
			 * 그러나 가짜 Proxy 객체에는 username 에 해당하는 정보는 없었기에
			 * 1. 영속성 컨텍스트에 초기화를 요청한 후,
			 * 2. 영속성 컨텍스트가 DB 를 조회.
			 * 3. 실제 Member Entity 생성.
			 * 4. Proxy Member 객체의 target 에 위 3번에서 만들어진 Member Entity 참조.
			 * 이런 과정으로 getUsername() 이 실행되는 것
			 * target 에 실제 객체가 있으므로 그저 username 을 갖고 오면 될 일이다.
			 */

			transaction.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			transaction.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
