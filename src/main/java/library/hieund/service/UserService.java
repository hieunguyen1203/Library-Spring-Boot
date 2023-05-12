package library.hieund.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

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

import library.hieund.exception.CustomException;
import library.hieund.model.User;
import library.hieund.repository.UserRepository;
import library.hieund.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String login(String username, String password) {
	try {
	    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("success", true);
	    jsonObject.put("access_token",
		    jwtTokenProvider.createToken(username, userRepository.findByEmail(username).getAppUserRoles()));

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
	    jsonObject.put("access_token",
		    jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles()));

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

    public User whoami(HttpServletRequest req) {
	return userRepository.findByEmail(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
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

    private String returnError(String msg) {
	JSONObject jsonObject = new JSONObject();
	JSONObject errors = new JSONObject();
	errors.put("message", msg);
	jsonObject.put("error", errors);
	return jsonObject.toString();
    }

}
