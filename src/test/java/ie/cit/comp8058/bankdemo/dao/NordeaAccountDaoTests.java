package ie.cit.comp8058.bankdemo.dao;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NordeaAccountDaoTests {

	@Autowired
	AccountDao accountDao;
	
	@Autowired 
	RestTemplate restTemplate;
	
	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@Value( "${BASE_ACCOUNTS_URI}" )
	private String accountsUri;
	
	private MockRestServiceServer mockServer;
	
	private String accountListResponse;
	private String singleAccountResponse;
	private String txnsFirstPageResponse;
	private String txnsLastPageResponse;
	
	
	@Before
	public void setup() {
		
		 mockServer = MockRestServiceServer.createServer(restTemplate);
		 
		 accountListResponse = "{\"groupHeader\":\r\n" + 
		 		"	{\"messageIdentification\":\"\",\r\n" + 
		 		"	 \"creationDateTime\":\"\",\r\n" + 
		 		"	 \"httpCode\":200},\r\n" + 
		 		" \"response\":\r\n" + 
		 		"	{\"accounts\":\r\n" + 
		 		"		[{\"_id\":\"ACCT1234-EUR\",\r\n" + 
		 		"		  \"country\":\"FI\",\r\n" + 
		 		"		  \"accountNumber\":{\"value\":\"ACCT1234\",\"_type\":\"IBAN\"},\r\n" + 
		 		"		  \"currency\":\"EUR\",\r\n" + 
		 		"		  \"ownerName\":\"owner\",\r\n" + 
		 		"		  \"product\":\"prodXXX\",\r\n" + 
		 		"		  \"accountType\":\"Current\",\r\n" + 
		 		"		  \"availableBalance\":\"5555.66\",\r\n" + 
		 		"		  \"bookedBalance\":\"5555.66\",\r\n" + 
		 		"		  \"valueDatedBalance\":\"5555.66\",\r\n" + 
		 		"		  \"_links\":[]\r\n" + 
		 		"		 },\r\n" + 
		 		"		  {\"_id\":\"ACCT5678-EUR\",\r\n" + 
		 		"		   \"country\":\"FI\",\r\n" + 
		 		"		   \"accountNumber\":{\"value\":\"ACCT5678\",\"_type\":\"IBAN\"},\r\n" + 
		 		"		   \"currency\":\"EUR\",\r\n" + 
		 		"		   \"ownerName\":\"owner\",\r\n" + 
		 		"		   \"product\":\"productXXX\",\r\n" + 
		 		"		   \"accountType\":\"Current\",\r\n" + 
		 		"		   \"_links\":[]\r\n" + 
		 		"		   }\r\n" + 
		 		"		  ]\r\n" + 
		 		"	}\r\n" + 
		 		"}";
		 
		 singleAccountResponse = "{\"groupHeader\":{\r\n" + 
		 		"	\"messageIdentification\":\"\",\"creationDateTime\":\"\",\"httpCode\":200},\r\n" + 
		 		"\"response\":{\r\n" + 
		 		"	\"_id\":\"ACCT1234-EUR\",\r\n" + 
		 		"	\"country\":\"FI\",\r\n" + 
		 		"	\"accountNumber\":{\"value\":\"ACCT1234\",\"_type\":\"IBAN\"},\r\n" + 
		 		"	\"currency\":\"EUR\",\r\n" + 
		 		"	\"ownerName\":\"owner\",\r\n" + 
		 		"	\"product\":\"productXXX\",\r\n" + 
		 		"	\"accountType\":\"Current\",\r\n" + 
		 		"	\"availableBalance\":\"5555.66\",\r\n" + 
		 		"	\"bookedBalance\":\"5555.66\",\r\n" + 
		 		"	\"valueDatedBalance\":\"5555.66\",\r\n" + 
		 		"	\"bank\":{\"name\":\"Nordea\",\"bic\":\"NDEAFIHH\",\"country\":\"FI\"},\r\n" + 
		 		"	\"status\":\"OPEN\",\r\n" + 
		 		"	\"creditLimit\":\"100\",\r\n" + 
		 		"	\"latestTransactionBookingDate\":\"2018-04-13\",\r\n" + 
		 		"	\"_links\":[]}}";
		 
		 txnsFirstPageResponse = "{\"groupHeader\":{\r\n" + 
		 		"	\"messageIdentification\":\"\",\"creationDateTime\":\"\",\"httpCode\":200\r\n" + 
		 		"	},\r\n" + 
		 		" \"response\":{\r\n" + 
		 		"	\"_continuationKey\":\"0000-1\",\r\n" + 
		 		"	\"transactions\":[\r\n" + 
		 		"		{\"_type\":\"CreditTransaction\",\"transactionId\":\"0220161120793980\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-13\",\"valueDate\":\"2018-04-13\",\"typeDescription\":\"Pano\",\"message\":\"Removal snow from the street\",\"amount\":\"8.24\",\"debtorName\":\"Firma OY\"},\r\n" + 
		 		"		{\"_type\":\"DebitTransaction\",\"transactionId\":\"0220161120793794\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-13\",\"valueDate\":\"2018-04-13\",\"typeDescription\":\"Itsepalvelu\",\"message\":\"Cash for the order for a potato in a suit\",\"amount\":\"-0.20\",\"creditorName\":\"Ville Virtanen\"}\r\n" + 
		 		"		],\r\n" + 
		 		"	\"_links\":[]}}";
		 
		 txnsLastPageResponse = "{\"groupHeader\":{\r\n" + 
		 		"	\"messageIdentification\":\"\",\"creationDateTime\":\"\",\"httpCode\":200\r\n" + 
		 		"	},\r\n" + 
		 		" \"response\":{\r\n" + 
		 		"	\"transactions\":[\r\n" + 
		 		"		{\"_type\":\"CreditTransaction\",\"transactionId\":\"0220161120793670\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-13\",\"valueDate\":\"2018-04-13\",\"typeDescription\":\"Pano\",\"message\":\"That time of the month … Rent\",\"amount\":\"49.77\",\"debtorName\":\"Matti Meikäläinen\"},\r\n" + 
		 		"		{\"_type\":\"DebitTransaction\",\"transactionId\":\"0220161120793577\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-13\",\"valueDate\":\"2018-04-13\",\"typeDescription\":\"Itsepalvelu\",\"message\":\"Cash for the order for a potato in a suit\",\"amount\":\"-0.16\",\"creditorName\":\"Ville Virtanen\"}\r\n" + 
		 		"		],\r\n" + 
		 		"	\"_links\":[]}}";

	}
	
	
	
	
	@Test
	public void testGetAllAccounts() {
		mockServer.expect(requestTo(accountsUri))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(accountListResponse, MediaType.APPLICATION_JSON));
		
		Account[] accounts = accountDao.getAllAccounts("12345");
		
		mockServer.verify();
		
		assertEquals(2, accounts.length);
		assertEquals(BigDecimal.valueOf(5555.66), accounts[0].getAvailableBalance());
		assertEquals("ACCT5678", accounts[1].getAccountNumber().getValue());
		assertNull(accounts[1].getAvailableBalance());

	}

	@Test
	public void testGetAccountById() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(singleAccountResponse, MediaType.APPLICATION_JSON));
		
		Account account = accountDao.getAccountById("12345", "ACCT1234-EUR");
		
		mockServer.verify();
		
		assertEquals("ACCT1234", account.getAccountNumber().getValue());
		assertEquals(BigDecimal.valueOf(5555.66), account.getAvailableBalance());
		assertEquals("Nordea", account.getBank().getName());
		
	}

	@Test
	public void testGetTransactionPageByAccountId() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?continuationKey=0000-1"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		TransactionPage txnPage = accountDao.getTransactionPageByAccountId("12345", "ACCT1234-EUR", "0000-1");
		
		mockServer.verify();
		
		assertEquals("0000-0", txnPage.getPreviousKey());
		assertTrue(txnPage.getNextKey().isEmpty());
		assertEquals(2, txnPage.getTransactions().size());
		
		
	}

	@Test
	public void testGetTransactionPageByAccountIdAndDate() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-01&toDate=2018-04-13"))
		.andExpect(queryParam("fromDate", "2018-04-01"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		TransactionPage txnPage = accountDao.getTransactionPageByAccountIdAndDate("12345", "ACCT1234-EUR", "2018-04-01", "2018-04-13", "");
		
		mockServer.verify();
		
		assertTrue(txnPage.getPreviousKey().isEmpty());
		assertEquals("0000-1", txnPage.getNextKey());
		assertEquals(2, txnPage.getTransactions().size());
		assertEquals("2018-04-13", txnPage.getTransactions().get(0).getBookingDate());
		
	}

	@Test
	public void testGetTransactionsByAccountId() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?continuationKey=0000-1"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		List<Transaction> txns = accountDao.getTransactionsByAccountId("12345", "ACCT1234-EUR");
		
		mockServer.verify();
		
		assertEquals(4, txns.size());
		
		
	}

	@Test
	public void testGetTransactionsByAccountIdAndDate() {

		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-01&toDate=2018-04-13"))
		.andExpect(queryParam("fromDate", "2018-04-01"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-01&toDate=2018-04-13&continuationKey=0000-1"))
		.andExpect(queryParam("fromDate", "2018-04-01"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		List<Transaction> txns = accountDao.getTransactionsByAccountIdAndDate("12345", "ACCT1234-EUR", "2018-04-01", "2018-04-13");
		
		mockServer.verify();
		
		assertEquals(4, txns.size());
		
	}

}
