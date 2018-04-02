package ie.cit.comp8058.bankdemo.dao;

import java.util.List;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;

public interface AccountDao {

	Account[] getAllAccounts(String accessToken);
	
	Account getAccountById(String accessToken, String id);
	
	List<Transaction> getTransactionsByAccountId(String accessToken, String id);
	
	List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate);

	TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey);

	TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate,
			String continuationKey);
	
}
