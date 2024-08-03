
package it.platform.controller;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import it.platform.model.Note;
import it.platform.model.Ticket;
import it.platform.model.User;
import it.platform.repository.CategoryRepository;
import it.platform.repository.NoteRepository;
import it.platform.repository.TicketRepository;
import it.platform.repository.UserRepository;
import it.platform.utility.Utility;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	private TicketRepository ticketRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private NoteRepository noteRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	

	@GetMapping
	public String listTicket(@RequestParam(name = "name", required = false) String title, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		List<Ticket> listTicket = new ArrayList<Ticket>();
		String username = userDetails.getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if (Utility.isUserRole(user.get())) {
			listTicket = ticketRepo.findByUser(user.get());
			if (Utility.search(title)) {
				listTicket.clear();
				listTicket = findTicketInList(ticketRepo.findByUser(user.get()), title);
			}
		} else {
			listTicket = ticketRepo.findAll();
			if (Utility.search(title)) {
				listTicket.clear();
				listTicket = ticketRepo.findByTitleContaining(title);
			}
		}
 
		model.addAttribute("ticketList", listTicket);
		return "ticket/ticket_list";
	}
	

	@GetMapping("/{id}")
	public String dataTicket(@PathVariable int id , Model model,@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("userTicketPresent", false);
		String username = userDetails.getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if(Utility.isUserRole(user.get())) {
			if(isUserTicket(user.get(), id)) {
				model.addAttribute("userTicketPresent", true);
			}
		}else {
			model.addAttribute("userTicketPresent", true);
		}
		model.addAttribute("ticket", ticketRepo.findById(id));
		return "ticket/data";
	}
	
	//BLOCCO USER CON SECURITY
	@GetMapping("/addticket")
	public String addTicket(Model model) {
		model.addAttribute("userTicketPresent", true);
		model.addAttribute("addMode", true);
		model.addAttribute("ticket", new Ticket());
		model.addAttribute("categoryList", categoryRepo.findAll());
		model.addAttribute("usersAvailable", userRepo.findByActiveState(true));
		return "ticket/add_edit";
	}
	
	//BLOCCO USER CON SECURITY
	@PostMapping("/addticket")
	public String storeTicket(@Valid @ModelAttribute("ticket") Ticket formTicket , BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("userTicketPresent", true);
		if(formTicket.getCategories().isEmpty()) {
			bindingResult.addError(new ObjectError("ticket","inserire la categoria del ticket"));
		}
		
		if (bindingResult.hasErrors()) {
			addEditPostModelBinding(model);
			return "ticket/add_edit";
		}
		
		ticketRepo.save(formTicket);		
		return "ticket/data";		
	}
	
	@GetMapping("/editticket/{id}")
	private String editTicket(@PathVariable int id , Model model,@AuthenticationPrincipal UserDetails userDetails) {
		//controllo per fare in modo di non mettere nell'URL un id di un ticket differente
		model.addAttribute("userTicketPresent", false);
		String username = userDetails.getUsername();
		Optional<User> user = userRepo.findByUsername(username);
		if(Utility.isUserRole(user.get())) {
			if(isUserTicket(user.get(), id)) {
				model.addAttribute("userTicketPresent", true);
			}
		}else {
			model.addAttribute("userTicketPresent", true);
		}
		model.addAttribute("addMode", false);
		model.addAttribute("ticket", ticketRepo.findById(id));
		model.addAttribute("categoryList", categoryRepo.findAll());
		model.addAttribute("usersAvailable", userRepo.findByActiveState(true));
		return "ticket/add_edit";
	}
	
	@PostMapping("/editticket/{id}")
	public String storeEditTicket(@Valid @ModelAttribute("ticket") Ticket formTicket , BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", false);
		model.addAttribute("userTicketPresent", true);
		
		if (isStartDateAfterEndDate(formTicket) ) {			
				bindingResult.addError(
						new ObjectError("ticket", "la data di chiusura ticket è prima della data di inizio"));			
		}
		
		if (isTicketClosed(formTicket)) {			
				bindingResult
						.addError(new ObjectError("ticket", "se hai finito il ticket prego inserire le ore lavorate"));			
		}
		
	    if(formTicket.getCategories().isEmpty()) {
			bindingResult.addError(new ObjectError("ticket","inserire la categoria del ticket"));
		}
		
		if (bindingResult.hasErrors()) {
			addEditPostModelBinding(model);
			return "ticket/add_edit";
		}
		
		ticketRepo.save(formTicket);		
		return "ticket/data";		
	}
	
	
	//BLOCCO USER CON SECURITY
	@PostMapping("/delete/{id}")
	private String deleteTicket(@PathVariable int id, Model model) {
		model.addAttribute("delete", true);
		model.addAttribute("userTicketPresent", true);
		Ticket ticket = ticketRepo.findById(id);
		List<Note> notes = noteRepo.findByTicket(ticket);
		noteRepo.deleteAll(notes);
		ticketRepo.delete(ticket);
		return "ticket/data";		
	}
	
	
	@GetMapping("/addnote/{id}")
	private String addOffer(@PathVariable int id , Model model,@AuthenticationPrincipal UserDetails userDetails) {
		model.addAttribute("addMode", true);
		model.addAttribute("availableToFillIn", true);
		String username = userDetails.getUsername();
		Optional<User> user = userRepo.findByUsername(username);
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
		Note note = new Note();
		note.setTicket(ticket);
		note.setAuthor(username);
		note.setDate(LocalDate.now());
		model.addAttribute("note", note);
		return "note/add_edit_note";
	}
	
	//metodi
	private void addEditPostModelBinding(Model model) {
		model.addAttribute("categoryList", categoryRepo.findAll());
		model.addAttribute("usersAvailable", userRepo.findByActiveState(true));
	}
	
	protected boolean isUserTicket(User user, int id) {
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
	
	private List<Ticket> findTicketInList(List<Ticket> ticketList, String title) {
		List<Ticket> ticketInfo = new ArrayList<Ticket>();
		for(Ticket ticket : ticketList) {
			if(ticket.getTitle().equals(title)) {
				ticketInfo.add(ticket);
			}
		}
		return ticketInfo;
	}
	
	
	private boolean isStartDateAfterEndDate(Ticket formTicket) {
		boolean message = false;
		if (formTicket.getStartDate() != null) {
			if (formTicket.getEndDate().isBefore(formTicket.getStartDate())) {
				message = true;
			}
		}
		return message;
	}
	
	
	private boolean isTicketClosed(Ticket formTicket) {
		boolean message = false;
		if (formTicket.getStatusTicket().equals("chiuso") && formTicket.getTimeWorked() == 0) {	
			message = true;
		}
		return message;
	}
}
