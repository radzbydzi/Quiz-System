package pl.zpo.rdk.system.views;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import pl.zpo.rdk.system.domain.Question;
import pl.zpo.rdk.system.domain.User;
import pl.zpo.rdk.system.domain.UserPrivilege;
import pl.zpo.rdk.system.services.QuestionService;
import pl.zpo.rdk.system.services.UserService;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;

@Route("panel")
public class PanelView extends FlexLayout{
	
	@Autowired
	QuestionService questionService;
	@Autowired
	UserService userService;
	
	HttpServletRequest request;
	Div rightPanel = new Div();
	User loggedUser;
	
	//wzor na layout z bold i normal
	HorizontalLayout boldLableNormalLabel(String bold, String normal)
	{
		HorizontalLayout res = new HorizontalLayout();
		Label boldLabel = new Label(bold);
		boldLabel.getStyle().set("fontWeight", "bold");
		Label normalLabel = new Label(normal);
		res.add(boldLabel,normalLabel);
		return res;
	}
	
	//wnetrza prawego panelu
	//lista wszystkich pytan
	class AllQuestionView extends VerticalLayout{
		Grid<Question> questionsGrid = new Grid<>();//taka tabela
		
		void setNewDataProviderOfGrid(String phrase)
		{
			//lazy loading; nie wczytuje wszystkiego na raz
			CallbackDataProvider<Question, Void> provider = DataProvider
			        .fromCallbacks(query -> questionService.getAllContainingPhrase(
			        		query.getOffset(),
			        		query.getLimit(),
			        		phrase)
			        		.stream(),
			                query -> questionService.getAllContainingPhrase(
					        		query.getOffset(),
					        		query.getLimit(),
					        		phrase).size());
			questionsGrid.setDataProvider(provider);
		}
		
		AllQuestionView(){
			questionsGrid.setDetailsVisibleOnClick(true);
			Label infoLabel = new Label("Wszystkie pytania");
			add(infoLabel);
			TextField filterTextField = new TextField("Filtr");
			filterTextField.setValueChangeMode(ValueChangeMode.EAGER);
			filterTextField.addValueChangeListener(event -> {
				setNewDataProviderOfGrid(event.getValue());
			});
			questionsGrid.removeAllColumns();
			questionsGrid.setSelectionMode(SelectionMode.SINGLE);
			questionsGrid.setItems(questionService.getAll());
			questionsGrid.addColumn(Question::getQuestion).setHeader("Pytanie");
			questionsGrid.addColumn(Question::getCorrectAnswerValue).setHeader("Poprawna odpowedź");
			questionsGrid.addColumn(Question::getAnswerA).setHeader("Odpowiedź A");
			questionsGrid.addColumn(Question::getAnswerB).setHeader("Odpowiedź B");
			questionsGrid.addColumn(Question::getAnswerC).setHeader("Odpowiedź C");
			questionsGrid.addColumn(Question::getAnswerD).setHeader("Odpowiedź D");
			
			questionsGrid.setItemDetailsRenderer(
				    new ComponentRenderer<>(x -> {
				        VerticalLayout layout = new VerticalLayout();
				        layout.add(boldLableNormalLabel("Pytanie: ", x.getQuestion()));
				        layout.add(boldLableNormalLabel("Poprawna odpowedź: ", x.getCorrectAnswerValue()));
				        layout.add(boldLableNormalLabel("Odpowiedź A: ", x.getAnswerA()));
				        layout.add(boldLableNormalLabel("Odpowiedź B: ", x.getAnswerB()));
				        layout.add(boldLableNormalLabel("Odpowiedź C: ", x.getAnswerC()));
				        layout.add(boldLableNormalLabel("Odpowiedź D: ", x.getAnswerD()));
				        return layout;
			}));
			
			setNewDataProviderOfGrid(filterTextField.getValue());
			
			add(filterTextField,questionsGrid);
		}
	}
	//lista uzytkownikow
	class UserListView extends VerticalLayout{
		
