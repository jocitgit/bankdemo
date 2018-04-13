package ie.cit.comp8058.bankdemo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;

@Repository
@Profile("!demo")
public class NordeaAccountDao implements AccountDao {

	@Autowired RestTemplate restTemplate;
	
	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@Value( "${BASE_ACCOUNTS_URI}" )
	private String accountsUri;
	
	@Override
	public Account[] getAllAccounts(String accessToken) {
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		ResponseEntity<String> response = restTemplate.exchange(accountsUri, HttpMethod.GET, entity, String.class);
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
			return accounts;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Account getAccountById(String accessToken, String id) {
		String uri = accountsUri + "/" + id;

		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		//System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
		//System.out.println(response.getBody());
		
			
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode accountsNode = objectMapper.readTree(response.getBody()).path("response");
			JsonParser parser = accountsNode.traverse();
			Account account = objectMapper.readValue(parser, Account.class);
			
			return account;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey) {
		
		String previousKey = "";
		
		String uri = accountsUri + "/" + id + "/transactions";
		
		if ((continuationKey != null) && !(continuationKey.isEmpty())) {
			uri += "?continuationKey=" + continuationKey;
			
			int separatorIndex = continuationKey.indexOf("-");
			if (separatorIndex > 0) {
				String keyBase = continuationKey.substring(0, separatorIndex+1);
				//System.out.println("base: " + keyBase);
				try {
					int pageNumber = Integer.parseInt(continuationKey.substring(separatorIndex+1));
					if (pageNumber > 0) { 
						previousKey = keyBase + (pageNumber-1);
					}
				} catch (NumberFormatException e) {
					// do not update previousKey
				}
			}
		}

		//System.out.println(uri);
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		TransactionPage txnPage = new TransactionPage();
		ObjectMapper objectMapper = new ObjectMapper();		
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		//System.out.println(response.getBody());
		try {
			JsonNode responseNode = objectMapper.readTree(response.getBody()).path("response"); 
			txnPage.setNextKey(responseNode.path("_continuationKey").asText());
			
			///// TEMPORARY FIX FOR ERROR IN NORDEA API ////
			if ((continuationKey == null) || (continuationKey.isEmpty())) {
				String oldKey = txnPage.getNextKey();
				if (oldKey != null && !oldKey.isEmpty()) {
					String newKey = oldKey.substring(0,oldKey.length()-1) + "1";
					txnPage.setNextKey(newKey);
				}
			}
			////////////////////////////////////////////////

			JsonParser parser = responseNode.path("transactions").traverse();
			txnPage.setTransactions(objectMapper.readValue(parser, new TypeReference<List<Transaction>>(){}));
			txnPage.setPreviousKey(previousKey);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return txnPage;
	}
	
	@Override
	public TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate, String continuationKey) {
		
		String previousKey = "";
		
		String uri = accountsUri + "/"+id+"/transactions";
		uri += "?fromDate=" + fromDate + "&toDate=" + toDate;
		
		if ((continuationKey != null) && !(continuationKey.isEmpty())) {
			uri += "&continuationKey=" + continuationKey;
			
			int separatorIndex = continuationKey.indexOf("-");
			if (separatorIndex > 0) {
				String keyBase = continuationKey.substring(0, separatorIndex+1);
				//System.out.println("base: " + keyBase);
				try {
					int pageNumber = Integer.parseInt(continuationKey.substring(separatorIndex+1));
					if (pageNumber > 0) { 
						previousKey = keyBase + (pageNumber-1);
					}
				} catch (NumberFormatException e) {
					// do not update previousKey
				}
			}
		}

		//System.out.println(uri);
		
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		TransactionPage txnPage = new TransactionPage();
		ObjectMapper objectMapper = new ObjectMapper();		
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		try {
			JsonNode responseNode = objectMapper.readTree(response.getBody()).path("response"); 
			txnPage.setNextKey(responseNode.path("_continuationKey").asText());

			///// TEMPORARY FIX FOR ERROR IN NORDEA API ////
			if ((continuationKey == null) || (continuationKey.isEmpty())) {
				String oldKey = txnPage.getNextKey();
				if (oldKey != null && !oldKey.isEmpty()) {
					String newKey = oldKey.substring(0,oldKey.length()-1) + "1";
					txnPage.setNextKey(newKey);
				}
			}
			////////////////////////////////////////////////

			JsonParser parser = responseNode.path("transactions").traverse();
			txnPage.setTransactions(objectMapper.readValue(parser, new TypeReference<List<Transaction>>(){}));
			txnPage.setPreviousKey(previousKey);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return txnPage;
	}


	@Override
	public List<Transaction> getTransactionsByAccountId(String accessToken, String id) {
		String uri = accountsUri + "/"+id+"/transactions";

		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);

		String nextUri = uri;
		ResponseEntity<String> response;
		JsonNode responseNode;
		JsonParser parser;
		ArrayList<Transaction> txns = new ArrayList<Transaction>();
		String continuationKey = "";
		ObjectMapper objectMapper = new ObjectMapper();		

		do {
			response = restTemplate.exchange(nextUri, HttpMethod.GET, entity, String.class);
			//System.out.println("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
			//System.out.println(response.getBody());

			try {
				responseNode = objectMapper.readTree(response.getBody()).path("response"); 

				///// TEMPORARY FIX FOR ERROR IN NORDEA API ////
				if (continuationKey.isEmpty()) { // First time through only
					String oldKey = responseNode.path("_continuationKey").asText();
					if (oldKey != null && !oldKey.isEmpty()) {
						String newKey = oldKey.substring(0,oldKey.length()-1) + "1";
						continuationKey = newKey;
					} else {
						continuationKey = oldKey; // Only one page
					}
				} else {
					// Standard code if no Nordea error
					continuationKey = responseNode.path("_continuationKey").asText();
				}
				///////////////////////////////////////////////////
				
				//System.out.println("key(" + continuationKey + ")");
				nextUri = uri + "?continuationKey=" + continuationKey;
				parser = responseNode.path("transactions").traverse();
				//Transaction[] txns = objectMapper.readValue(parser, Transaction[].class);
				txns.addAll(objectMapper.readValue(parser, new TypeReference<List<Transaction>>(){}));

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} while (!continuationKey.isEmpty());
		return txns;
	}

	@Override
	public List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate) {
		
		String uri = accountsUri + "/"+id+"/transactions";
		uri += "?fromDate=" + fromDate + "&toDate=" + toDate;

		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.add("Accept", "application/json");
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("X-IBM-Client-Id", clientId);
		headers2.add("X-IBM-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers2);
		
		String nextUri = uri;
		ResponseEntity<String> response;
		JsonNode responseNode;
		JsonParser parser;
		ArrayList<Transaction> txns = new ArrayList<Transaction>();
		String continuationKey = "";
		ObjectMapper objectMapper = new ObjectMapper();		


		do {
			
			response = restTemplate.exchange(nextUri, HttpMethod.GET, entity, String.class);
			//System.out.println(response.getBody());
			try {
				responseNode = objectMapper.readTree(response.getBody()).path("response"); 
				
				///// TEMPORARY FIX FOR ERROR IN NORDEA API ////
				if (continuationKey.isEmpty()) { // First time through only
					String oldKey = responseNode.path("_continuationKey").asText();
					if (oldKey != null && !oldKey.isEmpty()) {
						String newKey = oldKey.substring(0,oldKey.length()-1) + "1";
						continuationKey = newKey;
					} else {
						continuationKey = oldKey; // Only one page
					}
				} else {
					// Standard code if no Nordea error
					continuationKey = responseNode.path("_continuationKey").asText();
				}
				/////////////////////////////////////////////////
				
				
				
				nextUri = uri + "&continuationKey=" + continuationKey;

				parser = responseNode.path("transactions").traverse();
				//Transaction[] txns = objectMapper.readValue(parser, Transaction[].class);
				txns.addAll(objectMapper.readValue(parser, new TypeReference<List<Transaction>>(){}));

			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		} while (!continuationKey.isEmpty());

		return txns;
	}


}
