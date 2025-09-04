package io.javabrains.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.Dtos.BookmarkRequest;
import io.javabrains.Dtos.PostResponseDTO;
import io.javabrains.Entities.Bookmark;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.BookmarkRepository;
import io.javabrains.Repositories.PostRepository;
import io.javabrains.Repositories.UserRepository;
import io.javabrains.Services.BookmarkService;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    private User getCurrentUser(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }
    @PostMapping("/{postId}")
    public ResponseEntity<String>addBookmark(@PathVariable Long postId ){
       User user=getCurrentUser();
       Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
       bookmarkService.addBookmark(user, post);
       return ResponseEntity.ok("Bookmark added successfully");
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String>removeBookmark(@PathVariable Long postId){
        User user=getCurrentUser();
        Post post=postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));
        bookmarkService.removeBookmark(user, post);
        return ResponseEntity.ok("Bookmark removed");
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getBookmarks(){
        User user=getCurrentUser();
        List<Post>bookmarkedPosts=bookmarkService.getBookmarkedPosts(user);
        List<PostResponseDTO>response=bookmarkedPosts.stream().map(post->new PostResponseDTO(post,true)).toList();
        System.out.println("Bookmarks for user " + user.getUsername() + ": " + bookmarkedPosts.size());
        System.out.println("bookmarkedPosts"+bookmarkedPosts);
        return ResponseEntity.ok(response);
        
    }

    @PostMapping("/toggle")
    public ResponseEntity<?> toggleBookmark(@RequestBody BookmarkRequest request){
        User user=getCurrentUser();
        Optional<Post> postOpt=postRepository.findById(request.getPostId());
        if(postOpt.isEmpty()){
            return ResponseEntity.badRequest().body("Post not found");
        }
        Optional<Bookmark>bookmarkOpt=bookmarkRepository.findByUserAndPost(user,postOpt.get());
        if(bookmarkOpt.isPresent()){
            bookmarkRepository.delete(bookmarkOpt.get());
            return ResponseEntity.ok().body("{bookmarked\": false}");
        }else{
            Bookmark bookmark=new Bookmark();
            bookmark.setUser(user);
            bookmark.setPost(postOpt.get());
            bookmarkRepository.save(bookmark);
            return ResponseEntity.ok().body("{\"bookmarked\": true}");

        }

    }
}
