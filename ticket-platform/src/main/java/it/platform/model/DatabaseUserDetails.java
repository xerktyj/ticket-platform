package it.platform.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails {
	
	private final Integer id;
	private final String username;
	private final String password;
	private final Set<GrantedAuthority> authorities;
	
	public DatabaseUserDetails(User user) {
	this.id = user.getId();
	this.username = user.getUsername();
	this.password = user.getPassword();
	
	
	authorities = new HashSet<GrantedAuthority>();
	for(Role role : user.getRoles()) {
	authorities.add(new SimpleGrantedAuthority(role.getName()));
	}
	
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

}
