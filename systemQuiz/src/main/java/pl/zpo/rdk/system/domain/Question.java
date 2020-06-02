package pl.zpo.rdk.system.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//!UWAGA! Jesli napiszesz np. userName to przekonwertuje je w bazie na user_name (SpringNamingStrategy)

//Tip:
//@Column(name="importance", columnDefinition="VARCHAR(25)")
//String imp;
//Dodajac to przed zmienna zdefiniujesz nazwe kolumny i typ kolumny (jest przydatny gdy tabela jeszcze nie istnieje)

@Entity(name="questions")//encja; bedzie zapisywac do tabeli questions; bez name zapisaloby do question
public class Question {
	
	//id bedzie mialo unikalna wartosc
	@Id
	@GeneratedValue
	Long id;
	
	String question;
	String answerA;
	String answerB;
	String answerC;
	String answerD;
	String correctAnswer;
	
	LocalDateTime addDateAndTime;//data i czas dodania pytania
	
	//relacja jeden do wielu; jeden autor ma wiele pytan
	@ManyToOne
	User author;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswerA() {
		return answerA;
	}

	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	public String getAnswerB() {
		return answerB;
	}

	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	public String getAnswerC() {
		return answerC;
	}

	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	public String getAnswerD() {
		return answerD;
	}

	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}
	
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public String getCorrectAnswerValue()
	{
		switch(correctAnswer)
		{
		case "A":
			return answerA;
		case "B":
			return answerB;
		case "C":
			return answerC;
		case "D":
			return answerD;
		default:
			return null;
		}
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public LocalDateTime getAddDateAndTime() {
		return addDateAndTime;
	}

	public void setAddDateAndTime(LocalDateTime addDateAndTime) {
		this.addDateAndTime = addDateAndTime;
	}
	
	
}
