package ie.cit.comp8058.bankdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ie.cit.comp8058.bankdemo.entity.Token;


@Controller
public class TestController {
	
	@Autowired RestTemplate restTemplate;
	
	//Check how to make params optional - or set default values
	@RequestMapping("/test")
    public String test(@RequestParam(value="code", defaultValue="") String code, @RequestParam(value="state", defaultValue="") String state) {
        System.out.println("Code: " + code);
        System.out.println("State: " + state);
        
        String uri = "https://api.nordeaopenbanking.com/v1/authentication/access_token";
        String client_id = "cb8d8af2-2416-4ce5-bb6c-2344e09793ec";
		String client_secret = "H2xH5gN2lX0aF3fH6cD7qN0hS3dV8fS2fH8eV3dO7uF0aM6kD4";
        
		
		//POST request with headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("X-IBM-Client-Id", client_id);
		headers.add("X-IBM-Client-Secret", client_secret);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", code);
		map.add("redirect_uri", "http://localhost:8081/test");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		//ResponseEntity<String> response = restTemplate.postForEntity( uri, request , String.class );

		
		//get/postForObject/Entity throw RestClientException
		
		Token token = restTemplate.postForObject( uri, request , Token.class );
		//System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
		//System.out.println(response.getBody());
		System.out.println(token.getAccess_token());
		System.out.println(token.getToken_type());
		System.out.println(token.getExpires_in());
	
		String access_token = token.getAccess_token();
		//store token in cookie and redirect??
		
        String accounts_uri = "https://api.nordeaopenbanking.com/v2/accounts";
		
        
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + access_token);
		headers2.add("X-IBM-Client-Id", client_id);
		headers2.add("X-IBM-Client-Secret", client_secret);
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		ResponseEntity<String> response = restTemplate.exchange(accounts_uri, HttpMethod.GET, entity, String.class);
        System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
		System.out.println(response.getBody());
        
        
        
        
        
		return "home";
    }
}
