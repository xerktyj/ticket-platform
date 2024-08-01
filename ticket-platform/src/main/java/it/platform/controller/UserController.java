package it.platform.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.platform.model.Role;
import it.platform.model.Ticket;
import it.platform.model.User;
import it.platform.repository.RoleRepository;
import it.platform.repository.TicketRepository;
import it.platform.repository.UserRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
    @Autowired
    private TicketRepository ticketRepo;

	
	//BLOCCO  USER CON SECURITY
	@GetMapping("/list")
	public String listUser(@RequestParam(name = "name", required = false) String username, Model model,@AuthenticationPrincipal UserDetails userDetails) {
		List<User> usersList = new ArrayList<User>();
		Optional<User> user = userRepo.findByUsername(username);
		if (username != null) {
			usersList.add(user.get());
		} else {
			usersList = userRepo.findAll();
		}
		model.addAttribute("usersList", usersList);
		return "user/user_list";
	}
	
	@GetMapping("/datauser/{id}")
	private String dataUser(@PathVariable int id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("availableToSee", false);
		Optional<User> user = userRepo.findByUsername(userDetails.getUsername());
		if (Role.isUserRole(user.get())) {
			if(user.get().getId() == id) {
				model.addAttribute("availableToSee", true);
			}
		}else {
			model.addAttribute("availableToSee", true);
		}
		model.addAttribute("user", userRepo.findById(id));
		return "user/data_user";	
	}
	
	//BLOCCO  USER CON SECURITY
	@GetMapping("/adduser")
	public String addTicket(Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("availableToFillIn", true);
		model.addAttribute("user", new User());
		model.addAttribute("rolesList", roleRepo.findAll());
		return "user/add_edit_user";
	}
	
	//BLOCCO USER CON SECURITY
	@PostMapping("/adduser")
	public String storeTicket(@Valid @ModelAttribute("user") User formUser , BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);	
		formUser.setPassword(getEncodePassword(formUser));
		
		if(isUsernameUnique(formUser)) {
				bindingResult.addError(new ObjectError("user","inserire un'altra username che non sia già stata usata"));
			}
		
		if(formUser.getRoles().isEmpty()) {
			bindingResult.addError(new ObjectError("user","inserire il ruolo"));
		}
		
		if (bindingResult.hasErrors()) {
			addEditPostModelBinding(model);
			return "user/add_edit_user";
		}
		model.addAttribute("availableToSee", true);
		userRepo.save(formUser);	
		return "user/data_user";		
	}
	
	
	
	
	@GetMapping("/datauser/edituser/{id}")
	private String editNoteAdmin(@PathVariable int id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("addMode", false);
		model.addAttribute("availableToFillIn", false);
		model.addAttribute("rolesList", roleRepo.findAll());
		Optional<User> user = userRepo.findByUsername(userDetails.getUsername());		
		if (Role.isUserRole(user.get())) {
			if (user.get().getId() == id) {
				model.addAttribute("availableToFillIn", true);
				String password = user.get().getPassword();
				password = password.replace("{noop}", "");
				user.get().setPassword(password);
				model.addAttribute("user", user.get());
			}

		} else {
			User userInfo = userRepo.findById(id);
			String password = userInfo.getPassword();
			password = password.replace("{noop}", "");
			userInfo.setPassword(password);
			model.addAttribute("availableToFillIn", true);
			model.addAttribute("user", userInfo);
		}

		return "user/add_edit_user";
	}

	@PostMapping("/datauser/edituser/{id}")
	private String storeEditNote(@Valid @ModelAttribute("user") User formUser,BindingResult bindingResult, Model model ) {
		model.addAttribute("addMode", false);
		formUser.setPassword(getEncodePassword(formUser));
		
		if(isUsernameUnique(formUser)) {
			bindingResult.addError(new ObjectError("user","inserire un'altra username che non sia già stata usata"));
		}
		
		if (bindingResult.hasErrors()) {
			addEditPostModelBinding(model);
			return "user/add_edit_user";
		}	
		model.addAttribute("availableToSee", true);
		userRepo.save(formUser);		
		return "user/data_user";
		
	}
	
	// BLOCCO PER USER
	@PostMapping("/deleteuser/{id}")
	private String deleteNote(@PathVariable int id, Model model) {
		model.addAttribute("availableToSee", true);
		User user = userRepo.findById(id);
		List<Ticket> ticketUserList =  ticketRepo.findByUser(user);
		if(ticketUserList.size() > 0) {
			model.addAttribute("deleteError", true);
		}else {
			model.addAttribute("deleteCorrect", true);
			userRepo.delete(user);
		}
		
		return "user/data_user";
	}
	
	//metodi
	private void addEditPostModelBinding(Model model) {
		model.addAttribute("availableToFillIn", true);
		model.addAttribute("rolesList", roleRepo.findAll());		
	}
	
	
	private boolean isUsernameUnique(User formUser) {
		boolean message = false;
		for(User user : userRepo.findAll()) {
			if(user.getUsername().equals(formUser.getUsername())) {
				if(user.getId()!=formUser.getId()) {
					message = true;
				}				
			}
		}
		return message;
	}
	
	private String getEncodePassword(User formUser) {
		String password = "{noop}"+formUser.getPassword();
		formUser.setPassword(password);
		return password;
	}
	
	

}
