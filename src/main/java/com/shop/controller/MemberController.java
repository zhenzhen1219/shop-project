package com.shop.controller;

import java.security.Principal;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberSearchDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;

@RequestMapping("/members")
@Controller
public class MemberController {

	@Autowired
	MemberService memberService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/new")
	public String memberForm(Model model) {
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}

	@PostMapping("/new")
	public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return "member/memberForm";
		}

		try {
			Member member = Member.createMember(memberFormDto, passwordEncoder);
			memberService.saveMember(member);
			// 중복회원이 발생했을때
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}

		return "redirect:/"; // main 페이지로 이동

	}

//	@ResponseBody
	@GetMapping("/login")
	public String loginMember() {
		return "member/memberLoginForm";
	}

	@GetMapping(value = { "/members", "/members/{page}" })
	public String memberList(MemberSearchDto memberSearchDto, @PathVariable("page") Optional<Integer> page,
			Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

		Page<Member> members = memberService.getMemberListPage(memberSearchDto, pageable);
		model.addAttribute("members", members);
		model.addAttribute("memberSearchDto", memberSearchDto);
		model.addAttribute("maxPage", 100);
		return "member/memberList";
	}

	// 관리자가 보는 회원 정보 -> 일반 이용자의 정보 조회 밎 삭제
	@GetMapping(value = "/member/{memberId}")
	public String memberDtl(@PathVariable("memberId") Long memberId, Model model) {
		try {
			MemberFormDto memberFormDto = memberService.getMemberDtl(memberId);
			model.addAttribute("memberFormDto", memberFormDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMessage", "존재하지 않는 회원 입니다.");
			model.addAttribute("memberFormDto", new MemberFormDto());
			return "member/adminMemberUpdateForm";
		}
		return "member/adminMemberUpdateForm";
	}

	// 일반 및 관리자의 본인 정보 조회 및 수정과 삭제

	@GetMapping("/myPage")
	public String myPage(Model model, Principal principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		MemberFormDto memberFormDto = memberService.getMemberByEmail(email);
		model.addAttribute("memberFormDto", memberFormDto);
		return "member/myPage";
	}

	@PostMapping("/member/update/{id}")
	public String updateMember(@PathVariable("id") Long id, @Valid MemberFormDto memberFormDto,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "member/adminMemberUpdateForm";
		}
		Member member = memberService.findById(id);

		// 수정된 정보 업데이트
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
		member.setAddress(memberFormDto.getAddress());

		// 변경된 회원 정보 저장
		memberService.save(member);

		return "redirect:/";
	}

	@PostMapping("/member/delete/{id}")
	public String deleteMember(@PathVariable("id") Long id, HttpSession session, Authentication authentication) {

		Member member = memberService.findById(id);
		// 현재 인증된 사용자와 삭제 대상이 일치할 때 세션을 무효화 시켜 로그아웃 처리
		if (authentication.getName().equals(member.getEmail())) {
			memberService.deleteById(id);
			session.invalidate();
		} else { // 다른 사람을 삭제하는 경우, 세션은 유지한 상태로 삭제 처리만 진행
			memberService.deleteById(id);
		}

		return "redirect:/";
	}

	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
		return "/member/memberLoginForm";
	}

	
	@GetMapping("/nameCheck")
	public String idCheck(Model model) {
		return "member/memberNameJuminCheck";
	}

	@GetMapping("/idSerch")
	public String idSerch(Model model, Principal principal) {
		return "member/memberSerch";
	}
	
	@PostMapping("/check")
	// @RequestParam
	public String memberCheck1(@RequestParam String name, Model model) throws Exception {
		System.out.println("제대로 실행되는지 체크하는 구간" + name);
		boolean check = memberService.validateDuplicateMemberCheck(name);
		System.out.println("check1" + check);
		if (check == false) {
			model.addAttribute("errorMessage", "존재하지 않는 회원 입니다.");
			return "/member/memberNameJuminCheck";
		}
		System.out.println("check2" + check);
		model.addAttribute("errorMessage", "인증을 받을 이메일을 작성해주세요.");
		model.addAttribute("name", name);
		return "member/memberSerch";
	}
	
	
	// 비번찾기 email, id 체크해주고 메일로 임시비밀번호 보내주기
	@GetMapping("/EmailNameCheck")
	public String EmailNameCheck(Model model) {
		return "member/memberEmailNameCheck";
	}
	
	
	@GetMapping("/passCheckForm")
	   public String passCheckForm(Model model) {
	      
	      return "member/passCheckForm";
	   }
	   
	   @PostMapping("/passCheckForm")
	   public String passCheck(@RequestParam("password") String password, 
	            HttpSession session, Authentication authentication, 
	            HttpServletRequest request, Model model) {
	      
	      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      String id = auth.getName(); // 로그인한 사용자의 아이디를 가져옴
	      
	      Member member = memberService.findByEmail(id);
	      String pw = member.getPassword();
	      
	      if(passwordEncoder.matches(password, pw)) {
	         return "redirect:/members/myPage";
	      } else {
	         model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
	         return "member/passCheckForm";
	      }
	   }
	
	

	
	
	

}
