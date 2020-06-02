package pl.zpo.rdk.system.domain;

//typ wyliczeniowy odpowiedzialny za pokazanie jakie przywileje ma uzytkownik
public enum UserPrivilege {
	UNACCEPTED("UNACCEPTED"),
	USER("USER"), 
	ADMIN("ADMIN");
	
	//nazwa przywileju w String
	private String name;
	
	UserPrivilege(String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return getName();
	}
}
