package pl.zpo.rdk.system.views;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import pl.zpo.rdk.system.domain.User;
import pl.zpo.rdk.system.domain.UserPrivilege;
import pl.zpo.rdk.system.services.UserService;

@Route("")
public class MainView extends VerticalLayout{
	
	private HttpSession session;
	private HttpServletRequest request;
	Div centralDiv = new Div();
	
	@Autowired
	UserService userService;
	
	public MainView(HttpServletRequest request){
		this.session = request.getSession();
		this.request = request;
	}
	
	//pokazuje kontrolki logowania
	class LoginView extends VerticalLayout{
			
			void refreshCentralDiv() {
				centralDiv.removeAll();
				centralDiv.add(this);
			}
			LoginView()
			{
				//zapisujemy do zmiennej prywatnej przekazane wlasciwosci
				
				//labele
				Label tresc = new Label("Zaloguj się");
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
				
				//kontrolki
				TextField emailTextField = new TextField("Email: ");
				emailTextField.setRequired(true);
				emailTextField.setWidthFull();
				PasswordField passwordTextField = new PasswordField("Hasło: ");
				passwordTextField.setRequired(true);
				passwordTextField.setWidthFull();
				HorizontalLayout buttonsLayout = new HorizontalLayout();
				Button loginButton = new Button("Zaloguj się");
				Button registerButton = new Button("Zarejestruj się");
				///akcja logowania przycisku
				loginButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){ //dluga forna

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						// TODO Auto-generated method stub
						String emailStr = emailTextField.getValue();
						String passwordStr = DigestUtils.sha1Hex(passwordTextField.getValue());
						Optional<User> user = userService.getAll().stream()
						.filter(x -> x.getEmail().contentEquals(emailStr) && x.getPassword().contentEquals(passwordStr))
						.findFirst();
						if(user.isPresent())
						{
							User u = user.get();
							if(u.getPrivilege().equals(UserPrivilege.UNACCEPTED))
							{
								blad.setText("Nie zostałeś jeszcze zaakceptowany!");
								blad.setVisible(true);
							}else {
								blad.setVisible(false);
								request.getSession().setAttribute("loggedUser", user.get());
								UI.getCurrent().getPage().setLocation("/panel");
							}
						}else {
							blad.setText("Złe dane logowania!");
							blad.setVisible(true);
						}
					}
					
				});
				registerButton.addClickListener(x ->{//krotka forma z lambda
					UI.getCurrent().getPage().setLocation("/register");
				});
				buttonsLayout.add(registerButton,loginButton);
				//dodawanie w kolejnosci
				add(tresc);
				add(blad);
				add(emailTextField);
				add(passwordTextField);
				add(buttonsLayout);
				
				refreshCentralDiv();
				
				
			}
		}
	
	
	@PostConstruct //potrzebne do uzywania serwisow; w konstruktorze serwisy nie dzialaja
	void init()
	{		
		this.setHeightFull();
		this.setWidthFull();
		
		this.setJustifyContentMode(JustifyContentMode.CENTER);//w pionie
		this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);//w poziomie
		
		if(session.getAttribute("loggedUser") == null)
		{
			LoginView loginView = new LoginView();
			
		}else {
			add(new Text("Zalogowany"));
			UI.getCurrent().getPage().setLocation("/panel");
		}
		add(centralDiv);
	}
}
