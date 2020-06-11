package com.Instantgram.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Instantgram.model.User;
import com.Instantgram.service.UserService;

@Controller
public class MainController {

	@Autowired
	UserService userService;

	@GetMapping("/")
	public String root() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(Model model, Authentication authentication) {
		String username = authentication.getName();
		model.addAttribute("currentUser", username);
		return "home";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "auth/login";
	}

	@GetMapping("/profile/{uname}")
	public String profile(@PathVariable("uname") String uname, Model model, Authentication authentication) {
		
		String username = authentication.getName();

		if (uname.equals(username)) {
			model.addAttribute("isSelf", "true");
		}
		User user = userService.findByUsername(uname);
		System.out.println(user);
		model.addAttribute("currentUser", username);
		model.addAttribute("username", uname);
		model.addAttribute("fullName", user.getFullName());
		return "profile";
	}
	
	@GetMapping("/getusers")
	@ResponseBody
	public Set<User> getUsers(@RequestParam String subStrUser) {
		return userService.findByUsernameIgnoreCaseContainingOrFullNameIgnoreCaseContaining(subStrUser, subStrUser);
	}

	@GetMapping("/nav")
	public String nav(Model model, Authentication authentication) {
		String username = authentication.getName();

		String fullName = userService.findByUsername(username).getFullName();
		model.addAttribute("username", username);
		model.addAttribute("fullName", fullName);

		return "nav";
	}
}
