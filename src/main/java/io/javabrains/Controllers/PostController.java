package io.javabrains.Controllers;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.Dtos.PostRequestDTO;
import io.javabrains.Dtos.PostResponse;
import io.javabrains.Dtos.TagResponseDTO;
import io.javabrains.Entities.Category;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.PostRepository;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Services.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @PostMapping
public ResponseEntity<?> createOrUpdatePost(@RequestBody PostRequestDTO postRequestDTO) {
    try {
        System.out.println("Starting to save the post..."); // Debug log

        Post savedPost = postService.createOrUpdatePost(postRequestDTO);

        System.out.println("Saved post object: " + savedPost); // Debug log
        if (savedPost == null || savedPost.getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Post could not be saved.");
        }

        Long postId = savedPost.getId();
        System.out.println("Saved Post ID: " + postId); // Debug log

        return ResponseEntity.ok(new PostResponse(postId));
    } catch (Exception e) {
        System.err.println("Error occurred: " + e.getMessage()); // Debug log
        e.printStackTrace();

        // Return an error response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing your request. Please try again.");
    }
}

    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>>getMyPosts(Principal principal){
        User user=userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post>myPosts=postRepository.findByUsername(user);
        return ResponseEntity.ok(myPosts);

    }
    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed(){
        List<Post>allPosts=postRepository.findAll();
        return ResponseEntity.ok(allPosts);
    }
    @GetMapping("/tags")
    public ResponseEntity<List<TagResponseDTO>>getAllTags(){
        return ResponseEntity.ok(postService.getAllTags());
    }    
    @GetMapping("/categories")
    public ResponseEntity<List<Category>>getAllCategories(){
        return ResponseEntity.ok(postService.getAllCategories());
    }
    @GetMapping("/auto-save")
    public ResponseEntity<?>autoSavePost(@RequestBody PostRequestDTO postRequestDTO ){
        postRequestDTO.setStatus("DRAFT");
        Post savedPost=postService.createOrUpdatePost(postRequestDTO);
        return ResponseEntity.ok(savedPost);

    }
    @GetMapping("/drafts")
    public ResponseEntity<List<Post>> getDrafts(){
        List<Post>drafts=postRepository.findAll().stream().filter(post->"DRAFT".equals(post.getStatus())).collect(Collectors.toList());
        return ResponseEntity.ok(drafts);
    }
}
