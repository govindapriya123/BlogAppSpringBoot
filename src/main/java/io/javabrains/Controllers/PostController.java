package io.javabrains.Controllers;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal; // When using JWT or basic authentication
            } else {
                throw new RuntimeException("Invalid user authentication type");
            }
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Post savedPost = postService.createOrUpdatePost(postRequestDTO, user);
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
    public ResponseEntity<List<Post>> getMyPosts(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> myPosts = postRepository.findByUsername(user);
        return ResponseEntity.ok(myPosts);

    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed() {
        List<Post> allPosts = postRepository.findAll();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(postService.getAllTags());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(postService.getAllCategories());
    }

    @GetMapping("/auto-save")
    public ResponseEntity<?> autoSavePost(@RequestBody PostRequestDTO postRequestDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        postRequestDTO.setStatus("DRAFT");
        Post savedPost = postService.createOrUpdatePost(postRequestDTO, user);
        return ResponseEntity.ok(savedPost);

    }

    @GetMapping("/drafts")
    public ResponseEntity<List<Post>> getDrafts() {
        List<Post> drafts = postRepository.findAll().stream().filter(post -> "DRAFT".equals(post.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(drafts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO newPostData,
            @AuthenticationPrincipal User currentUser) {
        Post updatedPost = postService.updatePost(id, newPostData, currentUser);
        Map<String,Object>response=new HashMap<>();
        response.put("postId",updatedPost.getId());
        System.out.println("updatedPost"+updatedPost);
        return ResponseEntity.ok(response);
    }
}
