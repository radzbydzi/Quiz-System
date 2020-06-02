package pl.zpo.rdk.system.services;

import java.util.List;

import pl.zpo.rdk.system.domain.Question;
import pl.zpo.rdk.system.domain.User;

public interface QuestionService {
	void addQuestion(Question q);
	void delQuestion(Question q);
	void delQuestionsOf(User user);
	
	List<Question> getAll();
	List<Question> getAllOf(User user);
	List<Question> getAllContainingPhrase(int offset, int limit, String phrase);
	List<Question> getAllContainingPhrase(String phrase);
	
	int getCount();
}
