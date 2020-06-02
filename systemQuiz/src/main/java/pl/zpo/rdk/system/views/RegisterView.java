package pl.zpo.rdk.system.views;

import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import pl.zpo.rdk.system.mailer.EmailSender;
import pl.zpo.rdk.system.services.UserService;
import pl.zpo.rdk.system.domain.User;
import pl.zpo.rdk.system.domain.UserPrivilege;

@Route("register")
public class RegisterView extends VerticalLayout{

	@Autowired
	EmailSender emailSender;
	
	@Autowired
	UserService userService;
	
	private HttpServletRequest request;
	
	public RegisterView(HttpServletRequest request){
		this.request = request;
	}
	
	@PostConstruct
	void init()
	{
		//this.setHeightFull();
		//this.setWidthFull();
		
		this.setJustifyContentMode(JustifyContentMode.CENTER);//w pionie
		this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);//w poziomie
		
		Label tresc = new Label("Zarejestruj się");
		tresc.getStyle().set("fontFamily", "Helevetica");
		tresc.getStyle().set("fontSize", "200%");
		tresc.getStyle().set("textAlign", "center");
		tresc.setSizeFull();
		
		Label blad = new Label("Błędne dane logowania!");
		blad.getStyle().set("color", "#F00");
		blad.getStyle().set("fontSize", "100%");
		blad.getStyle().set("textAlign", "center");
		blad.setSizeFull();
		blad.setVisible(false);
		
		TextField emailTextField = new TextField("Email w domenie utp.edu.pl");
		
		Button sendRequest = new Button("Wyślij prośbę");
		sendRequest.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				// TODO Auto-generated method stub
				String emailStr = emailTextField.getValue();
				if(emailStr.matches(".+@utp.edu.pl"))
				{
					if(userService.getAll().stream()
							.filter(x -> x.getEmail().contentEquals(emailStr))
							.findFirst().isPresent())
					{
						blad.getStyle().set("color", "#F00");
						blad.setText("Wysłałeś już prośbę!");
						blad.setVisible(true);
					}else {
						User newUser = new User();
						newUser.setEmail(emailStr);
						if(userService.getAll().size()==0)
						{
							newUser.setPrivilege(UserPrivilege.ADMIN);							
						}else
						{
							newUser.setPrivilege(UserPrivilege.UNACCEPTED);							
						}
						
						String generatedPassword = "";
						Random random = new Random();
						for(int i=0; i<8; i++)//tu nie da sie chyba zrobic tego stremem
						{
							int randomInteger = 65+random.nextInt(90-65);
							int letterCase = random.nextInt(2);//0 duza 1 mala
							String toAdd = String.valueOf((char)randomInteger);
							if(letterCase!=0)
							{
								toAdd=toAdd.toLowerCase();
							}
							generatedPassword+=toAdd;
						}
						System.out.println("Password generated: "+generatedPassword);
						System.out.println("Password hashed: "+DigestUtils.sha1Hex(generatedPassword));
						newUser.setPassword(DigestUtils.sha1Hex(generatedPassword));
						
						emailSender.sendEmail(emailStr, "System Quizowy - hasło", 
											"Cześć! <br> "
											+ "Twój login to: "+emailStr+"<br>"
											+ "Twoje hasło to: "+generatedPassword+"<br>"
											+ "<b>Jeśli nie wiesz czemu dostałeś tą wiadomość, usuń ją!</b>");
						
						userService.addUser(newUser);
						
						emailTextField.setEnabled(false);
						blad.setText("Pomyślnie dodano użytkownika! Sprawdz swoją skrzynkę.");
						blad.getStyle().set("color", "#0F0");
						blad.setVisible(true);
						sendRequest.setEnabled(false);
					}
		
		
				}else {	
					blad.getStyle().set("color", "#F00");
					blad.setText("Adres musi być w domenie utp.edu.pl");
					blad.setVisible(true);
				}
			}
			
		});
		
		add(tresc);
		add(blad);
		add(emailTextField);
		add(sendRequest);
		
	}
}
