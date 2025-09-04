package io.javabrains.Controllers;

import java.util.Base64;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.javabrains.Dtos.LoginRequest;
import io.javabrains.Dtos.LoginResponse;
import io.javabrains.Dtos.ProfileUpdateRequest;
import io.javabrains.Dtos.UserDTO;
import io.javabrains.Dtos.UserProfileRequest;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Services.UserService;
import io.javabrains.Utilities.UserMapper;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("username: " + loginRequest.getUsername());
        System.out.println("email: " + loginRequest.getEmail());
        System.out.println("password: " + loginRequest.getPassword());

        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            UserDTO userDTO =UserMapper.toUserDTO(user);
            System.out.println("userDTO"+userDTO);

            return ResponseEntity.ok(new LoginResponse("Login successful", userDTO));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Invalid credentials", null));
        }
    }

    public String saveImage(MultipartFile file, String filename) throws IOException {
        String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/profile"; // Save outside the JAR
        Path uploadPath = Paths.get(UPLOAD_DIR);
        String fileName = filename + ".jpg";

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/profile/" + fileName; // Return relative path for frontend
    }
}
