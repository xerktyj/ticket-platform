package it.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	public Category findById(int id);
	
	public Category findByName(String name);
	
}
