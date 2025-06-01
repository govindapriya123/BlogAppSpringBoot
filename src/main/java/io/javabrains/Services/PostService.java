package io.javabrains.Services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.Dtos.PostRequestDTO;
import io.javabrains.Dtos.TagResponseDTO;
import io.javabrains.Entities.Category;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.Tag;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.CategoryRepository;
import io.javabrains.Repositories.PostRepository;
import io.javabrains.Repositories.TagRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public Post createOrUpdatePost(PostRequestDTO postRequestDTO,User currentUser){
        Post post=new Post();
        System.out.println("postRequestDTO"+postRequestDTO.getStatus());
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setStatus(postRequestDTO.getStatus());
        post.setImageOrder(postRequestDTO.getImageOrder());
        post.setUser(currentUser);
        Category category = categoryRepository.findByName(postRequestDTO.getCategory())
        .orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setName(postRequestDTO.getCategory());
            return categoryRepository.save(newCategory);
        });
        post.setCategory(category);
        // List<Tag> tags = tagRepository.findAllById(postRequestDTO.getTagIds());
        // System.out.printf('--tags--'+tags);
        // post.setTags(tags);
        List<Long> incomingTagIds = postRequestDTO.getTagIds();

if (incomingTagIds == null || incomingTagIds.isEmpty()) {
    System.out.println("incoming tagids are empty");

} else {
    List<Tag> tags = tagRepository.findAllById(incomingTagIds);
    post.setTags(tags);
    System.out.println("incoming ids are not null");
    System.out.println(tags);
}
        post.setLastSaved(LocalDateTime.now());
        return postRepository.save(post);
    }
    public List<TagResponseDTO>getAllTags(){
        return tagRepository.findAll().stream().map(tag->new TagResponseDTO(tag.getId(), tag.getName())).collect(Collectors.toList());    
    }
    public List<Category>getAllCategories(){
        return categoryRepository.findAll();
    }
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }
    public Post updatePost(Long id,PostRequestDTO postRequestDTO,User currUser){
       Post post=postRepository.findById(id).orElseThrow(()->new RuntimeException("Post not found with id "+id));
       if(!post.getUser().getId().equals(currUser.getId())){
        throw new RuntimeException("You are not authorised to update the post.");
       }
       post.setTitle(postRequestDTO.getTitle());
       post.setContent(postRequestDTO.getContent());
       post.setStatus(postRequestDTO.getStatus());
       post.setImageOrder(postRequestDTO.getImageOrder());

       // Update category
       Category category = categoryRepository.findByName(postRequestDTO.getCategory())
               .orElseGet(() -> {
                   Category newCategory = new Category();
                   newCategory.setName(postRequestDTO.getCategory());
                   return categoryRepository.save(newCategory);
               });
       post.setCategory(category);

       // Update tags
       List<Long> incomingTagIds = postRequestDTO.getTagIds();
       if (incomingTagIds != null && !incomingTagIds.isEmpty()) {
           List<Tag> tags = tagRepository.findAllById(incomingTagIds);
           post.setTags(tags);
       } else {
           post.setTags(Collections.emptyList()); // Reset tags if empty
       }

       // Update timestamp
       post.setLastSaved(LocalDateTime.now());

       return postRepository.save(post);
    }
    
}
