package it.platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@NotBlank(message="mettere una username non già utilizzata")
	@Size(min=1,max=100)
	@Column(name="username", nullable=false)
	private String username;
	
	@NotNull
	@NotBlank(message="mettere una password")
	@Size(min=1,max=100)
	@Column(name="password", nullable=false)
	private String password;
	
	@NotNull
	@NotBlank(message="mettere una password")
	@Size(min=1,max=100)
	@Email
	@Column(name="email", nullable=false)
	private String email;

	@NotNull
	@NotBlank(message="mettere il nome")
	@Size(min=1,max=100)
	@Column(name="nome", nullable=false)
	private String name;
	
	@NotNull
	@NotBlank(message="mettere il cognome")
	@Size(min=1,max=100)
	@Column(name="cognome", nullable=false)
	private String surname;
	
	
	@NotNull
	@Column(name="stato attivita", nullable=false)
	private boolean activeState;
	
    //collegamento ManyToOne con i ruoli
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;
	
	//collegamento OneToMany con ticket
    @OneToMany(mappedBy = "user")
	@JsonManagedReference
	@JsonIgnore
	private List<Ticket> tickets;
	
	
	
		

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public boolean isActiveState() {
		return activeState;
	}

	public void setActiveState(boolean activeState) {
		this.activeState = activeState;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", name=" + name
				+ ", surname=" + surname + ", activeState=" + activeState + "]";
	}
	
	

}
