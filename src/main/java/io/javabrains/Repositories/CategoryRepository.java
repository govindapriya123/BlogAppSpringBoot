package io.javabrains.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.javabrains.Entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
    Optional<Category>findByName(String name);
}
