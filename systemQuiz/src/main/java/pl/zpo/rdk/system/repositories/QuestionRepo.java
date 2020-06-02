package pl.zpo.rdk.system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.zpo.rdk.system.domain.Question;

public interface QuestionRepo extends JpaRepository<Question, Long>{
	//tu mozesz dodac funkcje wg prawidel z
	//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	//albo przy uzyciu anotacji @Query
	
	List<Question> findByIdEquals(int id);
}
