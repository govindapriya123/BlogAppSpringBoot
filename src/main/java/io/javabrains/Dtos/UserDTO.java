package io.javabrains.Dtos;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private boolean profileCompleted;
    private String profilePic;
    private UserProfileDTO profile;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isProfileCompleted() {
        return profileCompleted;
    }
    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public UserProfileDTO getProfile() {
        return profile;
    }
    public void setProfile(UserProfileDTO profile) {
        this.profile = profile;
    }
    
    
}
