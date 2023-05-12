package library.hieund.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import library.hieund.dto.UserDataDTO;
import library.hieund.dto.UserResponseDTO;
import library.hieund.model.User;
import library.hieund.service.UserService;

@RestController
@RequestMapping("/users")
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
    public String register(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
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

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = {
	    @Authorization(value = "apiKey") })
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 404, message = "The user doesn't exist"), //
	    @ApiResponse(code = 500, message = "Expired or invalid JWT token") })
    public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
	return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_BORROWER')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = {
	    @Authorization(value = "apiKey") })
    @ApiResponses(value = { //
	    @ApiResponse(code = 400, message = "Something went wrong"), //
	    @ApiResponse(code = 403, message = "Access denied"), //
	    @ApiResponse(code = 500, message = "Expired or invalid JWT token") })
    public UserResponseDTO whoami(HttpServletRequest req) {
	return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_BORROWER')")
    public String refresh(HttpServletRequest req) {
	return userService.refresh(req.getRemoteUser());
    }

}
