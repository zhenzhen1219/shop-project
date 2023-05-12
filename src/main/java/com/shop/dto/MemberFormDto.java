package com.shop.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.shop.constant.Role;
import com.shop.entity.Member;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class MemberFormDto {

	Long id;
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	
//	
//	@NotBlank(message = "주민등록은 필수 입력 값입니다.")
//	private String jumin1;
//	
//	@NotBlank(message = "주민등록은 필수 입력 값입니다.")
//	private String jumin2;
	
	
	@NotEmpty(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;
	
	@NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
	@Length(min=4, max=16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	private String password;
	
	@NotEmpty(message = "주소는 필수 입력 값입니다.")
	private String address;

	private String picture;

	private Role role;



	private static ModelMapper modelMapper = new ModelMapper();

	public static MemberFormDto of(Member member){
		return modelMapper.map(member, MemberFormDto.class);
	}
	
}
