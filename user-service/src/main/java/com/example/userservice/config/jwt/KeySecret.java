package com.example.userservice.config.jwt;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
public class KeySecret {

	private static volatile KeySecret instance;
	private KeyPair keyPair;

	private KeySecret() {
		if (instance != null) {
			throw new RuntimeException("Use getInstance() method to create a singleton");
		}
		generateKeyPair();
	}

	public static KeySecret getInstance() {
		if (instance == null) {
			synchronized (KeySecret.class) {
				if (instance == null) {
					instance = new KeySecret();
				}
			}
		}
		return instance;
	}

	private void generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}
}