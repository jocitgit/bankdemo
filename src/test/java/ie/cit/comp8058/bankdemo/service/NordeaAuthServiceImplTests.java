package ie.cit.comp8058.bankdemo.service;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ie.cit.comp8058.bankdemo.entity.Token;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NordeaAuthServiceImplTests {
	
	@Autowired
	AuthService authService;
	
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
	
	private MockRestServiceServer mockServer;
	
	private String authResponse;
	
	@Before
	public void setup() {
		
		 mockServer = MockRestServiceServer.createServer(restTemplate);
		 
		 authResponse="{\r\n" + 
		 		"  \"access_token\": \"ABCDE\",\r\n" + 
		 		"  \"expires_in\": 7775999,\r\n" + 
		 		"  \"token_type\": \"BEARER\"\r\n" + 
		 		"}";
	}
	

	@Test
	public void testGetAccessToken() {
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("code", "XYZ");
		map.add("redirect_uri", redirectUri);
		
		mockServer.expect(requestTo(authUri + "/access_token"))
		.andExpect(method(HttpMethod.POST))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))	
		.andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE))
		.andExpect(content().formData(map))
        .andRespond(withSuccess(authResponse, MediaType.APPLICATION_JSON));
		
		Token token = authService.getAccessToken("XYZ", "12345");
		
		mockServer.verify();
		
		assertEquals("ABCDE", token.getAccess_token());
		assertEquals("7775999", token.getExpires_in());	
	}

	@Test
	public void testGetLoginUri() {

		String uriWithoutUI = authService.getLoginUri(false);
		String uriWithUI = authService.getLoginUri(true);
				
		assertEquals(authUri + "?state=12345&client_id=" + clientId + "&redirect_uri=" + redirectUri, uriWithoutUI);
		assertEquals(authUri + "?state=12345&client_id=" + clientId + "&redirect_uri=" + redirectUri + "&X-Response-Scenarios=AuthenticationWithUI", uriWithUI);
	}

}
