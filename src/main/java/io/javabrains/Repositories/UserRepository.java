package io.javabrains.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.javabrains.Entities.User;

public interface UserRepository  extends JpaRepository<User,Long>{
	Optional<User>findByUsername(String username);

}
