package it.platform.service;

import java.util.List;
import java.util.Optional;
import it.platform.model.Category;
import it.platform.model.Ticket;

public interface TicketService {
	
	public Optional<Ticket> findById(Integer id);
		
		public List<Ticket> findByTitle(String name);
		
		public List<Ticket> findAll();
		
		public Ticket save(Ticket ticket);
		
		public List<Ticket> findByStatusTicket(String statusTicket);
		
		public List<Ticket> findByCategories(Category category);
	
}
