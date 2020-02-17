package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.repo.User;
import net.slipp.repo.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//private List<User> users = new ArrayList<User>();
	
//	@PostMapping("/create")
//	public String create(String userId, String password, String name, String email) {
//		
//		return "index";
//	}
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/loginForm")
	public String loginForm(){
		return "user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		User user = userRepository.findByUserId(userId);
		if(user == null) {
			System.out.println("login fail1");
			return "redirect:/user/loginForm";
		}
		
//		if(!password.equals(user.getPassword())) {
//			System.out.println("login fail2");
//			return "redirect:/user/loginForm";
//		}
		//get으로 password를 꺼내는 것보다 아래처럼 메세지를 통해서 같은지 확인하는 코드가 더 안전하다
		
		if(!user.matchPassword(password)) {
			System.out.println("login fail2");
			return "redirect:/user/loginForm";
		}
		
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		System.out.println("login success");
		
		return "redirect:/";
		
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}
	
	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@PostMapping("/create")
	public String create(User user) {
		System.out.println("user: " + user);
		//users.add(user);
		userRepository.save(user);
		return "redirect:/user/loginForm";
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		
		return "/user/list";
	}
	
	@GetMapping("/{id}/form")
	public String updateform(@PathVariable Long id, Model model, HttpSession session) {
		//User user = userRepository.getOne(id); // 원래는 findone 이엇는데 이거로 대체함 없어짐..;
		
		//Object tempUser = session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/user/loginForm";
		}
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if(!sessionedUser.matchId(id)) { // id값을 직접 꺼내는게 아니고 같은지 확인하는 메세지만 받는다. -> 안전!
			throw new IllegalStateException("you cant update the another user");
		}
		User user = userRepository.findById(id).get();
		//user.id = id;
		model.addAttribute("user", user);
		System.out.println(user);
		return "/user/updateForm";
		//return "/user/list";
	}
	
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/user/loginForm";
		}
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		User user = userRepository.findById(id).get();
		user.update(updatedUser);
		userRepository.save(user);
		System.out.println(id);
		return "redirect:/user/list";
	}
}
