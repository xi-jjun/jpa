package hellojpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Error 발생
 * Exception in thread "main" java.lang.IllegalArgumentException: Unknown entity: hellojpa.domain.Member
 *
 * 강의에서는 maven 으로 build 를 했지만 난 gradle 로 함.
 * 따라서 pom.xml 에 <class>hellojpa.domain.Member</class> 를 추가해서 entity 를
 * 인식 할 수 있도록 해줘야 했다.
 */
@Entity // JPA 가 관리하는 class 임을 알려주는 역할
@Getter @Setter
@NoArgsConstructor // JPA 는 내부적으로 Reflection 을 쓰기 때문에 동적으로 객체를 생성해내야 한다. 따라서 기본 생성자가 하나 필요하다. https://wbluke.tistory.com/6
public class Member {
	@Id
	@Column(name = "member_id")
	private Long idx;

	@Column
	private String username;

	@ManyToOne(targetEntity = Team.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(Long idx, String username) {
		this.idx = idx;
		this.username = username;
	}
}
