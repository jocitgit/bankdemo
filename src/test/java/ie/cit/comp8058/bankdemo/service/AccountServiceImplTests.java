package ie.cit.comp8058.bankdemo.service;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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

import ie.cit.comp8058.bankdemo.entity.BalanceChartData;
import ie.cit.comp8058.bankdemo.entity.CreditDebitChartData;
import ie.cit.comp8058.bankdemo.entity.TransactionTotal;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceImplTests {
	
	@Autowired
	AccountService accountService;
	
	@Autowired 
	RestTemplate restTemplate;
	
	@Value( "${CLIENT_ID}" )
	private String clientId;
	
	@Value( "${CLIENT_SECRET}" )
	private String clientSecret;
	
	@Value( "${BASE_ACCOUNTS_URI}" )
	private String accountsUri;
	
	private MockRestServiceServer mockServer;
	
	private String txnsFirstPageResponse;
	private String txnsLastPageResponse;
	private String singleAccountResponse;
	
	@Before
	public void setup() {

		mockServer = MockRestServiceServer.createServer(restTemplate);


		txnsFirstPageResponse = "{\"groupHeader\":{\r\n" + 
				"	\"messageIdentification\":\"\",\"creationDateTime\":\"\",\"httpCode\":200\r\n" + 
				"	},\r\n" + 
				" \"response\":{\r\n" + 
				"	\"_continuationKey\":\"0000-1\",\r\n" + 
				"	\"transactions\":[\r\n" + 
				"		{\"_type\":\"CreditTransaction\",\"transactionId\":\"0220161120793980\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-13\",\"valueDate\":\"2018-04-13\",\"typeDescription\":\"Pano\",\"message\":\"Removal snow from the street\",\"amount\":\"8.24\",\"debtorName\":\"Firma OY\"},\r\n" + 
				"		{\"_type\":\"DebitTransaction\",\"transactionId\":\"0220161120793794\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-12\",\"valueDate\":\"2018-04-12\",\"typeDescription\":\"Itsepalvelu\",\"message\":\"Cash for the order for a potato in a suit\",\"amount\":\"-0.20\",\"creditorName\":\"Ville Virtanen\"}\r\n" + 
				"		],\r\n" + 
				"	\"_links\":[]}}";

		txnsLastPageResponse = "{\"groupHeader\":{\r\n" + 
				"	\"messageIdentification\":\"\",\"creationDateTime\":\"\",\"httpCode\":200\r\n" + 
				"	},\r\n" + 
				" \"response\":{\r\n" + 
				"	\"transactions\":[\r\n" + 
				"		{\"_type\":\"CreditTransaction\",\"transactionId\":\"0220161120793670\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-12\",\"valueDate\":\"2018-04-12\",\"typeDescription\":\"Pano\",\"message\":\"That time of the month … Rent\",\"amount\":\"49.77\",\"debtorName\":\"Matti Meikäläinen\"},\r\n" + 
				"		{\"_type\":\"DebitTransaction\",\"transactionId\":\"0220161120793577\",\"currency\":\"EUR\",\"bookingDate\":\"2018-04-11\",\"valueDate\":\"2018-04-11\",\"typeDescription\":\"Itsepalvelu\",\"message\":\"Cash for the order for a potato in a suit\",\"amount\":\"-0.16\",\"creditorName\":\"Ville Virtanen\"}\r\n" + 
				"		],\r\n" + 
				"	\"_links\":[]}}";
		
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

	}
	
	
	@Test
	public void testGetTransactionTotals() {

		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13&continuationKey=0000-1"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		List<TransactionTotal> totals = accountService.getTransactionTotals("12345", "ACCT1234-EUR", "2018-04-10", "2018-04-13", "day");
		
		mockServer.verify();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
		assertEquals(4, totals.size());
		
		assertEquals("2018-04-10", totals.get(0).getFromDate().format(formatter));
		assertEquals("2018-04-10", totals.get(0).getToDate().format(formatter));
		assertEquals(BigDecimal.ZERO, totals.get(0).getTotal());
		
		assertEquals("2018-04-11", totals.get(1).getFromDate().format(formatter));
		assertEquals(BigDecimal.valueOf(-0.16), totals.get(1).getTotalDebit());
		
		assertEquals("2018-04-12", totals.get(2).getFromDate().format(formatter));
		assertEquals(new BigDecimal("-0.20"), totals.get(2).getTotalDebit());
		assertEquals(BigDecimal.valueOf(49.77), totals.get(2).getTotalCredit());
		assertEquals(BigDecimal.valueOf(49.57), totals.get(2).getTotal());
		
		assertEquals("2018-04-13", totals.get(3).getFromDate().format(formatter));
		assertEquals("2018-04-13", totals.get(3).getToDate().format(formatter));
		assertEquals(BigDecimal.valueOf(8.24), totals.get(3).getTotal());
		
		
	}

	@Test
	public void testGetBalanceChartData() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13&continuationKey=0000-1"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(singleAccountResponse, MediaType.APPLICATION_JSON));
		
		BalanceChartData data = accountService.getBalanceChartData("12345", "ACCT1234-EUR", "2018-04-10", "2018-04-13");
		
		mockServer.verify();
		
		assertEquals("EUR", data.getCurrency());
		assertEquals(4, data.getXValues().size());
		assertEquals(4, data.getYValues().size());
		
		assertEquals("13-Apr-18", data.getXValues().get(3));
		assertEquals("12-Apr-18", data.getXValues().get(2));
		assertEquals("11-Apr-18", data.getXValues().get(1));
		assertEquals("10-Apr-18", data.getXValues().get(0));
		
		assertEquals(BigDecimal.valueOf(5555.66), data.getYValues().get(3));
		assertEquals(BigDecimal.valueOf(5547.42), data.getYValues().get(2));
		assertEquals(BigDecimal.valueOf(5497.85), data.getYValues().get(1));
		assertEquals(BigDecimal.valueOf(5498.01), data.getYValues().get(0));
		
	}

	@Test
	public void testGetCreditDebitChartData() {
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(singleAccountResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsFirstPageResponse, MediaType.APPLICATION_JSON));
		
		mockServer.expect(requestTo(accountsUri + "/ACCT1234-EUR/transactions?fromDate=2018-04-10&toDate=2018-04-13&continuationKey=0000-1"))
		.andExpect(queryParam("fromDate", "2018-04-10"))
		.andExpect(queryParam("toDate", "2018-04-13"))
		.andExpect(queryParam("continuationKey", "0000-1"))
		.andExpect(method(HttpMethod.GET))
		.andExpect(header("X-IBM-Client-Id", clientId))
		.andExpect(header("X-IBM-Client-Secret", clientSecret))
		.andExpect(header("Authorization", "Bearer 12345"))
        .andRespond(withSuccess(txnsLastPageResponse, MediaType.APPLICATION_JSON));
		
		CreditDebitChartData data = accountService.getCreditDebitChartData("12345", "ACCT1234-EUR", "2018-04-10", "2018-04-13", "week");
		
		mockServer.verify();
		
		assertEquals("EUR", data.getCurrency());
		assertEquals(1, data.getXValues().size());
		assertEquals(1, data.getCreditValues().size());
		assertEquals(1, data.getDebitValues().size());
		assertEquals("10-Apr-18 to 13-Apr-18", data.getXValues().get(0));
		assertEquals(BigDecimal.valueOf(58.01), data.getCreditValues().get(0));
		assertEquals(BigDecimal.valueOf(-0.36), data.getDebitValues().get(0));
		
	}

}
