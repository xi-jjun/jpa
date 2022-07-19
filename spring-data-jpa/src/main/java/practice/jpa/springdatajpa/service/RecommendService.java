package practice.jpa.springdatajpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.jpa.springdatajpa.domain.Problem;
import practice.jpa.springdatajpa.domain.Setting;
import practice.jpa.springdatajpa.domain.Tag;
import practice.jpa.springdatajpa.domain.User;
import practice.jpa.springdatajpa.repository.ProblemRepository;
import practice.jpa.springdatajpa.repository.TagRepository;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RecommendService {
	private final ProblemRepository problemRepository;
	private final TagRepository tagRepository;

	public Problem recommendProblemToUser(User user) {
		Setting userSetting = user.getSetting();
		Set<Integer> recommendationLevels = userSetting.getRecommendationLevels();
		Set<String> recommendationTags = userSetting.getRecommendationTags();

		List<Problem> problemsByLevelIn = problemRepository.findProblemsByLevelIn(recommendationLevels);
		List<Tag> distinctByTagNameIn = tagRepository.findDistinctByTagNameIn(recommendationTags);

		return null;
	}
}
