package hellojpa.loading;

import hellojpa.domain.Member;
import hellojpa.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class LazyLoadingCheck {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
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
			Member findMember = em.find(Member.class, 1L);
			System.out.println("findMember getTeam Class : " + findMember.getTeam().getClass());
			// findMember getTeam Class : class hellojpa.domain.Team$HibernateProxy$EQaBlgtL
			/**
			 * 우리는 line:29 으로 Member Entity 에 대한 정보는 가져왔지다. 해당 Entity 의 field 인 team 에 대한 정보는..? 어떻게 됐을까?
			 *
			 * <Member class>
			 * @ManyToOne(targetEntity = Team.class, fetch = FetchType.LAZY)
			 * @JoinColumn(name = "team_id")
			 * private Team team;
			 *
			 * Member class 를 보면, 연관관계인 team 객체 대하여 LAZY loading 을 하고 있음을 알 수 있다.
			 * 따라서 findMember.getTeam().getClass() 의 결과는 우리가 이전에 공부했던 Proxy 객체임을 알 수 있다.
			 * 왜? => team 에 대한 정보를 '지금 당장 말고 나중에' 가져오도록 했기 때문. 그 설정이 바로 LAZY
			 * 		따라서 
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
