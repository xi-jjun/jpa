package hellojpa.loading.eager;

import hellojpa.domain.Member;
import hellojpa.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EagerLoading {
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
			Member findMember = em.find(Member.class, 1L); // Member 에 대한 select query 발생.
			/**
			 * @ManyToOne(targetEntity = Team.class, fetch = FetchType.EAGER)
			 * @JoinColumn(name = "team_id")
			 * 	private Team team;
			 * Member 에서 연관된 객체를 Eager loading 을 하게끔 설정.
			 */
			System.out.println("=== query check 1 ===");
			Team findMemberTeam = findMember.getTeam(); // query 안날라감. 실제 값에 접근 안했기 때문
			System.out.println("=== query check 2 ===");

			// 이미 위에서 Member 에 대한 연관된 객체까지 한번에 query 를 날려서 가져왔음으로 query 발생 x
			System.out.println("findMemberTeam.getTeamName() = " + findMemberTeam.getTeamName());

			System.out.println("=== query check 3 ===");

/*			[출력 결과]
			Hibernate:
				select
					member0_.member_id as member_i1_0_0_,
					member0_.team_id as team_id3_0_0_,
					member0_.username as username2_0_0_,
					team1_.team_id as team_id1_1_1_,
					team1_.teamName as teamname2_1_1_
				from
					Member member0_
				left outer join
					Team team1_
						on member0_.team_id=team1_.team_id
				where
					member0_.member_id=?
			=== query check 1 ===
			=== query check 2 ===
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
