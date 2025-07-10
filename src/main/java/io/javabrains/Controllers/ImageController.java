package io.javabrains.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import io.javabrains.Entities.Post;
import io.javabrains.Repositories.PostRepository;


@RestController
@RequestMapping("/api/images")
public class ImageController {
    private static final String UPLOAD_DIR="src/main/resources/static/images";
    @Autowired
    private PostRepository postRepository;
    @PostMapping("/upload")
    public ResponseEntity<String>uploadImage(@RequestParam("images")MultipartFile[] images, @RequestParam("postId") Long postId){
        List<String>imageNames=new ArrayList<>();
        Post existingPost = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        Arrays.stream(images).forEach(file -> {
            try {
                // Generate a unique filename using UUID
                String uniqueID = UUID.randomUUID().toString();
                String fileExtension = getFileExtension(file.getOriginalFilename());
                String uniqueFileName = uniqueID + fileExtension;
                Path path = Paths.get(UPLOAD_DIR + "/" + uniqueFileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                imageNames.add(uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
            }
        });
        existingPost.setImageNames(imageNames);
        postRepository.save(existingPost);
        return ResponseEntity.ok("Files uploaded successfully: " + String.join(", ", imageNames));
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.')); // Extracts the file extension (.jpg, .png, etc.)
    }
    
}
