package io.javabrains.Controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Utilities.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // @Autowired
    // private UserRepository userRepository;
    // @Autowired
    // private final PasswordEncoder passwordEncoder;
    // @Autowired
    // private JwtUtil jwtUtil;
    public AuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // @Autowired
    // public AuthenticationController(PasswordEncoder passwordEncoder) {
    //     this.passwordEncoder = passwordEncoder;
    // }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody User loginRequest){
        User user=userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(()->new RuntimeException("User not found"));
        System.out.println("Request Password: " + loginRequest.getPassword());
        System.out.println("Stored Hashed Password: " + user.getPassword());
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
        String token=jwtUtil.generateToken(user.getUsername());
         Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("profileCompleted", user.isProfileCompleted());
        response.put("user",user);

        return ResponseEntity.ok(response);

    }
    @PostMapping("/validate-username")
    public ResponseEntity<String>validateUsername(@RequestBody Map<String,String>request){
        String username=request.get("username");
        boolean exists=userRepository.findByUsername(username).isPresent();
        if(exists){
         return ResponseEntity.status(409).body("Username is already taken");
        }
        return ResponseEntity.ok("Username is available");
    }

    
    
}
