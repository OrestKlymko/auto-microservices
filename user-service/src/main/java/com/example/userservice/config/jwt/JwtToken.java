package com.example.userservice.config.jwt;

import com.example.userservice.dto.UserLogin;
import com.example.userservice.model.UserModel;
import com.example.userservice.redis.RedisRepository;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
@AllArgsConstructor
public class JwtToken {
	private final UserRepository userRepository;
	private final RedisRepository redisRepository;

	public String generateToken(UserLogin userLogin) throws JsonProcessingException {
		HashMap<String, Object> claims = new HashMap<>();
		UserModel userModel = userRepository.findUserModelByEmail(userLogin.getEmail()).get();
		JwtUserTransfer jwtUserTransfer = JwtUserTransfer.convertToJwtUserTransfer(userModel, userLogin);
		claims.put("User", jwtUserTransfer);

		PrivateKey privateKey = KeySecret.getInstance().getKeyPair().getPrivate();
		PublicKey publicKey = KeySecret.getInstance().getKeyPair().getPublic();
		redisRepository.savePublicKey( publicKey);
		return Jwts.builder()
				.claims(claims)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.signWith(privateKey)
				.compact();
	}

	public JwtUserTransfer getUserFromToken(String token) {
		Claims claims = getClaims(token);
		LinkedHashMap<String, Object> userLoginDataMap = (LinkedHashMap<String, Object>) claims.get("User");
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(userLoginDataMap, JwtUserTransfer.class);
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
