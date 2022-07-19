package practice.jpa.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practice.jpa.springdatajpa.domain.Problem;
import practice.jpa.springdatajpa.domain.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
	List<Problem> findProblemsByLevelIn(Set<Integer> levels);

	@Query("SELECT DISTINCT pt.problem FROM ProblemTag pt WHERE pt.problem.level IN :levels AND pt.tag IN :tags")
	List<Problem> findDistinctProblemListByLevelInAndTagIn(Collection<Integer> levels, Collection<Tag> tags);
}
