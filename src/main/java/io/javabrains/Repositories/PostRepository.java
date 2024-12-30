package io.javabrains.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.javabrains.Entities.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    
}
