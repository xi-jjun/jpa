package hellojpa.basictest;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class FirstCache {
	public static void main(String[] args) {
		// "hello" 는 persistence.xml 에서 설정한 값과 같아야 한다.
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
//			createMember(em, tx); // id=100L member 를 이 method 로 저장했었다.

			System.out.println("== Select Query ===");
			// 처음에 id=100L member 가 1차 캐시에 없으므로 DB 에 select query 날려서
			// 1차캐시에 저장한 후 find1 으로 가져온다.
			Member find1 = em.find(Member.class, 100L);
			System.out.println("== Select Query ===");

			// DB 에서 가져온 member 객체가 1차 캐시에 존재하기 때문에
			// Select query 가 안날라간다.
			Member find2 = em.find(Member.class, 100L);
			System.out.println("== Select Query ===");

			/**
			 * 그리고 JPA 는 영속 엔티티의 동일성을 보장하기 때문에
			 * 같은 PK 를 가진 객체를 조회하게 될 때, 그 둘의 == 연산은 True 가 나오게 된다.
			 * 언제? → 같은 transaction 안에서!!!!!
			 */
			System.out.println(find1 == find2);

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}

	private static void createMember(EntityManager em, EntityTransaction tx) {
		Member member = new Member();
		member.setIdx(100L);
		member.setUsername("1차 캐시에서 조회할 때 update 쿼리가 안나온다며?!");

		// save : 1차 캐시에 저장
		em.persist(member);

		// find ← 이 때는 select query 가 안날라간다.
		// 왜? 1차 캐시에 찾으려는 id=100L member 객체가 저장되어 있으니깐
		Member findMember = em.find(Member.class, 100L);
		System.out.println("findMember = " + findMember);

		// save to DB. query 날라간다ㅏㅏㅏ
		System.out.println("=== Query ===");
		tx.commit();
		System.out.println("=== Query ===");
	}
}
