package it.platform.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.platform.model.Category;
import it.platform.model.Ticket;
import it.platform.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService{
	
	@Autowired
	private TicketRepository ticketRepo;
	
	@Override
	public Optional<Ticket> findById(Integer id){
		return ticketRepo.findById(id);
	}
	
	@Override
	public List<Ticket> findByTitle(String name){
		return ticketRepo.findByTitleContaining(name);
	}
	
	@Override
	public List<Ticket> findByStatusTicket(String statusTicket){
		return ticketRepo.findByStatusTicket(statusTicket);
	}
	
	
	@Override
	public List<Ticket> findAll(){
		return ticketRepo.findAll();
	}
	
	@Override
	public Ticket save(Ticket ticket) {
		return ticketRepo.save(ticket);
	}
	
	@Override
	public List<Ticket> findByCategories(Category category){
		return ticketRepo.findByCategories(category);
	}
	
 
}
