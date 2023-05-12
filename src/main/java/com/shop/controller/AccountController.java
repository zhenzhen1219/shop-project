package com.shop.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shop.dto.CartDetailDto;
import com.shop.service.EmailService;
import com.shop.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AccountController {

	private final MemberService memberService;
	private final EmailService emailService;
	@Autowired
	PasswordEncoder passwordEncoder;
	String recode;
	String checkName;
	
	@PostMapping("/members/login/mailConfirm")
	// @ResponseBody
	public String mailConfirm(@RequestParam String email, @RequestParam String name) throws Exception {
		recode = emailService.sendSimpleMessage(email);
		checkName = name;
		System.out.println("recode----" + recode);
		log.info("인증코드 : " + recode);

		return "member/memberCheckCode";
	}
	
	
	

	@PostMapping("/members/join/mailConfirm")
	public ResponseEntity joinMailConfirm(@RequestParam String email) throws Exception {
		String code = emailService.sendSimpleMessageJoin(email);
		
		System.out.println("code----" + code);
		log.info("회원가입 인증코드 : " + code);
	
		
		return new ResponseEntity<String>(code, HttpStatus.OK);
		
	}
	
	
	
	
	
	


	@PostMapping("/members/codeCheck")
	// @RequestParam
	public String memberCheck(@RequestParam String code, Model model) throws Exception {
		System.out.println("제대로 실행되는지 코드 체크하는 구간" + code);

		System.out.println("code1" + code);
		if (!code.equals(recode)) {
			model.addAttribute("errorMessage", "코드가 틀립니다.");
			return "member/memberCheckCode";
		}
		
		System.out.println("code2" + code);
		String id = memberService.getMemberByEmail2(checkName);
		model.addAttribute("errorMessage", "회원 아이디는[ "+id+" ]입니다.");
		model.addAttribute("url", "/login");
		return "member/memberCheckCode";
	}
	
	
	@PostMapping("/members/SeachUserIdName")
	public String memberCheck2(@RequestParam  String email,@RequestParam String name, Model model) throws Exception {
		System.out.println("id와 이름 비교해서 맞는지 체크하는 구간입니다.");

		boolean check = memberService.validateDuplicateMemberCheck2(name, email);
		
		if(check!=true) {
			model.addAttribute("errorMessage", "이메일(Id) 또는 이름이 잘못되었습니다.");
			System.out.println("check name, Id" + check);
			//model.addAttribute("url", "/members/EmailNameCheck");
			return "member/memberEmailNameCheck";
		}
		model.addAttribute("name", name);
		model.addAttribute("emailId", email);
		return "member/memberPwEmail";
	}
	
	
	// 이메일 주소받고 임시 비밀번호 보내주고 DB에서 임시비밀번호로 변경해주기
	@PostMapping("/members/emailPw")
	public String emailPw(@RequestParam String email, @RequestParam String emailId, Model model) throws Exception {
		System.out.println("emailId" + emailId);
		String pw1 = emailService.sendSimpleMessage2(email);
		String pw2 = passwordEncoder.encode(pw1);
		System.out.println(pw2);
		memberService.updatePw(emailId, pw2);
		model.addAttribute("errorMessage", "임시비밀번호를 메일로 보냈습니다.");
		return "member/memberLoginForm";
	}
	

}