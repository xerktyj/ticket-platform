package it.platform.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "La categoria è obbligatoria")
	@Column(name = "categoria", nullable = false)
	private String name;
	
	//collegamento many to many con i tickets
	@ManyToMany(mappedBy="categories")
	@JsonBackReference
	private List<Ticket> tickets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return name;
	}

	public void setCategory(String category) {
		this.name = category;
	}

	@Override
	public String toString() {
		return  name ;
	}
	
	

}
