package practice.jpa.springdatajpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.jpa.springdatajpa.domain.Problem;
import practice.jpa.springdatajpa.domain.ProblemTag;
import practice.jpa.springdatajpa.domain.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(readOnly = true)
@SpringBootTest
class ProblemRepositoryTest {
	@Autowired
	private ProblemTagRepository problemTagRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private TagRepository tagRepository;

	@Transactional
	@BeforeAll
	void dummy() {
		initAllTagsInfo();

		long[] problemsId = {1000L, 1001, 1002, 1003};
		int[] levels = {1, 1, 8, 8};
		String[] titles = {"A+B", "A-B", "터렛", "피보나치 함수"};
		String[][] tags = {
				{"implementation", "arithmetic", "math"},
				{"implementation", "arithmetic", "math"},
				{"geometry", "math"},
				{"dp"},
		};

		for (int i = 0; i < 4; i++) {
			Problem problem = Problem.builder()
					.id(problemsId[i])
					.level(levels[i])
					.title(titles[i])
					.build();

			problemRepository.save(problem);

			String[] tagsInfo = tags[i];
			for (String tag : tagsInfo) {
				Tag findTag = tagRepository.findByTagName(tag);
				ProblemTag problemTag = ProblemTag.builder()
						.tag(findTag)
						.problem(problem)
						.build();

				problemTagRepository.save(problemTag);
			}
		}

	}

	private void initAllTagsInfo() {
		String[] totalTags = {"implementation", "arithmetic", "math", "geometry", "dp"};
		for (String tag : totalTags) {
			Tag tagEntity = Tag.builder()
					.tagName(tag)
					.build();
			tagRepository.save(tagEntity);
		}
	}

	@Test
	void findRecommendProblem() {
		List<Tag> distinctByTagNameIn = tagRepository.findDistinctByTagNameIn(new HashSet<>(Arrays.asList("dp", "math")));
		Collection<Integer> levels = new ArrayList<>(Arrays.asList(1, 2, 3));
		List<Problem> distinctProblemListByLevelInAndTagIn = problemRepository.findDistinctProblemListByLevelInAndTagIn(levels, distinctByTagNameIn);
		for (Problem problem : distinctProblemListByLevelInAndTagIn) {
			System.out.println("problem.getTitle() = " + problem.getTitle());
		}
	}
}