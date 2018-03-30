package ie.cit.comp8058.bankdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@RequestMapping("/accounts")
	public String getAccounts(@CookieValue(value="bank_token", required=false) String accessToken, Model model) {
		
		
		if (accessToken == null) {
			// TODO - handle error if no cookie is null?
			return "home";
		}
		
		
		Account[] accounts = accountService.getAllAccounts(accessToken);
		
		if (accounts != null) {
			model.addAttribute("accounts", accounts);
			return "accountList";
		} else {
			//TODO - error handling
			return "home";
		}
		
	}
	
	@RequestMapping("/accounts/{id}")
	public String getAccounts(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, Model model) {
		
		
		if (accessToken == null) {
			// TODO - handle error if no cookie is null?
			return "home";
		}
		
		
		Account account = accountService.getAccountById(accessToken, id);
		
		if (account != null) {
			model.addAttribute("account", account);
			return "accountDetail";
		} else {
			//TODO - error handling
			return "home";
		}
		
	}
	
	@RequestMapping("/accounts/{id}/transactions")
	public String getTransactions(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, Model model) {
		
		
		if (accessToken == null) {
			// TODO - handle error if no cookie is null?
			return "home";
		}
		
		
		Transaction[] txns = accountService.getTransactionsByAccountId(accessToken, id);
		
		if (txns != null) {
			model.addAttribute("txns", txns);
			return "txnList";
		} else {
			//TODO - error handling
			return "home";
		}
		
	}
}
