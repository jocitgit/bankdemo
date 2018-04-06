package ie.cit.comp8058.bankdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	
	
	
	@RequestMapping("/")
    public String home() {
        return "home";
	}
	
	
	
	
	
}
