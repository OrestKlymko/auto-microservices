package com.example.userservice.config.security;

import com.example.userservice.config.jwt.JwtUserTransfer;
import com.example.userservice.dto.response.ResponseExceptionModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Authenticator {
	private final AuthenticationConfiguration authenticationConfiguration;

	private AuthenticationManager getAuthManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	public void authenticateUser(JwtUserTransfer jwtUserTransfer) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(jwtUserTransfer.getEmail(), jwtUserTransfer.getPassword());
		Authentication authenticate = getAuthManager().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
	}

	public String getUsernameOfUserAuthentication() throws ResponseExceptionModel {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean userAuthenticated = authentication.isAuthenticated();

		if (userAuthenticated) {
			User authenticationPrincipal = (User) authentication.getPrincipal();
			return authenticationPrincipal.getUsername();
		}
		throw new ResponseExceptionModel(HttpStatus.FORBIDDEN, "User not authenticate");
	}

}
