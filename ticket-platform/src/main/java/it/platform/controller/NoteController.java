package it.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.platform.model.Note;
import it.platform.model.Ticket;
import it.platform.model.User;
import it.platform.repository.NoteRepository;
import it.platform.repository.TicketRepository;
import it.platform.repository.UserRepository;
import it.platform.utility.Utility;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	private NoteRepository noteRepo;
	
	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/datanote/{id}")
	private String dataNote(@PathVariable int id, Model model,@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("note", noteRepo.findById(id) );
		Optional<User> user = userRepo.findByUsername(userDetails.getUsername());
		Note note = noteRepo.findById(id);
		if (Utility.isUserRole(user.get())) {
			if (isUserNote(note,user.get())) {
				model.addAttribute("availableToSee", true);				
			}
		}else {
			model.addAttribute("availableToSee", true);
		}
		return "note/data_note";	
	}
	
	
	@PostMapping("/addnote")
	private String storeNote(@Valid @ModelAttribute("note") Note formNote, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("availableToFillIn", true);
		model.addAttribute("availableToSee", true);
		if (bindingResult.hasErrors()) {
			return "note/add_edit_note";
		}		
		noteRepo.save(formNote);		
		return "note/data_note";
	}
	
	@GetMapping("/ticket/{id}")
	public String indexNotes(@PathVariable int id, Model model,
			@AuthenticationPrincipal UserDetails userDetails){
		model.addAttribute("addMode", false);
		Optional<User> user = userRepo.findByUsername(userDetails.getUsername());	
		if(Utility.isUserRole(user.get())) {
			if(isUserTicket(user.get(), id)) {
				model.addAttribute("availableToFillIn", true);						
			}else {
				model.addAttribute("availableToFillIn", false);
			}
		}else {
			model.addAttribute("availableToFillIn", true);
		}
		Ticket ticket = ticketRepo.findById(id);
		List<Note> listNote = new ArrayList<Note>();
		listNote = noteRepo.findByTicket(ticket);
		model.addAttribute("ticket", ticket);
		model.addAttribute("notesList", listNote);		
		
		return "note/note_list";
	}
	
	
	@GetMapping("/ticket/editnote/{id}")
	private String editNote(@PathVariable("id") Integer id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("addMode", false);
		model.addAttribute("availableToFillIn", false);
		Note note = noteRepo.findById(id).get();
		note.setDate(LocalDate.now());
		note.setAuthor(userDetails.getUsername());
		Optional<User> user = userRepo.findByUsername(userDetails.getUsername());		
		if (Utility.isUserRole(user.get())) {
			if (isUserNote(note,user.get())) {
				model.addAttribute("availableToFillIn", true);
				model.addAttribute("note", note);
				
			}else {
				model.addAttribute("availableToFillIn", false);
			}
		} else {
			model.addAttribute("availableToFillIn", true);
			model.addAttribute("note", note);
		}

		return "note/add_edit_note";
	}
	

	@PostMapping("/ticket/editnote/{id}")
	private String storeEditNote(@Valid @ModelAttribute("note") Note formNote,BindingResult bindingResult, Model model ) {
		model.addAttribute("addMode", false);
		
		if (bindingResult.hasErrors()) {
			return "note/add_edit_note";
		}	
		model.addAttribute("availableToSee", true);
		noteRepo.save(formNote);		
		return "note/data_note";
		
	}
	
	//BLOCCO PER USER
	@PostMapping("/deletenote/{id}")
	private String deleteNote(@PathVariable int id, Model model) {
		model.addAttribute("availableToSee", true);
		Note note = noteRepo.findById(id);
		noteRepo.delete(note);
		return "note/data_note";
	}

	
	//metodi
	private boolean isUserNote(Note note, User user) {
		boolean message = false;
		List<Ticket> listUserTicket = ticketRepo.findByUser(user);
		for(Ticket ticketInfo : listUserTicket) {
			if(ticketInfo.getId() == note.getTicket().getId()) {
				message = true;
				break;
			}
		}
		return message;
	}
	
	private boolean isUserTicket(User user, int id) {
		boolean message = false;
		List<Ticket>  listTicket = ticketRepo.findByUser(user);
		if(listTicket != null) {
			for (Ticket ticket : listTicket) {
				if (ticket.getId() == id) {
					message = true;
				}
			}
		}
		return message;
	}
	

}
