package it.platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Note {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="data", nullable=false)
	private LocalDate date;
	
	
	@NotBlank(message="mettere dettaglio note")
	@Size(min=1,max=60000)
	@Column(name="dettaglio", nullable=false)	
	private String description;
	
	
	@NotNull
	@Column(name="autore", nullable=false)
	private String author;
	


	//collegamento ManyToOne ticket
	@ManyToOne
	@JoinColumn(name="ticket_id", nullable=false)
	@JsonBackReference
	private Ticket ticket;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public Ticket getTicket() {
		return ticket;
	}


	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}


	@Override
	public String toString() {
		return "Note [date=" + date + ", description=" + description + ", author=" + author + "]";
	}

	

}
