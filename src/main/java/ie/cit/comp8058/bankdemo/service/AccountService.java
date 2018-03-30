package ie.cit.comp8058.bankdemo.service;

import ie.cit.comp8058.bankdemo.entity.Account;

public interface AccountService {

	public Account[] getAllAccounts(String accessToken);
	
	public Account getAccountById(String accessToken, String id);
	
}
