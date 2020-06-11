package com.Instantgram.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Instantgram.model.Role;
import com.Instantgram.model.User;
import com.Instantgram.repository.UserRepository;
import com.Instantgram.web.dto.UserRegistrationDto;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    
    @Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

    public User save(UserRegistrationDto registration){
        User user = new User();
        user.setFullName(registration.getFullName());
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));

    }
    
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

	@Override
	public Set<User> findByUsernameIgnoreCaseContainingOrFullNameIgnoreCaseContaining(String subStrUser, String subStrUser2) {
		return userRepository.findByUsernameIgnoreCaseContainingOrFullNameIgnoreCaseContaining(subStrUser,subStrUser2);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

}
