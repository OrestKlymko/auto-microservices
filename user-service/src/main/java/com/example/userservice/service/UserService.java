package com.example.userservice.service;


import com.example.userservice.config.security.Authenticator;
import com.example.userservice.config.security.SecurityConfiguration;
import com.example.userservice.config.jwt.JwtToken;
import com.example.userservice.dto.response.ResponseExceptionModel;
import com.example.userservice.dto.UserLogin;
import com.example.userservice.dto.UserRegistration;
import com.example.userservice.model.UserModel;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.ValidChecker;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SecurityConfiguration securityConfiguration;
	private final Authenticator authenticator;
	private final JwtToken jwtToken;

	public Cookie login(UserLogin userLogin) throws ResponseExceptionModel, JsonProcessingException {
		boolean credentialIsOk = validateUserCredentials(userLogin);
		if (credentialIsOk) {
			String token = jwtToken.generateToken(userLogin);
			Cookie cookie = new Cookie("jwt", token);
			cookie.setHttpOnly(true);
			return cookie;
		}
		throw new ResponseExceptionModel(HttpStatus.FORBIDDEN, "Bad credential");
	}

	private boolean validateUserCredentials(UserLogin userLogin) throws ResponseExceptionModel {
		UserModel userModel = getUserModel(userLogin);
		checkCorrectPassword(userLogin, userModel);
		return true;
	}

	private void checkCorrectPassword(UserLogin userLogin, UserModel userModel) throws ResponseExceptionModel {
		boolean matchesPassword = securityConfiguration.passwordEncoder().matches(userLogin.getPassword(), userModel.getPassword());
		if (!matchesPassword) {
			throw new ResponseExceptionModel(HttpStatus.BAD_REQUEST, "Password incorrect");
		}
	}

	private UserModel getUserModel(UserLogin userLogin) throws ResponseExceptionModel {
		return userRepository.findUserModelByEmail(userLogin.getEmail())
				.orElseThrow(() -> new ResponseExceptionModel(HttpStatus.NOT_FOUND, "User not found with this email"));
	}


	public void registration(UserRegistration userRegistration) throws ResponseExceptionModel {
		boolean validRegistrationUser = isValidRegistrationUser(userRegistration);
		if (validRegistrationUser) {
			UserModel userModel = UserModel.builder()
					.username(userRegistration.getUsername())
					.email(userRegistration.getEmail())
					.password(securityConfiguration.passwordEncoder().encode(userRegistration.getPassword()))
					.role(userRegistration.getRole())
					.enabled(userRegistration.isEnabled())
					.build();
			userRepository.save(userModel);
		}
	}

	private boolean isValidRegistrationUser(UserRegistration userRegistration) throws ResponseExceptionModel {
		boolean passwordValid = ValidChecker.isPasswordValid(userRegistration.getPassword());
		boolean emailValid = ValidChecker.isEmailValid(userRegistration.getEmail());
		isUserExistingByEmail(userRegistration);
		isUserExistingByUsername(userRegistration);
		return passwordValid && emailValid;
	}

	private void isUserExistingByEmail(UserRegistration userRegistration) throws ResponseExceptionModel {
		if (userRepository.findUserModelByEmail(userRegistration.getEmail()).isPresent()) {
			throw new ResponseExceptionModel(HttpStatus.CONFLICT,
					String.format("User with email %s already exist", userRegistration.getEmail()));
		}
	}
	private void isUserExistingByUsername(UserRegistration userRegistration) throws ResponseExceptionModel {
		if (userRepository.findUserModelByUsername(userRegistration.getUsername()).isPresent()) {
			throw new ResponseExceptionModel(HttpStatus.CONFLICT,
					String.format("User with username %s already exist", userRegistration.getUsername()));
		}
	}

	public void logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", "");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}


	public UserModel getAuthUserModel() throws ResponseExceptionModel {
		String usernameOfUserAuthentication = authenticator.getUsernameOfUserAuthentication();
		return userRepository.findUserModelByUsername(usernameOfUserAuthentication).get();
	}

}
