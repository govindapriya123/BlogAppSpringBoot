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

import io.javabrains.Dtos.PostResponseDTO;
import io.javabrains.Dtos.TagResponseDTO;
import io.javabrains.Dtos.UserDTO;
import io.javabrains.Entities.Category;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.BookmarkRepository;
import io.javabrains.Repositories.PostRepository;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Services.PostService;
import io.javabrains.Utilities.UserMapper;

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
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @PostMapping
    public ResponseEntity<?> createOrUpdatePost(@RequestBody PostRequestDTO postRequestDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            } else {
                throw new RuntimeException("Invalid user authentication type");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Post savedPost = postService.createOrUpdatePost(postRequestDTO, user);

            if (savedPost == null || savedPost.getId() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Post could not be saved.");
            }

            return ResponseEntity.ok(new PostResponseDTO(savedPost, false));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<PostResponseDTO>> getMyPosts(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> myPosts = postRepository.findByUsername(user);

        List<PostResponseDTO> response = myPosts.stream()
                .map(post -> new PostResponseDTO(post, bookmarkRepository.existsByUserAndPost(user, post)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDTO>> getFeed(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> allPosts = postRepository.findAll();

        List<PostResponseDTO> response = allPosts.stream()
                .map(post -> new PostResponseDTO(post, bookmarkRepository.existsByUserAndPost(user, post)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(postService.getAllTags());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(postService.getAllCategories());
    }

    @PostMapping("/auto-save")
    public ResponseEntity<PostResponseDTO> autoSavePost(@RequestBody PostRequestDTO postRequestDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postRequestDTO.setStatus("DRAFT");
        Post savedPost = postService.createOrUpdatePost(postRequestDTO, user);

        return ResponseEntity.ok(new PostResponseDTO(savedPost, false));
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostResponseDTO>> getDrafts(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<Post> drafts = postRepository.findAll().stream()
                .filter(post -> "DRAFT".equals(post.getStatus()))
                .collect(Collectors.toList());

        List<PostResponseDTO> response = drafts.stream()
                .map(post -> new PostResponseDTO(post, bookmarkRepository.existsByUserAndPost(user, post)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id,
                                                      @RequestBody PostRequestDTO newPostData,
                                                      @AuthenticationPrincipal User currentUser) {
        Post updatedPost = postService.updatePost(id, newPostData, currentUser);
        return ResponseEntity.ok(new PostResponseDTO(updatedPost, false));
    }
}
