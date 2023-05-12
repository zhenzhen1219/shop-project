package com.shop.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// ajax 요청에 따른 처리
		// XMLHttpRequest 값이 세팅되어 요청이 올때
		// 인증되지 않은 사용자가 요청할 경우 "Unauthorized" 에러 발생시킴
		// 나머지 경우는 로그인 페이지 리다이렉트
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			// "Unauthorized" 에러발생
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
		}else {
			// 로그인 페이지로 리다이렉트
			response.sendRedirect("/members/login");
		}
	}

}
