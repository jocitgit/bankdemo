package ie.cit.comp8058.bankdemo.service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import ie.cit.comp8058.bankdemo.dao.AccountDao;
import ie.cit.comp8058.bankdemo.entity.Account;
import ie.cit.comp8058.bankdemo.entity.Transaction;
import ie.cit.comp8058.bankdemo.entity.TransactionPage;

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
	public List<Transaction> getTransactionsByAccountId(String accessToken, String id) {
		return accountDao.getTransactionsByAccountId(accessToken, id);
	}

	@Override
	public List<Transaction> getTransactionsByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate) {
		return accountDao.getTransactionsByAccountIdAndDate(accessToken, id, fromDate, toDate);
	}

	@Override
	public TransactionPage getTransactionPageByAccountId(String accessToken, String id, String continuationKey) {
		return accountDao.getTransactionPageByAccountId(accessToken, id, continuationKey);
	}

	@Override
	public TransactionPage getTransactionPageByAccountIdAndDate(String accessToken, String id, String fromDate,
			String toDate, String continuationKey) {
		return accountDao.getTransactionPageByAccountIdAndDate(accessToken, id, fromDate, toDate, continuationKey);
	}
	
	
	
}
