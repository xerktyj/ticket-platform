package it.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.platform.model.Category;
import it.platform.model.Ticket;
import it.platform.model.User;
import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Integer>{
	
	public List<Ticket> findByTitleContaining(String title);
	
	public Ticket findById(int id);
	
	public List<Ticket> findByUser(User user);
	
	public List<Ticket> findByStatusTicket(String statusTicket);
	
	public List<Ticket> findByCategories(Category category);
	

}
