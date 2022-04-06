package hellojpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Team {
	@Id
	@Column(name = "team_id")
	private Long idx;

	@Column
	private String teamName;

	@OneToMany(mappedBy = "team")
	private List<Member> members = new ArrayList<>();
}
