package pl.zpo.rdk.system.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.zpo.rdk.system.services.QuestionService;

@RestController
public class RestQuestionCSV {
	@Autowired
	QuestionService questionService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/allAnswers")
	public ResponseEntity<String> questionWithAll() {
		String header = "\"Pytanie\", \"Poprawna odpowiedź\", \"Odpowiedź A\", \"Odpowiedź B\", \"Odpowiedź C\", \"Odpowiedź D\"";
		StringBuilder content = new StringBuilder();
		questionService.getAll().stream()
		.forEach(x -> {
			content.append("\""+x.getQuestion()+"\", ");
			content.append("\""+x.getCorrectAnswer()+"\", ");
			content.append("\""+x.getAnswerA()+"\", ");
			content.append("\""+x.getAnswerB()+"\", ");
			content.append("\""+x.getAnswerC()+"\", ");
			content.append("\""+x.getAnswerD()+"\"");
			content.append("\r\n");
		});
		String result = header+"\r\n"+content.toString();
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/allGoodAnswers")
	public ResponseEntity<String> questionWithAllGood() {
		String header = "\"Pytanie\", \"Poprawna odpowiedź\"";
		StringBuilder content = new StringBuilder();
		questionService.getAll().stream()
		.forEach(x -> {
			content.append("\""+x.getQuestion()+"\", ");
			content.append("\""+x.getCorrectAnswer()+"\"");
			content.append("\r\n");
		});
		String result = header+"\r\n"+content.toString();
		
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity<String>(result, responseHeaders, HttpStatus.CREATED);
	}
}
