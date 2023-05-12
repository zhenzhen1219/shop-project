package com.shop.service;

import com.shop.constant.Role;
import com.shop.dto.OAuthAttributes;
import com.shop.dto.SessionMember;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import security_auuth2.config.auth.PrincipalDetails;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>, UserDetailsService {
	private final MemberRepository memberRepository;
	private final HttpSession httpSession;

	public CustomOAuth2UserService(MemberRepository memberRepository, HttpSession httpSession) {
		this.memberRepository = memberRepository;
		this.httpSession = httpSession;
	}

//	// 로그인 처리
//	@Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//        
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//
//        // OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//	// OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        	
//        	  
//            
//            System.out.println("userRequest = " + userRequest.getClientRegistration());
//            System.out.println("oAuth2User = " + userRequest.getAdditionalParameters());
//            System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
//            System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());
//            System.out.println("oAuth2User.getName() = " + oAuth2User.getName());
//          
//            
//            String provider = userRequest.getClientRegistration().getRegistrationId(); // google
//            String providerId = oAuth2User.getAttribute("sub");
//            //String providerId = oAuth2User.getAttribute("response");
//            String username = provider + "_" + providerId; // google_1923912312312
//            String email = oAuth2User.getAttribute("email");
//            
//            
//           
//            Member member = memberRepository.findByEmail(email);
//            if (member == null) {
//                System.out.println("최초 가입");
//                member = Member.builder()
//                        .name(email)
//                        .email(email)
//                        .role(Role.ADMIN.ADMIN)
//                        .provider(provider)
//                        .providerId(providerId)
//                        .build();
//                memberRepository.save(member);
//            }else {
//                System.out.println("이미 가입한 적이 있습니다!!!!!!!!!!!!!!!!!!!!!!!!!");
//            }
//          
//        	
//        	
//        
//        return new PrincipalDetails(member, oAuth2User.getAttributes());
//        
//    
//    }

	// 로그인 처리
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		String providerId;
		String provider;
		String username;
		String email;
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// OAuth2 로그인 시 키 값이 된다. 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다. 각각
		// 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();

		// OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
				oAuth2User.getAttributes());

		if ("naver".equals(registrationId)) {

			Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttribute("response");
			System.out.println(response.get("id"));

			System.out.println("Naver처리확인하기");
			providerId = response.get("id").toString();

			provider = userRequest.getClientRegistration().getRegistrationId(); // google
			System.out.println("provider 값 체크" + provider);
			username = response.get("name").toString();
			System.out.println("username 값 체크" + username);
			email = response.get("email").toString();

		} else {

			System.out.println("구글처리확인하기");
			providerId = oAuth2User.getAttribute("sub");

			provider = userRequest.getClientRegistration().getRegistrationId(); // google
			System.out.println("provider 값 체크" + provider);
			username = oAuth2User.getAttribute("name"); // google_1923912312312
			System.out.println("username 값 체크" + username);
			email = oAuth2User.getAttribute("email");

		}
		System.out.println(providerId);
		System.out.println("userRequest = " + userRequest.getClientRegistration());
		System.out.println("oAuth2User = " + userRequest.getAdditionalParameters());
		System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());
		System.out.println("oAuth2User.getAuthorities() = " + oAuth2User.getAuthorities());
		System.out.println("oAuth2User.getName() = " + oAuth2User.getName());

		Member member = memberRepository.findByEmail(email);
		if (member == null) {
			System.out.println("최초 가입");
			member = Member.builder().name(email).email(email)
					.role(Role.ADMIN)
					.provider(provider)
					.providerId(providerId)
					.build();
			memberRepository.save(member);
		} else {
			System.out.println("이미 가입한 적이 있습니다!!!!!!!!!!!!!!!!!!!!!!!!!");
		}

		return new PrincipalDetails(member, oAuth2User.getAttributes());

	}

	// 일반로그인처리
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);

		System.out.println("-------MemberService------");
		System.out.println(member.getEmail());
		System.out.println(member.getRole());
		System.out.println("-------일반로그인이랑 소셜로그인 구분하는지 체크해보기------");

		return User.builder().username(member.getEmail()).password(member.getPassword())
				.roles(member.getRole().toString()).build();

	}

}
