package com.Instantgram.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.Instantgram.model.User;
import com.Instantgram.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService {

	User findByUsername(String username);

    User save(UserRegistrationDto registration);
    
    User save(User user);
    
	Set<User> findByUsernameIgnoreCaseContainingOrFullNameIgnoreCaseContaining(String subStrUser, String subStrUser2);

}
