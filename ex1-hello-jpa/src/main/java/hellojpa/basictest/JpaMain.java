package hellojpa.basictest;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
	public static void main(String[] args) {
		/**
		 * persistence.xml 에서
		 * <persistence-unit name="hello">
		 *     ...
		 * 의 name attribute 가 아래의 persistenceUnitName 의 param 으로 들어가는 것이다.
		 *
		 * EntityManagerFactory 는 1번만 만들어져야 한다. 그리고 어플리케이션 전체에서 공유
		 */
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		// 고객의 정보 저장과 같은 한번의 프로세스를 그 때 그 때 EntityManager 를 만들어서 처리해준다.
		// thread 간에 공유하면 안된다. 사용하고 버려야 한다.
		EntityManager entityManager = emf.createEntityManager();

		// 데이터를 변경하는 모든 작업은 tx 안에서 일어나야 한다.
		EntityTransaction transaction = entityManager.getTransaction(); // db connection 하나 얻기!

		// tx start
		transaction.begin();

		// 저장할 Entity 생성
		Member member = new Member();
		member.setIdx(1L);
		member.setUsername("JPA Basic");
//
//		// save
//		entityManager.persist(member);
//		Hibernate: <= sql 자체를 보여주는게 show_sql
//		/* insert hellojpa.domain.Member  <= pom.xml 의 user_sql_comments=true
//		 */ insert <= 이렇게 예쁘게 query 보여주는게 format_sql
//				into
//		Member
//				(username, idx)
//		values
//				(?, ?)

		// find
		Member findMember = entityManager.find(Member.class, 1L);
//		Hibernate:
//		select
//		member0_.idx as idx1_0_0_,
//				member0_.username as username2_0_0_
//		from
//		Member member0_
//		where
//		member0_.idx=?

		// remove
//		entityManager.remove(member);

		// update
		Member toBeUpdatedMember = entityManager.find(Member.class, 1L);
		toBeUpdatedMember.setUsername("Other Name");
//		Hibernate:
//    /* update
//        hellojpa.domain.Member */ update
//				Member
//		set
//		username=?
//		where
//		idx=?


		// JPQL : table 이 아닌 entity 객체를 대상으로!! 현재 Member 객체를 대상으로 JPQL query 를 작성함
		// JPQL == 객체지향 SQL
		List<Member> memberList = entityManager.createQuery("select m from Member as m", Member.class)
//				.setFirstResult(1) // paging. 1번부터
//				.setMaxResults(10) // 10번까지
				.getResultList(); // 결과를 List 로 출력해줘
//		Hibernate:
//    /* select
//        m
//    from
//        Member as m */ select
//		member0_.idx as idx1_0_,
//				member0_.username as username2_0_
//		from
//		Member member0_
		for (Member member1 : memberList) {
			System.out.println("member1 = " + member1);
		}

		transaction.commit();
		// tx end


		entityManager.close(); // 사용 다 하면 꼭 닫아줘야 한다. => 실제에서는 commit, close 등등을 spring 에서 알아서 해준다

		emf.close(); // WAS 가 내려갈 때 종료해준다.
	}
}
