package com.example.userservice.config.jwt;

import com.example.userservice.dto.UserLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Deserializer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
@AllArgsConstructor
public class JwtToken {

	public String generateToken(UserLogin userLogin) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("UserLoginObject", userLogin);
		return Jwts.builder()
				.claims(claims)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 60*60 * 1000))
				.signWith(KeySecret.getInstance().getKeyPair().getPrivate())
				.compact();
	}

	public UserLogin getUserFromToken(String token) {
		Claims claims = getClaims(token);
			LinkedHashMap<String, Object> userLoginDataMap = (LinkedHashMap<String, Object>) claims.get("UserLoginObject");
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(userLoginDataMap, UserLogin.class);
	}

	private static Claims getClaims(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(KeySecret.getInstance().getKeyPair().getPublic())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims;
	}
}
