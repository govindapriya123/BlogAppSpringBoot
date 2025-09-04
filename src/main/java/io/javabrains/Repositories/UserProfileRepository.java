package io.javabrains.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.javabrains.Entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
