package ie.cit.comp8058.bankdemo.service;

import ie.cit.comp8058.bankdemo.entity.Token;

public interface AuthService {

	Token getAccessToken(String code, String state);
	
	String getLoginUri(boolean withUI);
}
