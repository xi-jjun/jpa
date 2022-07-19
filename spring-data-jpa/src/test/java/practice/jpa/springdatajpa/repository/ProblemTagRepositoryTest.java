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

import java.util.List;
import java.util.Optional;

/**
 * 개별 태스트의 독립성을 보장하고, 테스트 사이의 상호관계에서 발생하는 부작용을 방지하기 위해,
 * JUnit는 개별 테스트 메서드 의 실행 전, 새로운 인스턴스를 생성한다.
 * 이를 통해 개별 테스트 메서드는 완전히 독립적인 객체 환경에서 동작하며, 이를 메서드 단위 생명주기라 한다.
 *
 * 만약 모든 테스트 메서드를 동일한 인스턴스 환경에서 동작시키고 싶다면, 단순히 @TestInstance 어노테이션을 사용하면 된다.
 *
 * 클래스 단위 생명주기를 가지는 클래스는, 테스트 실행 중 단 하나의 인스턴스를 생성
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // test instance 의 life cycle 을 설정
@Transactional(readOnly = true)
@SpringBootTest
class ProblemTagRepositoryTest {
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
	void findProblemTagsByProblem() {
		Problem findProblem = problemRepository.findById(1000L).get();
		List<ProblemTag> result = problemTagRepository.findProblemTagsByProblem(findProblem);
		for (ProblemTag problemTag : result) {
			System.out.println("problemTag.getTag() = " + problemTag.getTag().getTagName());
		}
	}

	@Test
	void findProblemTagsByProblemIn() {
		List<Problem> problems = problemRepository.findAll();
		List<ProblemTag> problemTagsByProblemIn = problemTagRepository.findProblemTagsByProblemIn(problems);
		for (ProblemTag problemTag : problemTagsByProblemIn) {
			System.out.println("problemTag.getProblem().getTitle() = " + problemTag.getProblem().getTitle());
		}
	}

	@Test
	void findDistinctFirstByProblemIn() {
		List<Problem> problems = problemRepository.findAll();
		List<ProblemTag> distinctFirstByProblemIn = problemTagRepository.findDistinctFirstByProblemIn(problems);
		for (ProblemTag problemTag : distinctFirstByProblemIn) {
			System.out.println("problemTag.getProblem().getTitle() = " + problemTag.getProblem().getTitle());
		}
	}

	@Test
	void findProblemIdListByCustomQuery() {
		List<Problem> problems = problemRepository.findAll();
		List<Long> distinctProblemIdByProblemIn = problemTagRepository.findDistinctProblemIdByProblemIn(problems);
		for (Long id : distinctProblemIdByProblemIn) {
			System.out.println("id = " + id);
		}
	}
}
