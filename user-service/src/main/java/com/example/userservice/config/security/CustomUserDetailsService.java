package com.example.userservice.config.security;

import com.example.userservice.model.UserModel;
import com.example.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserModel userModel = userRepository.findUserModelByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s not found", email)));

		return User.builder()
				.username(userModel.getUsername())
				.password(userModel.getPassword())
				.disabled(!userModel.isEnabled())
				.roles(userModel.getRole().toString())
				.build();
	}
}
