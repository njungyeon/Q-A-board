package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.slipp.repo.Answer;
import net.slipp.repo.AnswerRepository;
import net.slipp.repo.Question;
import net.slipp.repo.QuestionRepository;
import net.slipp.repo.Result;
import net.slipp.repo.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")  //answer는 무조건 question에 종속되기 때문에 이런식으로 설계하면 좋다
public class ApiAnswerController {

	@Autowired
	private AnswerRepository answerRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@PostMapping("")
	public Answer create(@PathVariable Long questionId, String contents, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findById(questionId).get();
		Answer answer = new Answer(loginUser, question, contents);
		
		question.addAnswer();
		questionRepository.save(question);
		//answerRepository.save(answer);
		return answerRepository.save(answer);
		//String.format("redirect:/questions/%d", questionId);
	}
		
	@GetMapping("/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인해야합니다."); 
		}
		Answer answer = answerRepository.findById(id).get();
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!answer.isSameWriter(loginUser)) {
			return Result.fail("자신의 글만 삭제할 수 있습니다.");
		}
		
		answerRepository.deleteById(id);
		Question question = questionRepository.findById(questionId).get();
		question.deleteAnswer();
		questionRepository.save(question);
		
		return Result.ok();
	}
}
