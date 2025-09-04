package io.javabrains.Dtos;

public class LoginResponse {
    private UserDTO user;
    private String token;
    private String message;
    public LoginResponse(String token,UserDTO user,String message){
       this.token=token;
       this.user=user;
       this.message=message;
    }
    public String getToken() {
        return token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserDTO getUser(){
        return user;
    }
    public void setUser(UserDTO user){
        this.user=user;
    }
 
}
