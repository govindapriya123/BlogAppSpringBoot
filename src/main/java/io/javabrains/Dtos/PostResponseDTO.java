package io.javabrains.Dtos;

import java.util.List;

import io.javabrains.Entities.Post;

public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private List<Long>tagIds;
    private String categoryId;
    private List<String>imageOrder;
    private String status; //DRAFT or published
    private boolean bookmarked;
     public PostResponseDTO(Post post, boolean bookmarked) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.bookmarked = bookmarked;
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
    public List<Long> getTagIds() {
        return tagIds;
    }
    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
    public String getCategory() {
        return categoryId;
    }
    public void setCategory(String categoryId) {
        this.categoryId = categoryId;
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
