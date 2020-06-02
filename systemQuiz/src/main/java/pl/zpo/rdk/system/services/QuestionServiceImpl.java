package pl.zpo.rdk.system.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.zpo.rdk.system.domain.Question;
import pl.zpo.rdk.system.domain.User;
import pl.zpo.rdk.system.repositories.QuestionRepo;

@Service
public class QuestionServiceImpl implements QuestionService{

	//odwolujemy sie do repozytorium (zauwaz ze to typ interfejsu a nie klasy implementujacej interfejs)
	@Autowired
	QuestionRepo questionRepo;

	
	@Override
	public void addQuestion(Question q) {
		// TODO Auto-generated method stub
		questionRepo.save(q); //jesli nie ma encji w bazie to tworzy nowa, jesli jest to ja aktualizuje
	}

	@Override
	public void delQuestion(Question q) {
		// TODO Auto-generated method stub
		questionRepo.delete(q);
	}

	@Override
	public void delQuestionsOf(User user) {
		// TODO Auto-generated method stub
		//za pomoca stream
		List<Question> questions = getAll();
		
		questions.stream()
		.filter(x -> x.getAuthor().getId() == user.getId())//daje dalej tylko obiekty spelniajace zaleznosc
		.forEach(x -> {
			delQuestion(x);
		});
		
	}

	@Override
	public List<Question> getAll() {
		// TODO Auto-generated method stub
		List<Question> res = questionRepo.findAll();
		if(res == null)
			res = new ArrayList<>(); 
		return res;
	}

	@Override
	public List<Question> getAllOf(User user) {
		// TODO Auto-generated method stub
		List<Question> res =  getAll().stream()
				.filter(x -> x.getAuthor().getId().equals(user.getId()))
				.collect(Collectors.toList());//tworzenie listy ze stream
		if(res == null)
			res = new ArrayList<>(); 
		return res;
	}

	@Override
	public List<Question> getAllContainingPhrase(int offset, int limit, String phrase) {
		// TODO Auto-generated method stub
		List<Question> allQuestions = getAll();
		List<Question> filtered = allQuestions.stream()
				.filter(x -> x.getQuestion().contains(phrase) ||
						     x.getAnswerA().contains(phrase) ||
						     x.getAnswerB().contains(phrase) ||
						     x.getAnswerC().contains(phrase) ||
						     x.getAnswerD().contains(phrase))
				.skip(offset)
				.limit(limit)
				.collect(Collectors.toList());
		if(filtered == null)
		{
			filtered = new ArrayList<>();
		}
			
		
		return filtered;
	}
	
	@Override
	public List<Question> getAllContainingPhrase(String phrase) {
		return getAllContainingPhrase(0,getCount(),phrase);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getAll().size();
	}

}
