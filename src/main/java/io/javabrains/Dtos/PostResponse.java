package io.javabrains.Dtos;

public class PostResponse {
    private Long postId;
    public PostResponse(Long postId){
        this.postId=postId;
    }
    public Long getPostId(){
        return postId;
    }
    public void setPostId(Long postId){
        this.postId=postId;
    }
}
