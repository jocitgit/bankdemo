package ie.cit.comp8058.bankdemo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.BalanceChartData;
import ie.cit.comp8058.bankdemo.entity.CreditDebitChartData;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;
import ie.cit.comp8058.bankdemo.entity.TransactionTotal;
import ie.cit.comp8058.bankdemo.exception.ItemNotFoundException;
import ie.cit.comp8058.bankdemo.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@RequestMapping("/accounts")
	public String getAccounts(@CookieValue(value="bank_token", required=false) String accessToken, Model model) {
		
		Account[] accounts = accountService.getAllAccounts(accessToken);
		
		if (accounts != null) {
			model.addAttribute("accounts", accounts);
			return "accountList";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
	
	@RequestMapping("/accounts/{id}")
	public String getAccounts(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, Model model) {
		
		Account account = accountService.getAccountById(accessToken, id);
		
		if (account != null) {
			model.addAttribute("account", account);
			return "accountDashboard";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
	
	@GetMapping("/accounts/{id}/transactions")
	public String getTransactions(@CookieValue(value="bank_token", required=false) String accessToken, 
			@PathVariable("id") String id, 
			@RequestParam(value="dateFilter", required=false) String dateFilter, 
			@RequestParam(value="fromDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, 
			@RequestParam(value="toDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, 
			@RequestParam(value="continuationKey", required=false) String continuationKey, 
			Model model) {
		
		TransactionPage txnPage;
		String txnFromDate = "";
		String txnToDate = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		// If the user has specified a time period such as 'previous week' set the date range
		if (dateFilter != null) {
			toDate = LocalDate.now();
			switch (dateFilter) {
			case "week":			
				fromDate = toDate.minusWeeks(1);
				break;
			case "month":
				fromDate = toDate.minusMonths(1);
				break;
			case "sixmonth":
				fromDate = toDate.minusMonths(6);
				break;
			default:
				fromDate = toDate;				
			}
		}
		
		// If no dates are set, get the first page of transactions
		if (fromDate == null || toDate == null) {
			txnPage = accountService.getTransactionPageByAccountId(accessToken, id, continuationKey);
		// Otherwise get the first page of transactions within the date range
		} else {
			txnFromDate = fromDate.format(formatter);
			txnToDate = toDate.format(formatter);
			txnPage = accountService.getTransactionPageByAccountIdAndDate(accessToken, id, txnFromDate, txnToDate, continuationKey);
		}
		
		// Add the transactions to the view, along with the links to the next and previous pages
		if (txnPage != null) {
			model.addAttribute("txns", txnPage.getTransactions());	
			model.addAttribute("currentKey", continuationKey);
			model.addAttribute("previousKey", txnPage.getPreviousKey());
			model.addAttribute("nextKey", txnPage.getNextKey());
			model.addAttribute("accountId", id);
			model.addAttribute("fromDate", txnFromDate);
			model.addAttribute("toDate", txnToDate);
			return "txnList";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
	

	@GetMapping("/accounts/{id}/totals")
	public String getTotals(@CookieValue(value="bank_token", required=false) String accessToken, 
			@PathVariable("id") String id, 
			@RequestParam(value="groupBy", required=false) String groupBy, 
			@RequestParam(value="fromDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, 
			@RequestParam(value="toDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, 
			Model model) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String txnFromDate = "";
		String txnToDate = "";
		
		if (fromDate == null || toDate == null) {
			//Default to previous week
			toDate = LocalDate.now();
			fromDate = toDate.minusWeeks(1);
		}
		
		txnFromDate = fromDate.format(formatter);
		txnToDate = toDate.format(formatter);
		 
		
		if (groupBy == null || groupBy.isEmpty()) {
			groupBy = "day"; //default
		}

		List<TransactionTotal> totals = accountService.getTransactionTotals(accessToken, id, txnFromDate, txnToDate, groupBy);

		if (totals!=null) {
			model.addAttribute("accountId", id);
			model.addAttribute("fromDate", txnFromDate);
			model.addAttribute("toDate", txnToDate);
			model.addAttribute("groupBy", groupBy);
			model.addAttribute("totals", totals);
			return "txnTotals";
		} else {
			throw new ItemNotFoundException();
		}
	}
	
	@RequestMapping("/accounts/{id}/balanceChart")
	public String getBalanceChart(@CookieValue(value="bank_token", required=false) String accessToken, 
			@PathVariable("id") String id,
			@RequestParam(value="dateFilter", required=false) String dateFilter, 
			Model model) {

		LocalDate fromDate;
		LocalDate toDate;
		String txnFromDate = "";
		String txnToDate = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		if (dateFilter==null) {
			dateFilter = "week"; //default
		}


		toDate = LocalDate.now();

		switch (dateFilter) {
		case "week":			
			fromDate = toDate.minusWeeks(1);
			break;
		case "month":
			fromDate = toDate.minusMonths(1);
			break;
		case "sixmonth":
			fromDate = toDate.minusMonths(6);
			break;
		default:
			fromDate = toDate.minusWeeks(1); //default			
		}


		txnFromDate = fromDate.format(formatter);
		txnToDate = toDate.format(formatter);

		BalanceChartData data = accountService.getBalanceChartData(accessToken, id, txnFromDate, txnToDate);

		if (data==null) {
			throw new ItemNotFoundException();
		}

		model.addAttribute("accountId", id);
		model.addAttribute("chartXValues", data.getXValues());
		model.addAttribute("chartYValues", data.getYValues());
		model.addAttribute("currency", data.getCurrency());

		return "balanceChart";

	}
	
	@GetMapping("/accounts/{id}/creditDebitChart")
	public String getCreditDebitChart(@CookieValue(value="bank_token", required=false) String accessToken, 
			@PathVariable("id") String id, 
			@RequestParam(value="groupBy", required=false) String groupBy, 
			@RequestParam(value="fromDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate, 
			@RequestParam(value="toDate", required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate, 
			Model model) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String txnFromDate = "";
		String txnToDate = "";
		
		if (fromDate == null || toDate == null) {
			//Default to previous week
			toDate = LocalDate.now();
			fromDate = toDate.minusWeeks(1);
		}
		
		txnFromDate = fromDate.format(formatter);
		txnToDate = toDate.format(formatter);
		 
		
		if (groupBy == null || groupBy.isEmpty()) {
			groupBy = "day"; //default
		}
		
		CreditDebitChartData data = accountService.getCreditDebitChartData(accessToken, id, txnFromDate, txnToDate, groupBy);
		
		if (data!=null) {
			model.addAttribute("accountId", id);
			model.addAttribute("fromDate", txnFromDate);
			model.addAttribute("toDate", txnToDate);
			model.addAttribute("groupBy", groupBy);
			model.addAttribute("chartXValues", data.getXValues());
			model.addAttribute("chartCreditValues", data.getCreditValues());
			model.addAttribute("chartDebitValues", data.getDebitValues());
			model.addAttribute("currency", data.getCurrency());
			return "creditDebitChart";
		} else {
			throw new ItemNotFoundException();
		}
	}
}
