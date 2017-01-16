package com.starkinc.wtopic.contoller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.starkinc.wtopic.entity.User;
import com.starkinc.wtopic.service.LoginService;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginContoller {
	
	private LoginService loginService;
	
	@GetMapping("/")
	public String index(){
		return "index";
	}
	
	@PostMapping("/login")
	public String login(Model model, @ModelAttribute User user){
		boolean isAuthenticated = false;
		String landingPage = "index";
		if(null != user && 
				StringUtils.isNotBlank(user.getName()) && StringUtils.isNotBlank(user.getPassword())){
			isAuthenticated = loginService.validateLogin(user.getName(), user.getPassword());
			if(isAuthenticated){
				landingPage = "home";
			}
		}else{
			landingPage = "invalidRequest";
		}
		return landingPage;
	}

	@Autowired
	public LoginContoller(LoginService loginService) {
		this.loginService = loginService;
	}
	
	
	
}
