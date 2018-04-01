package ie.cit.comp8058.bankdemo.controller;

import java.io.IOException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class GlobalExceptionHandlerController  {

	
	// Handle error responses from RestTemplate API calls
	@ExceptionHandler({HttpClientErrorException.class})
	public String handleHttpError(Model model, HttpClientErrorException e) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String failureCode = "";
		String failureDesc = "";
		
		try {
			JsonNode firstFailureNode = objectMapper.readTree(e.getResponseBodyAsString()).path("error").path("failures").get(0);
			if (firstFailureNode != null) {
				failureCode = firstFailureNode.path("code").asText();
				failureDesc = firstFailureNode.path("description").asText();
			}
		} catch (IOException ioError) {
			ioError.printStackTrace();
		}
		
		model.addAttribute("error", "HttpClientErrorException");
		model.addAttribute("message", e.getMessage());
		model.addAttribute("failureCode", failureCode);
		model.addAttribute("failureDesc", failureDesc);
		
		return "error";
	}
	
	
}
