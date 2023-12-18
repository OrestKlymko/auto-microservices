package com.example.userservice.repository;

import com.example.userservice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
	Optional<UserModel> findUserModelByEmail(String email);
	Optional<UserModel> findUserModelByUsername(String username);
}