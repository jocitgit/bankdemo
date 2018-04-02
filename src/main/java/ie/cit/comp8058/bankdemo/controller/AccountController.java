package ie.cit.comp8058.bankdemo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.DateRange;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.exception.ItemNotFoundException;
import ie.cit.comp8058.bankdemo.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AccountService accountService;
	
	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
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
	public String getTransactions(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, Model model) {
		
		Transaction[] txns = accountService.getTransactionsByAccountId(accessToken, id);
		
		if (txns != null) {
			model.addAttribute("txns", txns);
			model.addAttribute("accountId", id);
			model.addAttribute("dateRange", new DateRange());
			return "txnList";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
	
	@PostMapping("/accounts/{id}/transactions")
	public String postTransactions(@CookieValue(value="bank_token", required=false) String accessToken, @PathVariable("id") String id, @Valid @ModelAttribute DateRange dateRange, BindingResult bindingResult, Model model) {
		
		System.out.println(dateRange);
		
		if (bindingResult.hasErrors() || dateRange == null) {
			//System.out.println("binding error");
            return "txnList";
        }
		
		//TODO - check for NULL dates
		
		//String fromDate = "2018-03-31";
		//String toDate = "2018-04-05";
		String fromDate = DATE_FORMAT.format(dateRange.getFromDate());
		String toDate = DATE_FORMAT.format(dateRange.getToDate());
		
		System.out.println("From: " + fromDate);
		System.out.println("To: " + toDate);
		
		Transaction[] txns = accountService.getTransactionsByAccountIdAndDate(accessToken, id, fromDate, toDate);
		
		if (txns != null) {
			model.addAttribute("txns", txns);
			model.addAttribute("accountId", id);
			return "txnList";
		} else {
			throw new ItemNotFoundException();
		}
		
	}
}
