package com.Instantgram.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Instantgram.model.User;
import com.Instantgram.service.UserService;

@RestController
@RequestMapping("/follow")
public class FollowController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/{followingName}")
	public Set<User> follow(@PathVariable String followingName, Authentication authentication) {
		String username = authentication.getName();
		User follower = userService.findByUsername(username);
		User following = userService.findByUsername(followingName);
		following.addFollower(follower);
		userService.save(following);
		follower = userService.findByUsername(username);
		
		return follower.getFollowing();
	}
	
	@DeleteMapping("/{uname}")
	public Set<User> unfollow(@PathVariable String uname, Authentication authentication) {
		String username = authentication.getName();
		User follower = userService.findByUsername(username);
		User following = userService.findByUsername(uname);
		following.remFollower(follower);
		userService.save(following);
		follower = userService.findByUsername(username);
		
		return follower.getFollowing();
	}
	
	@GetMapping("/followers/{uname}")
	public Set<User> getFollowers(@PathVariable String uname) {
		User user = userService.findByUsername(uname);
		return user.getFollowers();
	}
	
	@GetMapping("/following/{uname}")
	public Set<User> getFollowing(@PathVariable String uname) {
		User user = userService.findByUsername(uname);
		return user.getFollowing();
	}
	
}
