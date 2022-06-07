package hellojpa.lecture;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * create table lecture_user (
 *        id bigint not null,
 *         age integer,
 *         createdDate timestamp,
 *         description clob,
 *         lastModifiedDate timestamp,
 *         roleType varchar(255),
 *         name varchar(255),
 *         primary key (id)
 *     )
 * ignoredField 는 @Transient annotation 을 썼기에 DB field 로 반영이 안된 것을 볼 수 있다.
 */
@Getter
@Setter
@Table(name = "lecture_user")
@Entity
class User {
	@Id
	private Long id;

	@Column(name = "name") // DB 에는 name
	private String username; // 객체에는 username 이라는 이름으로 쓰일 수 있다.

	private Integer age; // Integer 에 적합한 type 이 설정된다.

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	/**
	 * [ annotation Transient ]
	 * Specifies that the property or field is not persistent.
	 * It is used to annotate a property or field of an entity class, mapped superclass, or embeddable class.
	 */
	@Transient
	private String ignoredField;

	/**
	 * [ annotation Lob ] : https://docs.jboss.org/hibernate/jpa/2.1/api/javax/persistence/Lob.html
	 * Specifies that a persistent property or field should be persisted
	 * as a large object to a database-supported large object type.
	 */
	@Lob
	private String description;
}
