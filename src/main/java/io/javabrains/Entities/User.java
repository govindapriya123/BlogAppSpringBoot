package io.javabrains.Entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class User implements UserDetails {

	@jakarta.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	private String username;
	private String email;
	private String password;
	private boolean profileCompleted=false;
	private String profilePic;
	public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		this.Id = id;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isProfileCompleted() {
		return profileCompleted;
	}
	public void setProfileCompleted(boolean profileCompleted) {
		this.profileCompleted = profileCompleted;
	}
	@Override
	public Collection<? extends GrantedAuthority>getAuthorities(){
		return Collections.emptyList();
	}
	
	@Override
	public boolean isAccountNonExpired(){
		return true;
	}
	@Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
