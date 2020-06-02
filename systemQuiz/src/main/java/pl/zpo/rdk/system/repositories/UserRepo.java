package pl.zpo.rdk.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.zpo.rdk.system.domain.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
}
