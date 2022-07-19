package practice.jpa.springdatajpa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Setting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String levels;

	@NotBlank
	private String tags;

	public Set<Integer> getRecommendationLevels() {
		String[] levelArr = levels.split(",");
		return Arrays.stream(levelArr)
				.map(Integer::parseInt)
				.collect(Collectors.toSet());
	}

	public Set<String> getRecommendationTags() {
		String[] tagArr = tags.split(",");
		return new HashSet<>(Arrays.asList(tagArr));
	}
}
