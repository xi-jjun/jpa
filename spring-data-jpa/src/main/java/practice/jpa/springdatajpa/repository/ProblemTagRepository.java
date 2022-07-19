package practice.jpa.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.jpa.springdatajpa.domain.Problem;
import practice.jpa.springdatajpa.domain.ProblemTag;
import practice.jpa.springdatajpa.domain.Tag;

import java.util.Collection;
import java.util.List;

public interface ProblemTagRepository extends JpaRepository<ProblemTag, Long> {
	List<ProblemTag> findProblemTagsByProblem(Problem problem);

	List<ProblemTag> findProblemTagsByProblemIn(Collection<Problem> problems);

	List<ProblemTag> findDistinctFirstByProblemIn(Collection<Problem> problems);

	@Query("SELECT DISTINCT pt.problem.id FROM ProblemTag pt WHERE pt.problem IN :problems")
	List<Long> findDistinctProblemIdByProblemIn(Collection<Problem> problems);


}
