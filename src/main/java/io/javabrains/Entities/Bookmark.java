package io.javabrains.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Bookmark {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;
    public Bookmark() {
    }
    
    public Bookmark(User user2, Post post2) {
        //TODO Auto-generated constructor stub
        this.user=user2;
        this.post=post2;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
}
