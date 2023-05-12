package com.shop.config;

import com.shop.constant.Role;
import com.shop.service.CustomOAuth2UserService;
import com.shop.service.MemberService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MemberService memberService;
	@Autowired
	private final CustomOAuth2UserService customOAuth2UserService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable() // 수진 추가함
		 		
		 http.formLogin()
					.loginPage("/members/login")
					.defaultSuccessUrl("/",true)
					.usernameParameter("email")
					.failureUrl("/members/login/error")
					
					
				.and()
					.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID");
				http.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
					
					
				.and()
				
				.oauth2Login()
					
					.userInfoEndpoint() // oauth2 로그인 성공 후 가져올 때의 설정들
					
					// 소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
					.userService(customOAuth2UserService);// 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
					
						

				// http.oauth2Login().loginPage("/members/login");
				 http.authorizeRequests()
				// 메인페이지, 회원 관련 url, 상품 관련 페이지, 상품 이미지는
				// 인증(로그인)없이 해당 경로에 접근할 수 있도록 permitAll()사용
				.mvcMatchers("/","/members/**", "/item/**","/images/**","/font/**","/eshopper/**").permitAll()
				.antMatchers("/api/v1/**","/api/v2/**").hasRole(Role.USER.name()) // /api/v1/** 은 USER권한만 접근 가능
				.mvcMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated();

		http.exceptionHandling()
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint());


	}

	
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/error", "/eshopper/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	

	

}
