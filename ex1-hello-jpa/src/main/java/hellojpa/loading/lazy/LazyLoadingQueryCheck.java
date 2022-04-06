package hellojpa.loading.lazy;

import hellojpa.domain.Member;
import hellojpa.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Lazy Loading 으로 연관객체를 가져오면 Proxy 객체임을 알았음.
 * 따라서 이번엔 해당 Proxy 객체의 실제값에 접근할 때 query 가 발생하는지 확인할 것.
 */
public class LazyLoadingQueryCheck {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		/**
		 * [이 코드를 실행시켰을 당시 DB table 에 저장된 데이터]
		 * Member
		 * ID | user| team_id
		 * 1L | KJJ | 1L
		 *
		 * Team
		 * ID | Team_name
		 * 1L | teamA
		 */
		try {
			Member findMember = em.find(Member.class, 1L); // Member 에 대한 select query 발생
			System.out.println("=== query check 1 ===");
			Team findMemberTeam = findMember.getTeam(); // query 안날라감. 실제 값에 접근 안했기 때문
			System.out.println("=== query check 2 ===");

			// 연관객체 Team 의 '실제 값' team name 에 접근하려고 했기에 Proxy 객체는 초기화 과정이 일어남.
			// 따라서 이 때 team 에 대한 조회 query 가 발생
			System.out.println("findMemberTeam.getTeamName() = " + findMemberTeam.getTeamName());

			System.out.println("=== query check 3 ===");


/*			[출력 결과]
			Hibernate:
				select
					member0_.member_id as member_i1_0_0_,
					member0_.team_id as team_id3_0_0_,
					member0_.username as username2_0_0_
				from
					Member member0_
				where
					member0_.member_id=?
			=== query check 1 ===
			=== query check 2 ===
			Hibernate:
				select
					team0_.team_id as team_id1_1_0_,
					team0_.teamName as teamname2_1_0_
				from
					Team team0_
				where
					team0_.team_id=?
			findMemberTeam.getTeamName() = teamA
			=== query check 3 ===
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
