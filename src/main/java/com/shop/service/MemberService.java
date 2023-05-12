package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
 * UserDetailsService - 데이터베이스에서 회원 정보를 가져오는 역할을 담당
 * - loadUserByUsername() 메소드로 회원 정보를 조회하여 
 *   사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환
 * - 스프링 시큐리티에서 UserDetailService를 구현하고 있는 클래스를 통해 로그인 기능을 구현한다고 
 *   생각하면 됨. 
 */

@Service
@Transactional
@RequiredArgsConstructor
//public class MemberService implements UserDetailsService{
public class MemberService {

	// 빈을 주입하는 방법
	// 1. @Autowired 어노테이션 이용
	// 2. 필드 주입(Setter 주입)
	// 3. 생성자 주입(빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하면
	// @Autowired 없이 의존성 주입 가능)
	@Autowired
	MemberRepository memberRepository;

	// 회원 가입
	public Member saveMember(Member member) {
		validateDuplicateMember(member);

		return memberRepository.save(member);
	}

	// 회원 정보 저장
	public void save(Member member) {
		memberRepository.save(member);
	}

	// 회원 ID로 회원 정보 조회
	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
	}

	// 회원 삭제
	public void deleteById(Long id) {
		memberRepository.deleteById(id);
	}

	// 중복된 회원인지 check
	private void validateDuplicateMember(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}

	// 회원 정보 조회
	@Transactional(readOnly = true)
	public MemberFormDto getMemberDtl(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
		MemberFormDto memberFormDto = MemberFormDto.of(member);
		return memberFormDto;
	}

	// 로그인누르면 여기서 처리
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		Member member = memberRepository.findByEmail(email);
//
//		System.out.println("-------MemberService------");
//		System.out.println(member.getEmail());
//		System.out.println("-------일반로그인이랑 소셜로그인 구분하는지 체크해보기------");
//		
//		return User.builder()
//				.username(member.getEmail())
//				.password(member.getPassword())
//				.roles(member.getRole().toString())
//				.build();
//		
//	}

	public Member getMemberByUsername(String name) {
		return memberRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public Page<Member> getMemberListPage(MemberSearchDto memberSearchDto, Pageable pageable) {
		return memberRepository.getMemberPage(memberSearchDto, pageable);
	}

	public MemberFormDto getMemberByEmail(String email) {
		return memberRepository.getMemberByEmail(email);
	}

	// 아이디찾을때 이름값으로 찾음 나중에 주민등록번호도 추가할수있으면 해서 조회하기,,,
	public boolean validateDuplicateMemberCheck(String name) {
		boolean check = true;
		Member findMember = memberRepository.findByName(name);
		if (findMember == null) {
			check = false;
			// throw new IllegalStateException("회원이 존재하지 않습니다.");
		}

		return check;
	}
	
	public void updatePw(String email, String pw) {
		System.out.println("비밀번호 업데이트할려는 구간입니다.");
		System.out.println(email);
		System.out.println(pw);
		memberRepository.updatePw(email, pw);
	}
	


	// 이름 조회한뒤 아이디 알려주는 구간

	public String getMemberByEmail2(String name) {
		return memberRepository.getMemberId(name);
	}
	
	
	// 비밀번호찾기 - 이름 아이디 조회해서 값 리턴해주기 
	public boolean validateDuplicateMemberCheck2(String name, String email) {
		boolean check = true;
		String id = memberRepository.getMemberIdName(name, email);
		if (id == null) {
			check = false;
			// throw new IllegalStateException("회원이 존재하지 않습니다.");
		}

		return check;
	}
	
	   public Member findByEmail(String email) {
		      return memberRepository.findByEmail(email);
		   }

}
