package pl.zpo.rdk.system.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity(name="users")
public class User {

	@Id
	@GeneratedValue
	Long id;
	
	String email;
	String password;
	
	@Enumerated(EnumType.STRING)
	UserPrivilege privilege;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserPrivilege getPrivilege() {
		return privilege;
	}
	public void setPrivilege(UserPrivilege privilege) {
		this.privilege = privilege;
	}
	
	
	
	
}
