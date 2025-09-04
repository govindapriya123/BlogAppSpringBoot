package io.javabrains.Services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.Entities.Bookmark;
import io.javabrains.Entities.Post;
import io.javabrains.Entities.User;
import io.javabrains.Repositories.BookmarkRepository;

@Service
public class BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;
    public boolean isBookmarked(User user,Post post){
        return bookmarkRepository.existsByUserAndPost(user,post);
    }
    public Bookmark addBookmark(User user,Post post){
        if(isBookmarked(user, post)){
            throw new RuntimeException("Already Bookmarked");
        }
        return bookmarkRepository.save(new Bookmark(user, post));
    }
    public void removeBookmark(User user,Post post){
        bookmarkRepository.findByUserAndPost(user,post).ifPresent(bookmarkRepository::delete);
    }
    public List<Post>getBookmarkedPosts(User user){
        return bookmarkRepository.findByUser(user).stream().map(Bookmark::getPost).filter(Objects::nonNull).toList();
    }
    
}
