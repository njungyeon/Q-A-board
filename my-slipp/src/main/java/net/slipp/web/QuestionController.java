package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.repo.Question;
import net.slipp.repo.QuestionRepository;
import net.slipp.repo.User;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;

	@GetMapping("/form")
	public String form(HttpSession session, Model model) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/user/loginForm";
		}
		return "/qna/form";
	}
	
	@PostMapping("")
	public String create(String title, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return "/user/loginForm";
		}
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		Question question = new Question(sessionedUser, title, contents);
		//model.addAttribute("question", question);
		questionRepository.save(question);
		
		return "redirect:/";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute("question", questionRepository.findById(id).get());
		return "/qna/show";
	}
	
//	@GetMapping("/{id}/form")
//	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
//		
//		if(!HttpSessionUtils.isLoginUser(session)) {
//			return "/user/loginForm";
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		Question question = questionRepository.findById(id).get();
//		
//		if(!question.isSameWriter(loginUser)) {
//			return "/user/loginForm";
//			//css가 안먹혀서 절대경로로 설정했더니 해결됐다. => 그럼 그냥 처음부터 다 절대경로로 설정했으면 되는거 아닌가?
//			//추후에는 로그인 페이지가 아니고 그냥 오류창만 발생하도록 만들거나 아님 처음부터 수정 삭제 버튼이 안보이게 했으면 좋겠다.	
//		}
//		model.addAttribute("question", question);
//		return "/qna/updateForm";
//	}
	
	//현재 위의 함수와 다른 함수에서 if문으로 에러 처리하는 부분이 상당히 중복되고 있다. 
	// 이 부분의 코드들을 리팩토링 해보자
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		try {
			Question question = questionRepository.findById(id).get();
			hasPermission(session, question);
			model.addAttribute("question", question);
			return "/qna/updateForm";
		} catch(IllegalStateException e){
			model.addAttribute("errorMessage", e.getMessage());
			System.out.println("message: " + e.getMessage());
			return "/user/loginForm";
		}
//		if(!HttpSessionUtils.isLoginUser(session)) {
//			return "/user/loginForm";
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		Question question = questionRepository.findById(id).get();
//		
//		if(!question.isSameWriter(loginUser)) {
//			return "/user/loginForm";
//			
//		}
//		model.addAttribute("question", question);
//		return "/qna/updateForm";
	}
	private boolean hasPermission(HttpSession session, Question question) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			throw new IllegalStateException("로그인이 필요합니다.");
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!question.isSameWriter(loginUser)) {
			throw new IllegalStateException("자신의 글만 수정,삭제가 가능합니다.");
		}
		//이런식으로 throw를 통해서 에러 메세지를 던질 수도 있고 
		//아니면 클래스 하나를 만들어서 리턴할 수도 있다.
		return true;
	}
	
	@PostMapping("/update/{id}")
	public String update(@PathVariable Long id, String title, String contents, HttpSession session, Model model) {
//		if(!HttpSessionUtils.isLoginUser(session)) {
//			return "/user/loginForm";
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		Question question = questionRepository.findById(id).get();
//		
//		if(!question.isSameWriter(loginUser)) {
//			return "/user/loginForm";
//		}
//		question.update(title, contents);
//		questionRepository.save(question);
//		return String.format("redirect:/questions/%d", id);

		try {
			Question question = questionRepository.findById(id).get();
			hasPermission(session, question);
			question.update(title, contents);
			questionRepository.save(question);
			return String.format("redirect:/questions/%d", id);
		} catch(IllegalStateException e){
			model.addAttribute("errorMessage", e.getMessage());
			return "/user/loginForm";
		}
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
//		if(!HttpSessionUtils.isLoginUser(session)) {
//			return "/user/loginForm";
//		}
//		User loginUser = HttpSessionUtils.getUserFromSession(session);
//		Question question = questionRepository.findById(id).get();
//		
//		if(!question.isSameWriter(loginUser)) {
//			return "/user/loginForm";
//		}
		
//		questionRepository.deleteById(id);
//		System.out.println("here you are?");
//		return "redirect:/";
		
		try {
			Question question = questionRepository.findById(id).get();
			hasPermission(session, question);
			questionRepository.deleteById(id);
			return "redirect:/";
		} catch(IllegalStateException e){
			model.addAttribute("errorMessage", e.getMessage());
			return "/user/loginForm";
		}
	}
}
