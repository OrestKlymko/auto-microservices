package com.example.userservice.controller;

import com.example.userservice.dto.response.ResponseExceptionModel;
import com.example.userservice.dto.response.ResponseModel;
import com.example.userservice.dto.UserLogin;
import com.example.userservice.dto.UserRegistration;
import com.example.userservice.model.UserModel;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/")
public class UserController {

	private final UserService userService;


	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLogin userLogin, HttpServletResponse response) {
		try {
			Cookie successLoginCookie = userService.login(userLogin);
			response.addCookie(successLoginCookie);
			return ResponseEntity.ok(new ResponseModel(HttpStatus.ACCEPTED, "Login success"));
		} catch (ResponseExceptionModel e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/registration")
	public ResponseEntity<?> login(@RequestBody UserRegistration userRegistration) {
		try {
			userService.registration(userRegistration);
			return ResponseEntity.ok(new ResponseModel(HttpStatus.CREATED, "Success registration"));
		} catch (ResponseExceptionModel e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		userService.logout(response);
		return ResponseEntity.ok(new ResponseModel(HttpStatus.ACCEPTED, "Logout success"));
	}

	@GetMapping("/get-user")
	public ResponseEntity<?> getAuthUser() {
		try {
			return ResponseEntity.ok(userService.getAuthUserModel());
		} catch (ResponseExceptionModel e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
