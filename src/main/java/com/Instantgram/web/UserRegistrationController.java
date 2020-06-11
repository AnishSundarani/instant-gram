package com.Instantgram.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Instantgram.model.User;
import com.Instantgram.service.UserService;
import com.Instantgram.web.dto.UserRegistrationDto;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "auth/registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result){
    	
    	System.out.println(userDto.getFullName());

        User existing = userService.findByUsername(userDto.getUsername());
        if (existing != null){
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        if (result.hasErrors()){
            return "auth/registration";
        }

        userService.save(userDto);
        return "redirect:/registration?success";
    }

}
