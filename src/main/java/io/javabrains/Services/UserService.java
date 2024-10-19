package io.javabrains.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.Entities.User;
import io.javabrains.Repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	public User registerUser(User user) {
		return userRepository.save(user);
	}
	public User login(String username,String password) {
		 return userRepository.findByUsername(username).orElse(null);
	}

}
