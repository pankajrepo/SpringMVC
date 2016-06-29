package com.example.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.User;
import com.example.service.UserService;
import com.example.validator.UserValidator;

@Controller
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static Integer id=0;

	@Autowired
	private UserService userService;

	@Autowired
	UserValidator userFormValidator;
	
	//Set a form validator
		@InitBinder
		protected void initBinder(WebDataBinder binder) {
			binder.setValidator(userFormValidator);
		}
	
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String index(Model model) {
		logger.debug("index()");
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/users/userForm", method = RequestMethod.GET)
	public String userForm(Model model) {
		logger.debug("index()");
		populateDefaultModel(model);
		model.addAttribute("userForm", new User());
		return "users/userForm";
	}
	
	// list page
		@RequestMapping(value = "/users", method = RequestMethod.GET)
		public String showAllUsers(Model model) {

			logger.debug("showAllUsers()");
			model.addAttribute("users", userService.findAll());
			return "users/list";

		}
		
		// save or update user
		// 1. @ModelAttribute bind form value
		// 2. @Validated form validator
		// 3. RedirectAttributes for flash value
		@RequestMapping(value = "/users", method = RequestMethod.POST)
		public String saveOrUpdateUser(@ModelAttribute("userForm") @Validated User user,
				BindingResult result, Model model, 
				final RedirectAttributes redirectAttributes) {
			
			
			logger.debug("saveOrUpdateUser() : {}", user);

			if (result.hasErrors()) {
				populateDefaultModel(model);
				return "users/userform";
			} else {

				// Add message to flash scope
				redirectAttributes.addFlashAttribute("css", "success");
				if(user.isNew()){
				  redirectAttributes.addFlashAttribute("msg", "User added successfully!");
				}else{
				  redirectAttributes.addFlashAttribute("msg", "User updated successfully!");
				}
				 id++;
				 user.setId(id);
				userService.saveOrUpdate(user);
				
				// POST/REDIRECT/GET
				return "redirect:/users/" + user.getId();

				// POST/FORWARD/GET
				// return "user/list";

			}

		}
		
		@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
		public String showUser(@PathVariable("id") int id, Model model) {

			logger.debug("showUser() id: {}", id);

			User user = userService.findById(id);
			if (user == null) {
				model.addAttribute("css", "danger");
				model.addAttribute("msg", "User not found");
			}
			model.addAttribute("user", user);

			return "users/show";

		}
		
		private void populateDefaultModel(Model model) {

			List<String> frameworksList = new ArrayList<String>();
			frameworksList.add("Spring MVC");
			frameworksList.add("Struts 2");
			frameworksList.add("JSF 2");
			frameworksList.add("GWT");
			frameworksList.add("Play");
			frameworksList.add("Apache Wicket");
			model.addAttribute("frameworkList", frameworksList);

			Map<String, String> skill = new LinkedHashMap<String, String>();
			skill.put("Hibernate", "Hibernate");
			skill.put("Spring", "Spring");
			skill.put("Struts", "Struts");
			skill.put("Groovy", "Groovy");
			skill.put("Grails", "Grails");
			model.addAttribute("javaSkillList", skill);

			List<Integer> numbers = new ArrayList<Integer>();
			numbers.add(1);
			numbers.add(2);
			numbers.add(3);
			numbers.add(4);
			numbers.add(5);
			model.addAttribute("numberList", numbers);

			Map<String, String> country = new LinkedHashMap<String, String>();
			country.put("US", "United Stated");
			country.put("CN", "China");
			country.put("SG", "Singapore");
			country.put("MY", "Malaysia");
			model.addAttribute("countryList", country);

		}


}
