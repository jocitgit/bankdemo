package ie.cit.comp8058.bankdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ie.cit.comp8058.bankdemo.entity.Token;

@Service
public class NordeaAuthServiceImpl implements AuthService {
	
	@Autowired
	RestTemplate restTemplate;

	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@Value( "${BASE_AUTH_URI}" )
	private String authUri;
		
	@Value( "${REDIRECT_URI}" )
	private String redirectUri;
	
	private static final String STATE_CODE = "12345";
	
	@Override
	public Token getAccessToken(String code, String state) {
				        
        if (code == null || state == null) {
        	return null;
        }
        
        if (!state.equals(STATE_CODE)) {
        	return null;
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

		String uri = authUri + "/access_token";
		Token token = restTemplate.postForObject( uri, request , Token.class );
	
		return token;
		
	}

	@Override
	public String getLoginUri(boolean withUI) {
		String uri = authUri + "?state=" + STATE_CODE + "&client_id=" + clientId + "&redirect_uri=" + redirectUri;
		if (withUI) {
			uri += "&X-Response-Scenarios=AuthenticationWithUI";
		}
		return uri;
	}

}
