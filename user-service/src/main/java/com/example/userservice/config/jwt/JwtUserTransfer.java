package com.example.userservice.config.jwt;

import com.example.userservice.dto.UserLogin;
import com.example.userservice.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtUserTransfer {
	private long userId;
	private String role;
	private String username;
	private String email;
	private String password;


	public static JwtUserTransfer convertToJwtUserTransfer(UserModel userModel, UserLogin userLogin){
		return JwtUserTransfer.builder()
				.userId(userModel.getId())
				.username(userModel.getUsername())
				.role(userModel.getRole().toString())
				.password(userLogin.getPassword())
				.email(userLogin.getEmail())
				.build();
	}
}
