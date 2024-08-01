package it.platform.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import it.platform.model.Category;
import it.platform.repository.CategoryRepository;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/categories")
public class CategoryController {
	
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	//BLOCCO  USER CON SECURITY
	@GetMapping("/list")
	public String listCategory(Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		 List<Category>	categoriesList = categoryRepo.findAll();

		model.addAttribute("categoriesList", categoriesList);
		return "category/category_list";
	}
	
	@GetMapping("/datacategory/{id}")
	public String dataCategory(@PathVariable int id, Model m) {	
		m.addAttribute("category", categoryRepo.findById(id));
		return "category/data_category";
	}
	
	
	@GetMapping("/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("addMode", true);
		model.addAttribute("category", new Category());
		return "/category/add_edit_category";
	}
	
	@PostMapping("/addcategory")
	public String storeCategory(@Valid @ModelAttribute("category") Category formCategory, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", true);
		
		if (bindingResult.hasErrors()) {
			return "/category/add_edit_category";
		}
		
		categoryRepo.save(formCategory);
		return "category/data_category";
	}
	
	@GetMapping("/editcategory/{id}")
	public String editCategory(@PathVariable int id, Model m) {
		m.addAttribute("addMode",false);
		m.addAttribute("category", categoryRepo.findById(id));
		return "category/add_edit_category";
	}
	
	@PostMapping("/editcategory/{id}")
	public String storeEditCategory(@Valid @ModelAttribute("category") Category formCategory, BindingResult bindingResult, Model model) {
		model.addAttribute("addMode", false);		
		if (bindingResult.hasErrors()) {
			return "category/add_edit_category";
		}
		categoryRepo.save(formCategory);
		return "category/data_category";
	}
	
	@PostMapping("/deletecategory/{id}")
	public String deleteCategory(@PathVariable int id, Model model) {
			categoryRepo.deleteById(id);
			model.addAttribute("deleteCorrect", true);		
		return "category/data_category";
	}

	
	

}
