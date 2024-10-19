package io.javabrains.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.Dtos.LoginRequest;
import io.javabrains.Entities.User;
import io.javabrains.Services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	 public ResponseEntity<User>register(@RequestBody User user){
		System.out.println("--user--"+user);
		return new ResponseEntity<>(userService.registerUser(user),HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<User>login(@RequestBody LoginRequest loginRequest){
		System.out.println("username"+loginRequest.getUsername());
		System.out.println("email"+loginRequest.getEmail());
		System.out.println("password"+loginRequest.getPassword());
		User user=userService.login(loginRequest.getUsername(), loginRequest.getPassword());
		if (user != null) {
			System.out.println("User found: " + user.getUsername());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
	}
	
}
