package library.hieund.controller;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import library.hieund.dto.UserDTO;
import library.hieund.dto.UserResponseDTO;
import library.hieund.model.User;
import library.hieund.service.UserService;
import library.hieund.validator.EmailConstraint;

@RestController
@RequestMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Api(tags = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    @ApiOperation(value = "${UserController.login}")
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 422, message = "Invalid username/password supplied") })
    public String login(//
	    @ApiParam("Email") @RequestParam String email, //
	    @ApiParam("Password") @RequestParam String password) {
	return userService.login(email, password);
    }

    @PostMapping("/register")
    @ApiOperation(value = "${UserController.register}")
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 422, message = "Username is already in use") })
    public String register(@ApiParam("Signup User") @RequestBody UserDTO user) {
	return userService.register(modelMapper.map(user, User.class));
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value = "apiKey") })
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 404, message = "The user doesn't exist"), //
	    @ApiResponse(code = 500, message = "Expired or invalid JWT token") })
    public String delete(@ApiParam("Username") @PathVariable String username) {
	userService.delete(username);
	return username;
    }

//    @GetMapping(value = "/{username}")
//    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
//    @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = {
//	    @Authorization(value = "apiKey") })
//    @ApiResponses(value = { //
//	    @ApiResponse(code = 400, message = "Something went wrong"), //
//	    @ApiResponse(code = 403, message = "Access denied"), //
//	    @ApiResponse(code = 404, message = "The user doesn't exist"), //
//	    @ApiResponse(code = 500, message = "Expired or invalid JWT token") })
//    public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
//	return modelMapper.map(userService.search(username), UserResponseDTO.class);
//    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_BORROWER')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {
	    @Authorization(value = "apiKey") })
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 500, message = "Expired or invalid JWT token") })
    public UserResponseDTO whoAmI(HttpServletRequest req) {
	return modelMapper.map(userService.whoAmI(req), UserResponseDTO.class);
    }

    @PatchMapping("/me/update")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_BORROWER')")
    public String updateUser(HttpServletRequest req, @RequestBody @Valid UserDTO userDataDTO,
	    BindingResult bindingResult) {

	return userService.updateUser(req, userDataDTO, bindingResult);

    }

//    @GetMapping("/refresh")
//    @PreAuthorize("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_BORROWER')")
//    public String refresh(HttpServletRequest req) {
//	return userService.refresh(req.getRemoteUser());
//    }

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public String allUsers(@RequestParam(required = false, defaultValue = "1") int page) {

	try {
	    int pageNumber = page - 1;
	    Page<User> users = userService.allUsers(pageNumber);
	    List<UserResponseDTO> userResponseDTOs = Arrays
		    .asList(modelMapper.map(users.toList(), UserResponseDTO[].class));

	    JSONObject usersJson = new JSONObject();
	    usersJson.put("users", userResponseDTOs);
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("data", usersJson);
	    return jsonObject.toString();
	} catch (Exception e) {
	    JSONObject jsonObject = new JSONObject();
	    JSONObject errors = new JSONObject();
	    errors.put("message", "Page must be numeric");
	    jsonObject.put("error", errors);
	    return jsonObject.toString();
	}

    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public String updateUserById(@RequestBody @Valid UserDTO userDataDTO, BindingResult bindingResult) {
//	return "ok";
	return userService.updateUserById(userDataDTO, bindingResult);

    }

}
