package com.example.userservice.config.jwt;


import com.example.userservice.config.security.Authenticator;
import com.example.userservice.dto.UserLogin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtToken jwtToken;
	private final Authenticator authenticator;

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String jwtTokenFromCookies = getJwtTokenFromCookies(request.getCookies());
		if (jwtTokenFromCookies != null) {
			UserLogin userFromToken = jwtToken.getUserFromToken(jwtTokenFromCookies);
			authenticator.authenticateUser(userFromToken);
		}
		filterChain.doFilter(request, response);
	}

	public String getJwtTokenFromCookies(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jwt")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
