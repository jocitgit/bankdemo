package ie.cit.comp8058.bankdemo.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import ie.cit.comp8058.bankdemo.dao.AccountDao;
import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired AccountDao accountDao;

	@Override
	public Account[] getAllAccounts(String accessToken) {
		return accountDao.getAllAccounts(accessToken);
	}

	@Override
	public Account getAccountById(String accessToken, String id) {
		return accountDao.getAccountById(accessToken, id);
	}

	@Override
	public Transaction[] getTransactionsByAccountId(String accessToken, String id) {
		return accountDao.getTransactionsByAccountId(accessToken, id);
	}
	
	
	
}
