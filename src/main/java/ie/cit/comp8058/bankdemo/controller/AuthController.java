package ie.cit.comp8058.bankdemo.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ie.cit.comp8058.bankdemo.entity.Token;
import ie.cit.comp8058.bankdemo.exception.AuthRequestException;
import ie.cit.comp8058.bankdemo.service.AuthService;

@Controller
public class AuthController {

	@Autowired 
	AuthService authService;
	
	@RequestMapping("/login")
	public String login(@RequestParam("withUI") boolean withUI) {
		
		String uri = authService.getLoginUri(withUI);
		
		return "redirect:" + uri;
	}
	
	@RequestMapping("/logout")
    public String logout(HttpServletResponse response) {
		
		Cookie bankCookie = new Cookie("bank_token", "");
		bankCookie.setMaxAge(0);
		response.addCookie(bankCookie);
		
        return "home";
	}
	
	@RequestMapping("/auth")
    public String test(@RequestParam(value="code", required=false) String code, @RequestParam(value="state", required=false) String state, HttpServletResponse response) {
        
        		
		Token token = authService.getAccessToken(code, state);
        
		if (token == null) {
			throw new AuthRequestException();
		}
		// Store token in cookie and redirect
		Cookie bankCookie = new Cookie("bank_token", token.getAccess_token());
		bankCookie.setHttpOnly(true);
		bankCookie.setMaxAge(Integer.parseInt(token.getExpires_in()));
		response.addCookie(bankCookie);
		// Do redirect not return here to update url
		return "redirect:/accounts";
		
	}
}
