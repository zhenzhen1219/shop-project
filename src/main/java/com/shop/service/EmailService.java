package com.shop.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
	 private final JavaMailSender javaMailSender;

	    //인증번호 생성
	    private final String ePw = createKey();
	    private final String eePw = createPw();
	    String joinPw = null;
	    
	    public MimeMessage createMessage(String to)throws MessagingException, UnsupportedEncodingException {
	        log.info("보내는 대상 : "+ to);
	        log.info("인증 번호 : " + ePw);
	        MimeMessage  message = javaMailSender.createMimeMessage();

	        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
	        message.setSubject("인증 번호: "); //메일 제목
	        message.setText("이메일 인증코드 : " + ePw);
	        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
//	        String msg="";
//	        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
//	        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
//	        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
//	        msg += ePw;
//	        msg += "</td></tr></tbody></table></div>";

//	        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
	        message.setFrom(new InternetAddress("","prac_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름
	        System.out.println("-----------------------");
	        
	        return message;
	    }
	    
	    
	    public MimeMessage createMessageJoin(String to)throws MessagingException, UnsupportedEncodingException {
	    	joinPw = createKey();
	        log.info("보내는 대상 : "+ to);
	        log.info("인증 번호 : " + joinPw);
	        MimeMessage  message = javaMailSender.createMimeMessage();

	        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
	        message.setSubject("회원가입 인증 번호: "); //메일 제목
	        message.setText("이메일 인증코드 : " + joinPw);
	        message.setFrom(new InternetAddress("","prac_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름
	        System.out.println("-----------------------");
	        
	        return message;
	    }
	    
	    
	    // 패스워드 이메일로 보내주기
	    public MimeMessage createMessage2(String to)throws MessagingException, UnsupportedEncodingException {
	        log.info("보내는 대상 : "+ to);
	        log.info("인증 번호 : " + eePw);
	        MimeMessage  message = javaMailSender.createMimeMessage();

	        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
	        message.setSubject("임시비밀번호입니다. "); //메일 제목
	        message.setText("임시비밀번호 : " + eePw);
	        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
//	        String msg="";
//	        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
//	        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
//	        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
//	        msg += ePw;
//	        msg += "</td></tr></tbody></table></div>";

//	        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
	        message.setFrom(new InternetAddress("","prac_Admin")); //보내는 사람의 메일 주소, 보내는 사람 이름
	        System.out.println("-----------------------");
	        
	        return message;
	    }
	    
	    
	    
	    

	    // 인증코드 만들기
	    public static String createKey() {
	        StringBuffer key = new StringBuffer();
	        Random rnd = new Random();

	        for (int i = 0; i < 6; i++) { // 인증코드 6자리
	            key.append((rnd.nextInt(10)));
	        }
	        return key.toString();
	    }
	    
	    
	    // 임시비밀번호 만들어주기
	    
	    public static String createPw() {
	        
	        Random rnd = new Random();
	        String pw = "";
	        for (int i = 0; i < 12; i++) {
				pw += (char) ((Math.random() * 26) + 97);
			}
	        return pw;
	    }

	    /*
	        메일 발송
	        sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
	        MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
	        bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
	     */
	    public String sendSimpleMessage(String to)throws Exception {
	    	System.out.println(to);
	        try{
	        	MimeMessage message = createMessage(to);
	            javaMailSender.send(message); // 메일 발송
	        }catch(MailException es){
	            es.printStackTrace();
	            throw new IllegalArgumentException();
	        }
	        return ePw; // 메일로 보냈던 인증 코드를 서버로 리턴
	    }
	    
	    
	    public String sendSimpleMessage2(String to)throws Exception {
	    	System.out.println(to);
	        try{
	        	MimeMessage message = createMessage2(to);
	            javaMailSender.send(message); // 메일 발송
	        }catch(MailException es){
	            es.printStackTrace();
	            throw new IllegalArgumentException();
	        }
	        return eePw; // 메일로 보냈던 인증 코드를 서버로 리턴
	    }
	    
	    
	    public String sendSimpleMessageJoin(String to)throws Exception {
	    	System.out.println(to);
	        try{
	        	MimeMessage message = createMessageJoin(to);
	            javaMailSender.send(message); // 메일 발송
	        }catch(MailException es){
	            es.printStackTrace();
	            throw new IllegalArgumentException();
	        }
	        return joinPw; // 메일로 보냈던 인증 코드를 서버로 리턴
	    }
}
