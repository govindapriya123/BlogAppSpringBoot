package io.javabrains.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.javabrains.Entities.Tag;
import io.javabrains.Repositories.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    public Tag createTag(Tag tag){
        return tagRepository.save((tag));
    }
    public List<Tag>getAllTags(){
        return tagRepository.findAll();
    }
    public Tag getTagById(Long id){
        return tagRepository.findById(id).orElseThrow(()->new RuntimeException("Tag not found"));
    }
    public void deleteTag(Long id){
        tagRepository.deleteById(id);
    }
    
}
