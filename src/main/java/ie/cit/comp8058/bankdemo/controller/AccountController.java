package ie.cit.comp8058.bankdemo.controller;

import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ie.cit.comp8058.bankdemo.entity.Account;

@Controller
public class AccountController {

	@Autowired RestTemplate restTemplate;
	
	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@RequestMapping("/accounts")
	public String getAccounts(@CookieValue("bank_token") String accessToken, Model model) {
		
		// TODO - handle error if no cookie? or defaults to null?
		if (accessToken == null) {
			//System.out.println("Access Token Cookie is null");
			return "home";
		}
		
		
		String accounts_uri = "https://api.nordeaopenbanking.com/v2/accounts";

		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		ResponseEntity<String> response = restTemplate.exchange(accounts_uri, HttpMethod.GET, entity, String.class);
		//System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
		//System.out.println(response.getBody());

			
		ObjectMapper objectMapper = new ObjectMapper();

		
		try {
			JsonNode accountsNode = objectMapper.readTree(response.getBody()).path("response").path("accounts");
			JsonParser parser = accountsNode.traverse();
			Account[] accounts = objectMapper.readValue(parser, Account[].class);
			for (Account a : accounts) {
				//System.out.println(a);
			}
			model.addAttribute("accounts", accounts);
			return "accountList";
		} catch (IOException e) {
			e.printStackTrace();
			return "home";
		}
		
	}
}
