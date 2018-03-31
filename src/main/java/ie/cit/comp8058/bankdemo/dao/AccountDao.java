package ie.cit.comp8058.bankdemo.dao;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;

public interface AccountDao {

	public Account[] getAllAccounts(String accessToken);
	
	public Account getAccountById(String accessToken, String id);
	
	public Transaction[] getTransactionsByAccountId(String accessToken, String id);
	
	public Transaction[] getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate);
	
}
