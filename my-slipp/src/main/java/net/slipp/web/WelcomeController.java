package net.slipp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import net.slipp.repo.Question;
import net.slipp.repo.QuestionRepository;

@Controller
public class WelcomeController {
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/helloworld")
	public String welcome(String name, Model model) {
		System.out.println("name : " + name);
		model.addAttribute("name", name);
		return "welcome";
	}
	
	@GetMapping("")
	public String home(Model model){
		//Question questions = (Question) questionRepository.findAll();
		//이건 왜 안될까?
		//model.addAttribute("questions", questions);
		
		model.addAttribute("questions", questionRepository.findAll());
		return "index";
	}
}
