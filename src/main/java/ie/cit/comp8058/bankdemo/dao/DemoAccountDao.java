package ie.cit.comp8058.bankdemo.dao;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;

@Repository
@Profile("demo")
public class DemoAccountDao implements AccountDao {

	@Override
	public Account[] getAllAccounts(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccountById(String accessToken, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByAccountId(String accessToken, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate, String continuationKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
