package io.javabrains.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import io.javabrains.Entities.Tag;

public interface TagRepository extends JpaRepository<Tag,Long>{
    List<Tag> findAllByIdIn(List<Long> ids);
}
