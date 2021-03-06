package com.starkinc.wtopic.security;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.starkinc.wtopic.constants.Constants;
import com.starkinc.wtopic.entity.TopicUser;
import com.starkinc.wtopic.restClient.LoginClient;
import com.starkinc.wtopic.util.TopicWebUtils;

/**
 * @author RajaSushanth
 *
 */
@Service
public class LoginDetailsService implements UserDetailsService {
	
	private LoginClient loginClient;

	@Override
	public UserDetails loadUserByUsername(String usernameWithPassord) throws UsernameNotFoundException {
		String[] splitted = usernameWithPassord.split("~");
		String username = splitted[0], password = splitted[1];
		ResponseEntity<TopicUser[]> userEntity = loginClient.attemptLogin(username, password);
		HttpStatus statusCode = null;
		HttpHeaders headers = null;
		if(null != userEntity){
			statusCode =  userEntity.getStatusCode();
			headers =  userEntity.getHeaders();
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(Constants.ROLE_USER));
		if(statusCode == OK){
			List<String> authorizationList = headers.get("Authorization");
			String token = null != authorizationList ? authorizationList.get(0) : null;
			//System.out.println(token);
			TopicWebUtils.bindUserSession(username, token);
			return new User(username, Constants.PASSWORD_PLACEHOLDER, true, true, true, true, authorities);
		}else if(statusCode == UNAUTHORIZED){
			HttpHeaders httpHeaders = userEntity.getHeaders();
			String error = (null == httpHeaders)? null: httpHeaders.getFirst("loginErrorMessage");
			if(null != error && error.contains("disabled")){
				return new User(username, Constants.PASSWORD_PLACEHOLDER, false, true, true, true, authorities);
			}
		}
	}

		throw new UsernameNotFoundException("User "+ username + " not found");
	}
	
	@Autowired
	public void setLoginClient(LoginClient loginClient) {
		this.loginClient = loginClient;
	}

}
