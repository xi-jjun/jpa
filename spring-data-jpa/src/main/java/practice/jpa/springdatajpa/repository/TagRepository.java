package practice.jpa.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.jpa.springdatajpa.domain.Tag;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
	List<Tag> findDistinctByTagNameIn(Set<String> tagNames);

	Tag findByTagName(String tagName);
}
