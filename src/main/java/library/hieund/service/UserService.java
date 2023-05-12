package library.hieund.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import library.hieund.dto.UserDTO;
import library.hieund.exception.CustomException;
import library.hieund.model.User;
import library.hieund.repository.UserRepository;
import library.hieund.security.JwtTokenProvider;

import library.hieund.validator.EmailConstraint;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String login(String email, String password) {
	try {
	    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("success", true);
	    jsonObject.put("access_token",
		    jwtTokenProvider.createToken(email, userRepository.findByEmail(email).getAppUserRoles()));

	    return jsonObject.toString();

	} catch (AuthenticationException e) {
	    return returnError("Wrong Credential");
//	    throw new CustomException("Wrong Credential", HttpStatus.UNPROCESSABLE_ENTITY);
	}

    }

    public String register(User appUser) {
	if (!userRepository.existsByEmail(appUser.getEmail())) {
	    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
	    userRepository.save(appUser);
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("success", true);
	    jsonObject.put("access_token", jwtTokenProvider.createToken(appUser.getEmail(), appUser.getAppUserRoles()));

	    return jsonObject.toString();
	} else {
	    return returnError("Email is already in use");
//	    throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
	}
    }

    public void delete(String username) {
	userRepository.deleteByUsername(username);
    }

    public User search(String email) {
	User appUser = userRepository.findByEmail(email);
	if (appUser == null) {
	    throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
	}
	return appUser;
    }

    public User whoAmI(HttpServletRequest req) {
	return userRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String updateUser(HttpServletRequest req, UserDTO userDataDTO, BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return returnError(bindingResult.getAllErrors().get(0).getDefaultMessage());
	}
	User user = userRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
	String email = userDataDTO.getEmail();
	String username = userDataDTO.getUsername();
	String password = userDataDTO.getPassword();
	if (user != null) {
	    if (StringUtils.isBlank(email) && StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
		return returnError("Missing parameter");
	    }

	    if (!StringUtils.isBlank(email)) {
		if (userRepository.existsByEmail(email)) {
		    return returnError("This email already exists in the system");
		}
		if (email.equals(user.getEmail())) {
		    return returnError("The new email must not be same as the old email");
		}
		user.setEmail(email.toLowerCase());
	    }
	    if (!StringUtils.isBlank(username)) {
		if (username.equals(user.getUsername())) {
		    return returnError("The new name must not be same as the old name");
		}
		user.setUsername(username);
	    }
	    if (!StringUtils.isBlank(password)) {
		if (passwordEncoder.matches(password, user.getPassword())) {
		    return returnError("New password cannot be exactly the same as old password");
		}
		user.setPassword(passwordEncoder.encode(password));
	    }
	    userRepository.save(user);
	    return returnSuccess();
	}
	return returnError("User not found");
    }

    public String refresh(String username) {
	return jwtTokenProvider.createToken(username, userRepository.findByEmail(username).getAppUserRoles());
    }

    public Page<User> allUsers(int page) {

	Pageable pageable = PageRequest.of(page, 2);
	Page<User> thirdPage = userRepository.findAll(pageable);
	return thirdPage;
//	return userRepository.findAll();

    }

    public String updateUserById(UserDTO userDataDTO, BindingResult bindingResult) {

	if (bindingResult.hasErrors()) {
	    return returnError(bindingResult.getAllErrors().get(0).getDefaultMessage());
	}

	String email = userDataDTO.getEmail();
	String username = userDataDTO.getUsername();
	String password = userDataDTO.getPassword();
	try {
	    User user = userRepository.findById(Integer.parseInt(userDataDTO.getId()));
	    if (user != null) {
		if (StringUtils.isBlank(email) && StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
		    return returnError("Missing parameter");
		}

		if (!StringUtils.isBlank(email)) {
		    if (userRepository.existsByEmail(email)) {
			return returnError("This email already exists in the system");
		    }
		    if (email.equals(user.getEmail())) {
			return returnError("The new email must not be same as the old email");
		    }
		    user.setEmail(email.toLowerCase());
		}
		if (!StringUtils.isBlank(username)) {
		    if (username.equals(user.getUsername())) {
			return returnError("The new name must not be same as the old name");
		    }
		    user.setUsername(username);
		}
		if (!StringUtils.isBlank(password)) {
		    if (passwordEncoder.matches(password, user.getPassword())) {
			return returnError("New password cannot be exactly the same as old password");
		    }
		    user.setPassword(passwordEncoder.encode(password));
		}
		userRepository.save(user);
		return returnSuccess();
	    }
	} catch (NumberFormatException e) {
	    return returnError("Id must be numeric");
	}
	return returnError("User not found");
    }

    private String returnSuccess() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("success", true);
	return jsonObject.toString();

    }

    private String returnError(String msg) {
	JSONObject jsonObject = new JSONObject();
	JSONObject errors = new JSONObject();
	errors.put("message", msg);
	jsonObject.put("error", errors);
	return jsonObject.toString();
    }

}
