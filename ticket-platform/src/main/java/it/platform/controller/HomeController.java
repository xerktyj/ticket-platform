package it.platform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import it.platform.model.User;
import it.platform.repository.UserRepository;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping
	public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String username = userDetails.getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		model.addAttribute("user", user.get());
		return "home/home";
	}
	

}
