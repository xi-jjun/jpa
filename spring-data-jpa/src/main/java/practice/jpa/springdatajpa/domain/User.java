package practice.jpa.springdatajpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "Users")
@Entity
public class User {
	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private String password;
	private String baekJoonId;
	private int refreshCount;

	@OneToOne
	@JoinColumn(name = "setting_id")
	private Setting setting;
}
