package ie.cit.comp8058.bankdemo.dao;

import java.util.List;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;

public interface AccountDao {

	public Account[] getAllAccounts(String accessToken);
	
	public Account getAccountById(String accessToken, String id);
	
	public List<Transaction> getTransactionsByAccountId(String accessToken, String id);
	
	public List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate);
	
}
