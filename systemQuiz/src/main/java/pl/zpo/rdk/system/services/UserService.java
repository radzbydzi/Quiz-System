package pl.zpo.rdk.system.services;

import java.util.List;

import pl.zpo.rdk.system.domain.User;

public interface UserService {
	void addUser(User user);
	void delUser(User user);
	List<User> getAll();
}
