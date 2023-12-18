package com.example.userservice.util;


import com.example.userservice.dto.response.ResponseExceptionModel;
import org.springframework.http.HttpStatus;

public class ValidChecker {
	public static boolean isPasswordValid(String password) throws ResponseExceptionModel {
		String correctPasswordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,12}$";
		boolean passwordMatches = password.matches(correctPasswordPattern);
		if (!passwordMatches) {
			throw new ResponseExceptionModel(HttpStatus.BAD_REQUEST, "Use a valid password");
		}
		return true;
	}

	public static boolean isEmailValid(String email) throws ResponseExceptionModel {
		String correctEmailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		boolean emailValid = email.matches(correctEmailPattern);
		if (!emailValid) {
			throw new ResponseExceptionModel(HttpStatus.BAD_REQUEST, "Use a valid email");
		}
		return true;
	}
}