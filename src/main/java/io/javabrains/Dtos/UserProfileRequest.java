package io.javabrains.Dtos;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileRequest {
    private String username;
    private MultipartFile profilePic;
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public MultipartFile getProfilePic(){
        return profilePic;
    }
    public void setProfilePic(MultipartFile profilePic){
        this.profilePic=profilePic;
    } 
    
}
