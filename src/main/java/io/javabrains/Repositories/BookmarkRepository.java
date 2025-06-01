package io.javabrains.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import io.javabrains.Entities.Bookmark;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark>findByUser(User user);
    boolean existsByUserAndPost(User user,Post post);
    void deleteByUserAndPost(User user,Post post);
    Optional<Bookmark>findByUserAndPost(User user,Post post);
}
