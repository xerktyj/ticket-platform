package it.platform.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.platform.exception.IllegalListSizeException;
import it.platform.model.Category;
import it.platform.model.Ticket;
import it.platform.response.Payload;
import it.platform.service.CategoryService;
import it.platform.service.TicketService;




@RestController
@CrossOrigin
@RequestMapping("/api/ticket")
public class RestControllerTicket {
	
	@Autowired(required=true)
	private TicketService ticketService;
    
	@Autowired
	private CategoryService categoryService;
	
	
	@GetMapping
	public ResponseEntity index(@RequestParam(name = "name", required = false) String statusTicket,@RequestParam(name = "categoria", required = false) String categoria) {
		List<Ticket> listTicket = new ArrayList<Ticket>();
	    Category category = categoryService.findByName(categoria);	
		if (statusTicket!= null && categoria == null) {
			listTicket = ticketService.findByStatusTicket(statusTicket);
		}else if(categoria != null && statusTicket == null) {
			listTicket = ticketService.findByCategories(category);
		}
		else {
			listTicket = ticketService.findAll();
		}
		try {
			if (listTicket.size() == 0)
				throw new IllegalListSizeException();
			return new ResponseEntity<List<Ticket>>(listTicket, HttpStatus.OK);
		} catch (IllegalListSizeException e) {
			return new ResponseEntity<Payload<Ticket>>(new Payload<Ticket>(null,e.getMessage(),null), HttpStatus.NOT_FOUND);
		}catch(Exception e) {
			return new ResponseEntity<Payload<Ticket>>(new Payload<Ticket>(null,e.getMessage(),null), HttpStatus.BAD_REQUEST);
		}
	}
	

}
