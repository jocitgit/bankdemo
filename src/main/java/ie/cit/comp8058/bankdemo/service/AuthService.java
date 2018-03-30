package ie.cit.comp8058.bankdemo.service;

import ie.cit.comp8058.bankdemo.entity.Token;

public interface AuthService {

	public Token getAccessToken(String code, String state);
	
	public String getLoginUri(boolean withUI);
}
