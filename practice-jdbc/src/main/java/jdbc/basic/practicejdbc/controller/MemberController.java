package jdbc.basic.practicejdbc.controller;

import jdbc.basic.practicejdbc.domain.Member;
import jdbc.basic.practicejdbc.repository.MemberRepositoryJdbc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {
	private final MemberRepositoryJdbc memberRepositoryJdbc;

	@GetMapping(value = "/members/new")
	public String createForm() {
		return "members/createMemberForm";
	}

	@PostMapping(value = "/members/new")
	public String create(MemberForm form) {
		Member member = new Member();
		member.setName(form.getName());
		memberRepositoryJdbc.save(member);
		return "redirect:/";
	}

	@GetMapping(value = "/members")
	public String list(Model model) {
		List<Member> members = memberRepositoryJdbc.findAll();
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
