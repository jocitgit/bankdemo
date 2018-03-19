package ie.cit.comp8058.bankdemo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import ie.cit.comp8058.bankdemo.entity.Token;

@Controller
public class AuthController {

	@Autowired RestTemplate restTemplate;
	
	@Value( "${ACCESS_URL}" )
	private String uri;
	
	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@Value( "${REDIRECT_URI}" )
	private String redirectUri;
	
	private static final String STATE_CODE = "12345";
	
	@RequestMapping("/login")
	public String login(@RequestParam("withUI") boolean withUI) {
		// TODO - Set up http redirect/response here
		
		
		String uri = "https://api.nordeaopenbanking.com/v1/authentication?state=" + STATE_CODE + "&client_id=" + clientId + "&redirect_uri=" + redirectUri;
		if (withUI) {
			uri += "&X-Response-Scenarios=AuthenticationWithUI";
		}
		return "redirect:" + uri;
	}
	
	@RequestMapping("/logout")
    public String logout(HttpServletResponse response) {
		
		Cookie bankCookie = new Cookie("bank_token", "");
		//bankCookie.setHttpOnly(true);
		bankCookie.setMaxAge(0);
		response.addCookie(bankCookie);
		
        return "home";
	}
	
	@RequestMapping("/auth")
    public String test(@RequestParam(value="code", required=false) String code, @RequestParam(value="state", required=false) String state, HttpServletResponse response) {
        //System.out.println("Code: " + code);
        //System.out.println("State: " + state);
        
        if (code == null || state == null) {
        	//TODO - error handling & return
        }
        
        if (state != STATE_CODE) {
        	//TODO - error handling & return
        }
        
             
		//POST request with headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("X-IBM-Client-Id", clientId);
		headers.add("X-IBM-Client-Secret", clientSecret);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", code);
		map.add("redirect_uri", redirectUri);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		//ResponseEntity<String> response = restTemplate.postForEntity( uri, request , String.class );

		
		//get/postForObject/Entity throw RestClientException
		
		Token token = restTemplate.postForObject( uri, request , Token.class );
		//System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
		//System.out.println(response.getBody());
		//System.out.println(token.getAccess_token());
		//System.out.println(token.getToken_type());
		//System.out.println(token.getExpires_in());
	
		
		//store token in cookie and redirect
		
		Cookie bankCookie = new Cookie("bank_token", token.getAccess_token());
		bankCookie.setHttpOnly(true);
		bankCookie.setMaxAge(Integer.parseInt(token.getExpires_in()));
		response.addCookie(bankCookie);
		// Do redirect not return here to update url
		return "redirect:/main";
		
	}
}
