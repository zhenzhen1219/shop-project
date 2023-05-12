package com.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class MemberServiceTest {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public Member createMember() {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail("test@email.com");
		memberFormDto.setName("홍길동");
		memberFormDto.setAddress("서울시 마포구 합정동");
		memberFormDto.setPassword("1234");
		
		return Member.createMember(memberFormDto, passwordEncoder);
	}
	
//	@Test
//	@DisplayName("회원가입 테스트")
//	public void saveMemberTest() {
//		Member member = createMember();
//		Member saveMember = memberService.saveMember(member);
//		
//		// assertEquals() - 첫번째 파라미터 : 기대값, 두번째 파라미터 : 실제로 저장된 값
//		// 				  - 파라미터의 두값을 비교하기 위해 사용
//		assertEquals(member.getEmail(), saveMember.getEmail());
//		assertEquals(member.getName(), saveMember.getName());
//		assertEquals(member.getAddress(), saveMember.getAddress());
//		assertEquals(member.getPassword(), saveMember.getPassword());
//		assertEquals(member.getRole(), saveMember.getRole());
//		
//	}
	
	@Test
	@DisplayName("회원가입 테스트")
	public void saveDuplicateMemberTest() {
		Member member1 = createMember();
		Member member2 = createMember();
		memberService.saveMember(member1);
		
		Throwable e = assertThrows(IllegalStateException.class, () ->{
			memberService.saveMember(member2);
		});
		
		assertEquals("이미 가입된 회원입니다.", e.getMessage());
		
	}
	

}
