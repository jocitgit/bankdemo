package ie.cit.comp8058.bankdemo.service;

import java.util.List;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.BalanceChartData;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;
import ie.cit.comp8058.bankdemo.entity.TransactionTotal;

public interface AccountService {

	Account[] getAllAccounts(String accessToken);
	
	Account getAccountById(String accessToken, String id);
	
	List<Transaction> getTransactionsByAccountId(String accessToken, String id);
	
	List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate, String toDate);

	TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey);

	TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate, String continuationKey);
	
	List<TransactionTotal> getTransactionTotals(String accessToken, String id, String fromDate, String toDate, String groupBy);

	BalanceChartData getBalanceChartData(String accessToken, String id, String fromDate, String toDate);
}
