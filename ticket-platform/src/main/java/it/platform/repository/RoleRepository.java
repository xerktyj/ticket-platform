package it.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.platform.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	

}
