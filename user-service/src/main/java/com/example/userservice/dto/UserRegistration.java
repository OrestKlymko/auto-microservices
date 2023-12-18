package com.example.userservice.dto;


import com.example.userservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistration {
	private String username;
	private String email;
	private String password;
	private boolean enabled = true;
	private Role role = Role.USER;
}
