package hellojpa.loading.proxy;

import hellojpa.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * persistence context 에 찾으려는 Entity 가 이미 존재하면,
 * em.getReference() 를 호출해도 '실제 Entity 를 반환' 한다.
 *
 * 그리고 JPA 는 어떻게든 동일성을 보장해 줌을 보여주는 코드가 있다.
 */
public class IfEntityInPersistenceContext {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction(); // db connection 하나 얻기!
		transaction.begin();

		try {
			Member member1 = em.find(Member.class, 150L); // find method 로 id=150 에 해당하는 객체 정보 가져온다.
			// 가져오는 순간 당연하게도 영속성 컨텍스트 1차 캐시에 저장된다.
			System.out.println("member1.getClass() = " + member1.getClass()); // 해당 객체는 당연히 Member class 이다.

			Member member2 = em.getReference(Member.class, 150L); // 이미 1차캐시에 member 의 정보가 있다. 따라서 그대로 가져온다.
			/**
			 * 1. 이미 1차 캐시에 존재하는데 굳이 Proxy 로 가져와서 target 에 참조를 걸어줄 필요가 없다. 원본을 그냥 반환하는게 성능 최적화 면에서도 더 좋다.
			 * 2. JPA 에서는 같은 transaction 에서 같은 PK 정보에 대해 반환된 정보는 == 연산에 대해 항상 true 임을 보장해준다.
			 */
			System.out.println("member2.getClass() = " + member2.getClass());
			// 놀랍게도 Proxy type 이 아니라 Member type 으로 실제 Entity 객체를 반환하게 된다.
//			member1.getClass() = class hellojpa.domain.Member
//			member2.getClass() = class hellojpa.domain.Member // getReference() method 로 가져왔지만 실제 객체를 반환하는 것을 볼 수 있다.

			em.clear();
			System.out.println("==============");

			Member refMember = em.getReference(Member.class, 150L);
			String username = refMember.getUsername(); // DB 에 Query 나가면서 Proxy 객체 초기화 발생. 해당 객체가 1차 캐시에 올라옴.
			System.out.println("username = " + username);

			Member findMember = em.find(Member.class, 150L);
			System.out.println("getReference Member class = " + refMember.getClass());
			System.out.println("find Member class = " + findMember.getClass());
			/**
			 * JPA 는 같은 transaction 에서 같은 PK 정보에 대한 객체는 == 비교 시에 항상 true 를 보장한다.
			 * em.find 할 때에 무조건 Proxy 가 아닌, 실제 Entity 를 반환하는 줄 알았지만 그런게 아니다.
			 * JPA 어떻게든 동일성을 지켜주기 때문에.
			 *
			 * 아래를 보면 find method 로 찾았어도, proxy 객체임을 알 수 있다.
			 * 이는 JPA 가 같은 transaction 내에서 동일성을 보장해줌을 알 수 있는 예시이다.
			 */
//			getReference Member class = class hellojpa.domain.Member$HibernateProxy$yu8zFcFU
//			find Member class = class hellojpa.domain.Member$HibernateProxy$yu8zFcFU

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
