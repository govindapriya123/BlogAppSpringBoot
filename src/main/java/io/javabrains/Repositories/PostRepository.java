package io.javabrains.Repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT p FROM Post p WHERE p.user.username = :username")
    List<Post>findByUsername(User user);
    
}
