package it.platform.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import it.platform.model.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User,Integer>{
	
	public Optional<User> findByUsername(String username);
	
    public	List<User> findByActiveState(boolean activeState);
	
	public User findByEmail(String email);
	
	public User findById(int id);
	

}
