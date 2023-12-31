package com.learning.resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.service.UserService;

import jakarta.ws.rs.QueryParam;

import com.learning.model.User;



@RestController
@RequestMapping(path = "api/v1/user")
public class UserResource {
	
	private UserService userService;
	
	@Autowired
	public UserResource(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path = "hello"
			)
	public List<User> fetchUsers(@QueryParam("gender")String gender, @QueryParam("ageLessThan") Integer ageLessThan){
		System.out.println(gender); 
		System.out.println(ageLessThan);
		return userService.getAllUser();
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "{userUid}")
	public List<User> fetchUser() {
		return userService.getAllUser();
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "{userUid}")
	public ResponseEntity<?>fetchUser(@PathVariable("userUid") UUID userUid){
		return userService.getUser(userUid).<ResponseEntity<?>>map(ResponseEntity::ok)
		.orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorMessage("user" + userUid+ "was not found")));
	}
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Integer> insertNewUser(@RequestBody User user){
		int result = userService.insertUser(user);
		return getIntegerResponseEntity(result);
	}
	
	@RequestMapping(method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Integer> updateUser(@RequestBody User user){
		int result = userService.updateUser(user);
		return getIntegerResponseEntity(result);
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE,
			path = "{userUid}")
	private ResponseEntity<Integer> deleteUser(@PathVariable("userUid") UUID userUid){
		int result = userService.removeUser(userUid);
		return getIntegerResponseEntity(result);
		
	}
	
	
	
	private ResponseEntity<Integer> getIntegerResponseEntity(int result) {
		if(result == 1) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}


	class ErrorMessage{
		String message;
		
		public ErrorMessage(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
	 
	}
}
