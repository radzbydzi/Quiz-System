package pl.zpo.rdk.system.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.zpo.rdk.system.domain.User;
import pl.zpo.rdk.system.repositories.UserRepo;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepo userRepo;
	
	//do usuniecie z bazy pytan uzytkownika przed jego usunieciem
	@Autowired
	QuestionService questionService;
	
	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		userRepo.save(user);
	}

	@Override
	public void delUser(User user) {
		// TODO Auto-generated method stub
		questionService.delQuestionsOf(user);
		userRepo.delete(user);
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated userRepo stub
		System.out.println(">-------------------------------------------------");
		List<User> result = userRepo.findAll();
		System.out.println(">-------------------------------------------------"+result==null?"n":"nn");
		if(result == null)
			result = new ArrayList<>();
		return result;
	}

}
