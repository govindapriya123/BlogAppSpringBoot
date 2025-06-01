package io.javabrains.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;


@Entity
public class Post {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NotEmpty(message ="Title cannot be empty")
   @Size(max=100,message="Title must be less than 100 characters")
   private String title;
   @Lob
   @Column(name = "content", columnDefinition = "LONGTEXT")
   private String content;
   @ManyToMany
   @JoinTable(
    name = "post_tag",               
    joinColumns = @JoinColumn(name = "id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
    private List<Tag> tags;
    @ManyToOne
    private Category  category; 
    @ElementCollection
    private List<String> imageOrder; 
    @ElementCollection
    private List<String> imageNames = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
     private User user;
    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user=user;
    }

    // Getters and setters
    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }
    private String status; 
    private LocalDateTime lastSaved; // Timestamp for auto-save
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags; 
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<String> getImageOrder() {
        return imageOrder;
    }
    public void setImageOrder(List<String> imageOrder) {
        this.imageOrder = imageOrder;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getLastSaved() {
        return lastSaved;
    }
    public void setLastSaved(LocalDateTime lastSaved) {
        this.lastSaved = lastSaved;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
