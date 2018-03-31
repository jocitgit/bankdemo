package ie.cit.comp8058.bankdemo.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;

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
	public Transaction[] getTransactionsByAccountId(String accessToken, String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
