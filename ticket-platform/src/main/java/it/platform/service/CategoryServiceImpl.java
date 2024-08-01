package it.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.platform.model.Category;
import it.platform.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	public Category findByName(String name) {
		return categoryRepo.findByName(name);
	}

}
