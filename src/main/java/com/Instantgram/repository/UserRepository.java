package com.Instantgram.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Instantgram.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByUsername(String username);
	
	Set<User> findByUsernameIgnoreCaseContainingOrFullNameIgnoreCaseContaining(String subStrUser, String subStrUser2);
	
}
