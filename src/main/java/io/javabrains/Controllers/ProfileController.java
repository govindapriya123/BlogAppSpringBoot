package io.javabrains.Controllers;
import io.javabrains.Dtos.ProfileUpdateRequest;
import io.javabrains.Entities.User;
import io.javabrains.Entities.UserProfile;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Repositories.UserProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    // ✅ Constructor injection (Spring will auto-wire these)
    public ProfileController(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @PutMapping(value = "/profile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            Principal principal
    ) {
        try {
            String currentUsername = principal.getName();
            User user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
            }

            if (request.getUsername() != null) user.setUsername(request.getUsername());
            if (request.getPhone()!= null) profile.setPhone(request.getPhone());
            if (request.getDob() != null) profile.setDob(LocalDate.parse(request.getDob()));
            if (request.getGender() != null) profile.setGender(request.getGender());
            if (request.getBio() != null) profile.setBio(request.getBio());
            if (request.getLocation() != null) profile.setLocation(request.getLocation());

            //  if (profilePic != null && !profilePic.isEmpty()) {
            //     String imageUrl = "uploads/" + profilePic.getOriginalFilename(); // implement saveImage
            //     profile.setProfilePic(imageUrl);
            // }

            profile.setProfileCompleted(true);
            user.setProfile(profile);

            userRepository.save(user); // cascade saves profile too

            return ResponseEntity.ok("Profile updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error updating profile: " + e.getMessage());
        }
    }
}
