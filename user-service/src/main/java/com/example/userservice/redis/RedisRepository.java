package com.example.userservice.redis;


import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.security.PublicKey;
import java.util.Base64;

@Repository
@AllArgsConstructor
public class RedisRepository {
	private final RedisTemplate<String, Object> redisTemplate;

	public void savePublicKey(PublicKey key)  {
		String keyBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
		redisTemplate.opsForValue().set("jwt", keyBase64);
	}
}
