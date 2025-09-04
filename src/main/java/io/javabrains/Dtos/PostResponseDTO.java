package io.javabrains.Dtos;

import java.time.LocalDateTime;
import java.util.List;

import io.javabrains.Entities.Category;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.Tag;
import io.javabrains.Entities.User;
import io.javabrains.Utilities.UserMapper;

public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private UserDTO user;
    private List<String> imageNames;
    private List<Tag> tags;
    private boolean bookmarked;
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public LocalDateTime getLastSaved() {
        return lastSaved;
    }

    public void setLastSaved(LocalDateTime lastSaved) {
        this.lastSaved = lastSaved;
    }

    private String status;
    private List<String> imageOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSaved;

    public PostResponseDTO(Post post, boolean isBookmarked) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
       if (post.getUser() != null) {
            this.user = UserMapper.toUserDTO(post.getUser());
        }
        this.imageNames = post.getImageNames();
        this.tags = post.getTags();
        this.bookmarked = isBookmarked;
        this.status = post.getStatus();
        this.imageOrder = post.getImageOrder();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.lastSaved = post.getLastSaved();
    }

    public PostResponseDTO(Long postId) {
        this.id=postId;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
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
    
    public boolean isBookmarked() {
        return bookmarked;
    }
    
    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
    
    
}
