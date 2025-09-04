package io.javabrains.Utilities;

import io.javabrains.Dtos.UserDTO;
import io.javabrains.Dtos.UserProfileDTO;
import io.javabrains.Entities.User;
import io.javabrains.Entities.UserProfile;

public class UserMapper {

    public static UserDTO toUserDTO(User user){
        if(user==null){
            return null;
        }
        UserDTO dto=new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfileCompleted(user.isProfileCompleted());
        dto.setProfilePic(user.getProfilePic());

        if (user.getProfile() != null) {
            dto.setProfile(toUserProfileDTO(user.getProfile()));
        }

        return dto;
    }

    public static UserProfileDTO toUserProfileDTO(UserProfile profile){
        if(profile==null){
            return null;
        }
        UserProfileDTO dto=new UserProfileDTO();
        dto.setPhone(profile.getPhone());
        dto.setDob(profile.getDob());
        dto.setGender(profile.getGender());
        dto.setLocation(profile.getLocation());
        dto.setBio(profile.getBio());
        dto.setProfileCompleted(profile.isProfileCompleted());
        dto.setProfilePic(profile.getProfilePic());

        return dto;

    }
    
}

