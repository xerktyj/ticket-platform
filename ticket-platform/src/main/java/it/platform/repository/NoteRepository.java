package it.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.platform.model.Note;
import it.platform.model.Ticket;

public interface NoteRepository extends JpaRepository<Note, Integer>{
	
	public List<Note> findByTicket(Ticket ticket);
	
	public Note findById(int id);

}
