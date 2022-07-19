package practice.jpa.springdatajpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Problem {
	@Id
	private Long id;

	private String title;
	private int level;

	@OneToMany(mappedBy = "problem", cascade = CascadeType.REMOVE)
	private List<ProblemTag> problemTags;
}
