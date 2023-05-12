package com.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member extends BaseEntity{

	
	@Id
	@Column(name="member_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;

	private String picture;
	
	private String address;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	private String provider;
	private String providerId;
	
	// 오늘 여기 추가해보는중
	

	@Builder
	public Member(String name, String email,Role role,String address,String picture, String password, 
			PasswordEncoder passwordEncoder, String provider, String providerId){
		this.name = name;
		this.email = email;
		this.address=address;
		this.role = role;
		this.picture=picture;
		this.password=password;
		this.providerId = providerId;
		this.provider = provider;
	}

	public static Member createMember(MemberFormDto memberFormDto,
									  PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setName(memberFormDto.getName());
		member.setEmail(memberFormDto.getEmail());
		member.setAddress(memberFormDto.getAddress());
		String password = passwordEncoder.encode(memberFormDto.getPassword());
		member.setPassword(password);
		member.setRole(Role.ADMIN);

		return member;
	}

	public Member update(String name, String picture){
		this.name = name;
		this.picture=picture;
		return this;
	}


	public String getRoleKey() {
		return this.role.getKey();
	}

	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
}