		Button delButton(User u)
		{
			Button res = new Button("Usuń");
			res.setIcon(VaadinIcon.TRASH.create());
			if(u.getId() == loggedUser.getId())//jesli to nasz uzytkownik to nie mozna usunac
			{
				res.setEnabled(false);
			}else {
				res.addClickListener(x -> {
					userService.delUser(u);	
					fillView();
				});
			}
			return res;
		}
		ComboBox<UserPrivilege> privilegeCheckBox(User u)
		{
			ComboBox<UserPrivilege> res = new ComboBox<>();
			res.setItems(UserPrivilege.values());
			res.setItemLabelGenerator(UserPrivilege::getName);
			res.setValue(u.getPrivilege());
			if(u.getId() == loggedUser.getId())//jesli to nasz uzytkownik to nie mozna usunac
			{
				res.setEnabled(false);
			}else
			{
				res.addValueChangeListener(x -> {
					u.setPrivilege(x.getValue());
					userService.addUser(u);
					fillView();
				});
			}
			
			return res;
		}
		void fillView()
		{
			removeAll();
			Label infoLabel = new Label("Lista użytkowników");
			add(infoLabel);
			Grid<User> userGrid = new Grid<>();//taka tabela
			userGrid.removeAllColumns();
			userGrid.setSelectionMode(SelectionMode.SINGLE);
			userGrid.setItems(userService.getAll());
			userGrid.addColumn(User::getEmail).setHeader("Email");
			userGrid.addComponentColumn(this::privilegeCheckBox).setHeader("Przywilej");//dodaje guzik
			userGrid.addComponentColumn(this::delButton).setHeader("Usuń");
			add(userGrid);
			
		}
		UserListView(){
			fillView();
		}
	}
	//lista pytan uzytkownika
	class UserQuestionView extends VerticalLayout{
		//okno dodawania pytan
		class AddQuestionWindowView extends VerticalLayout{
			AddQuestionWindowView(UserQuestionView root){
				this.setWidthFull();
				Label infoLabel = new Label("Dodaj pytanie");
				TextField question = new TextField("Pytanie");
				question.setWidthFull();
				HorizontalLayout correctAnswerLayout = new HorizontalLayout();
				correctAnswerLayout.setWidthFull();
				//-----
				List<String> data = Arrays.asList("A", "B", "C", "D");
				 
				RadioButtonGroup<String> radioButton = new RadioButtonGroup<>();
				radioButton.setLabel("Poprawna odpowiedź");
				radioButton.setItems(data);
				radioButton.setValue(data.get(0));
				correctAnswerLayout.add(radioButton);
				//----
				TextField answerA = new TextField("Odpowiedź A");
				answerA.setWidthFull();
				TextField answerB = new TextField("Odpowiedź B");
				answerB.setWidthFull();
				TextField answerC = new TextField("Odpowiedź C");
				answerC.setWidthFull();
				TextField answerD = new TextField("Odpowiedź D");
				answerD.setWidthFull();
				Button addQuestionButton = new Button("Dodaj pytanie");
				addQuestionButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){

						@Override
						public void onComponentEvent(ClickEvent<Button> event) {
							// TODO Auto-generated method stub
							if(loggedUser!=null)
							{
								Question newQuestion = new Question();
								
								newQuestion.setAddDateAndTime(LocalDateTime.now());
								newQuestion.setAuthor(loggedUser);
								newQuestion.setQuestion(question.getValue());
								newQuestion.setAnswerA(answerA.getValue());
								newQuestion.setAnswerB(answerB.getValue());
								newQuestion.setAnswerC(answerC.getValue());
								newQuestion.setAnswerD(answerD.getValue());
								newQuestion.setCorrectAnswer(radioButton.getValue());
								
								questionService.addQuestion(newQuestion);
							}
							root.fillView();
						}
					
				});
				//dodawanie do widoku
				add(infoLabel,question, correctAnswerLayout,answerA,answerB,answerC,answerD,addQuestionButton);
			}
		}
		
		Button delButton(Question q)//w argumencie ma aktualny obiekt question
		{
			Button res = new Button("Usuń");
			res.setIcon(VaadinIcon.TRASH.create());
			res.addClickListener(x -> {//krotsza forma niz poprzednio uzywane
				questionService.delQuestion(q);
				fillView();
			});
			return res;			
		}
		public void fillView()
		{
			removeAll();
			Label infoLabel = new Label("Moje pytania");
			add(infoLabel);
			Button addQuestion = new Button("Add question");
			addQuestion.addClickListener(x ->{
				removeAll();
				add(new AddQuestionWindowView(this));
			});
			add(addQuestion);
			Grid<Question> questionsGrid = new Grid<>();//taka tabela
			questionsGrid.removeAllColumns();
			questionsGrid.setSelectionMode(SelectionMode.SINGLE);
			questionsGrid.setItems(questionService.getAll());
			questionsGrid.addColumn(Question::getQuestion).setHeader("Pytanie");
			questionsGrid.addColumn(Question::getCorrectAnswerValue).setHeader("Poprawna odpowedź");
			questionsGrid.addColumn(Question::getAnswerA).setHeader("Odpowiedź A");
			questionsGrid.addColumn(Question::getAnswerB).setHeader("Odpowiedź B");
			questionsGrid.addColumn(Question::getAnswerC).setHeader("Odpowiedź C");
			questionsGrid.addColumn(Question::getAnswerD).setHeader("Odpowiedź D");
			questionsGrid.addComponentColumn(this::delButton).setHeader("Usuń");//dodaje guzik
			questionsGrid.setItems(questionService.getAllOf(loggedUser));
			questionsGrid.setItemDetailsRenderer(
				    new ComponentRenderer<>(x -> {
				        VerticalLayout layout = new VerticalLayout();
				        layout.add(boldLableNormalLabel("Pytanie: ", x.getQuestion()));
				        layout.add(boldLableNormalLabel("Poprawna odpowedź: ", x.getCorrectAnswerValue()));
				        layout.add(boldLableNormalLabel("Odpowiedź A: ", x.getAnswerA()));
				        layout.add(boldLableNormalLabel("Odpowiedź B: ", x.getAnswerB()));
				        layout.add(boldLableNormalLabel("Odpowiedź C: ", x.getAnswerC()));
				        layout.add(boldLableNormalLabel("Odpowiedź D: ", x.getAnswerD()));
				        return layout;
			}));
			add(questionsGrid);
		}
		UserQuestionView(){
			fillView();
		}
	}
	
	//menu lewa
	class MenuView extends VerticalLayout{
		MenuView()
		{
			if(loggedUser!=null)
			{
				Button allQuestionButton = new Button("Wszystkie pytania");
				allQuestionButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						// TODO Auto-generated method stub
						rightPanel.removeAll();
						rightPanel.add(new AllQuestionView());
					}
					
				});
				Button myQuestionButton = new Button("Moje pytania");
				myQuestionButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						// TODO Auto-generated method stub
						rightPanel.removeAll();
						rightPanel.add(new UserQuestionView());
					}
					
				});
				add(allQuestionButton);
				add(myQuestionButton);
				
				if(loggedUser.getPrivilege().equals(UserPrivilege.ADMIN))
				{
					Button userListButton = new Button("Użytkownicy");
					userListButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){

						@Override
						public void onComponentEvent(ClickEvent<Button> event) {
							// TODO Auto-generated method stub
							rightPanel.removeAll();
							rightPanel.add(new UserListView());
						}
						
					});
					add(userListButton);
				}
				Button logOutButton = new Button("Wyloguj");
				logOutButton.getStyle().set("backgroundColor", "#A00");
				logOutButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>(){

					@Override
					public void onComponentEvent(ClickEvent<Button> event) {
						// TODO Auto-generated method stub
						request.getSession().invalidate();
						UI.getCurrent().getPage().setLocation("/");
					}
					
				});
				add(logOutButton);
			}
			
		}
	}
	
	PanelView(HttpServletRequest request){
		this.request = request;
		if(request.getSession().getAttribute("loggedUser")!=null)
		{
			loggedUser = (User) request.getSession().getAttribute("loggedUser");
		}
	}
	
	@PostConstruct
	void init() {
		if(loggedUser==null)
		{
			UI.getCurrent().getPage().setLocation("/");			
		}else {
			this.setWidthFull();
			this.setHeightFull();
			MenuView menu = new MenuView();
			menu.setWidth("20%");
			add(menu);
			rightPanel.removeAll();
			rightPanel.add(new AllQuestionView());
			rightPanel.setWidth("80%");
			add(rightPanel);
		}
	}
}
