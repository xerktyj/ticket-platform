package it.platform.model;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
public class Ticket {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="inizio data")
	private LocalDate startDate;


	@NotNull
	@FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="fine data", nullable=false)
	private LocalDate endDate;
	
	
	@NotBlank(message="mettere dettaglio ticket")
	@Size(min=1,max=100)
	@Column(name="titolo", nullable=false)	
	private String title;


	@NotBlank(message="mettere dettaglio ticket")
	@Size(min=1,max=60000)
	@Column(name="dettaglio", nullable=false)	
	private String description;
    
    
   
   	@NotBlank(message="mettere lo stato del ticket")
   	@Column(name="stato_ticket", nullable=false)	
   	private String statusTicket;
    
    @Min(0)
    @Column(name="ore_lavorate")
    private int timeWorked;	
    
    //collegamento con oneToMany note
    @OneToMany(mappedBy = "ticket")
    private List<Note> notes;

    //collegamento  ManyToOne con user
  	@ManyToOne
  	@JoinColumn(name="user_id",nullable=false)
  	@JsonBackReference
  	private User user;
	
	//collegamento con ManytoMany category
    @ManyToMany
	@JoinTable(
			name = "ticket_category",
			joinColumns = @JoinColumn(name = "ticket_id"),
			inverseJoinColumns = @JoinColumn(name = "category_id")
	)
    @JsonManagedReference
	private List<Category> categories;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public LocalDate getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


	public LocalDate getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getStatusTicket() {
		return statusTicket;
	}


	public void setStatusTicket(String statusTicket) {
		this.statusTicket = statusTicket;
	}


	public int getTimeWorked() {
		return timeWorked;
	}


	public void setTimeWorked(int timeWorked) {
		this.timeWorked = timeWorked;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public List<Category> getCategories() {
		return categories;
	}


	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}


	public List<Note> getNotes() {
		return notes;
	}


	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	


	@Override
	public String toString() {
		return "Ticket [startDate=" + startDate + ", endDate=" + endDate + ", title=" + title + ", description="
				+ description + ", statusTicket=" + statusTicket + ", timeWorked=" + timeWorked + ", notes=" + notes
				+ ", user=" + user + ", categories=" + categories + "]";
	}

	
	
}
