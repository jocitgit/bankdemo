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
import ie.cit.comp8058.bankdemo.entity.TransactionPage;
import ie.cit.comp8058.bankdemo.entity.TransactionTotal;
import ie.cit.comp8058.bankdemo.exception.ItemNotFoundException;
import ie.cit.comp8058.bankdemo.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	/*
	// Binds <input type=date> format 'yyyy'MM-dd to Date request parameter
	@InitBinder
    public void initBinder(WebDataBinder binder) {
       binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE_FORMAT, true));
    }
   */
	
	
	
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
		
		
		if (fromDate == null || toDate == null) {
			txnPage = accountService.getTransactionPageByAccountId(accessToken, id, continuationKey);
		} else {
			//txnFromDate = DATE_FORMAT.format(fromDate);
			//txnToDate = DATE_FORMAT.format(toDate);
			txnFromDate = fromDate.format(formatter);
			txnToDate = toDate.format(formatter);
			txnPage = accountService.getTransactionPageByAccountIdAndDate(accessToken, id, txnFromDate, txnToDate, continuationKey);
		}
		
		
		//System.out.println("PrevKey: " + previousKey);
		
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
	
	@RequestMapping("/accounts/{id}/charts")
	public String getCharts(@CookieValue(value="bank_token", required=false) String accessToken, Model model) {
		//TODO add charts
		return "chart";
		
	}
}
