package ie.cit.comp8058.bankdemo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;
import ie.cit.comp8058.bankdemo.exception.ItemNotFoundException;
import ie.cit.comp8058.bankdemo.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	// Binds <input type=date> format 'yyyy'MM-dd to Date request parameter
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE_FORMAT, true));
    }
   
	
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
			return "accountDetail";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
	
	@GetMapping("/accounts/{id}/transactions")
	public String getTransactions(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, @RequestParam(value="fromDate", required=false) Date fromDate, @RequestParam(value="toDate", required=false) Date toDate, @RequestParam(value="continuationKey", required=false) String continuationKey, Model model) {
		
		TransactionPage txnPage;
		String txnFromDate = "";
		String txnToDate = "";
		//String previousKey = "";
		
		if (fromDate == null || toDate == null) {
			txnPage = accountService.getTransactionPageByAccountId(accessToken, id, continuationKey);
		} else {
			txnFromDate = DATE_FORMAT.format(fromDate);
			txnToDate = DATE_FORMAT.format(toDate);
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
	
}
