package hellojpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Member {
	@Id
	private Long idx;

	private String username;
}
