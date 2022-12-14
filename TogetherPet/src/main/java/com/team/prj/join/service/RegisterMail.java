package com.team.prj.join.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegisterMail implements MailServiceInter{

	@Autowired
	JavaMailSender emailSender;
	
	private String ePw = createKey();
	
	// 메일 내용 작성
	@Override
	public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
	       System.out.println("보내는 대상 : "+ to);
	       System.out.println("인증 번호 : "+ ePw);
	        MimeMessage  message = emailSender.createMimeMessage();
	 
	        message.addRecipients(RecipientType.TO, to);// 보내는 사람
	        message.setSubject("TogetherPet 인증 코드입니다.");// 메일 제목
	 
	        String msgg="";
	        msgg+= "<div style='margin:100px;'>";
	        msgg+= "<h1> 안녕하세요. TogetherPet입니다. </h1>";
	        msgg+= "<br>";
	        msgg+= "<p>아래 코드를 인증코드 확인란에 입력해주세요.<p>";
	        msgg+= "<br>";
	        msgg+= "<p>감사합니다.<p>";
	        msgg+= "<br>";
	        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
	        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
	        msgg+= "<div style='font-size:130%'>";
	        msgg+= "CODE : <strong>";
	        msgg+= ePw+"</strong><div><br/> ";
	        msgg+= "</div>";
	        message.setText(msgg, "utf-8", "html");// 내용
	        message.setFrom(new InternetAddress("hyee1021@naver.com","TogetherPet_ADMIN"));// 보내는 사람
	 
	        return message;
	}
	
	// 랜덤 인증 코드
	@Override
	public String createKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();
		
		for(int i=0; i<8; i++) { // 인증코드 8자리
			int index=rnd.nextInt(3); // 0~2까지 랜덤, rnd값에 따라서 아래 switch문 실행됨
			
			switch(index) {
			case 0:
				key.append((char) ((int) (rnd.nextInt(26)) + 97)); // 소문자
				break;
			case 1:
				key.append((char) ((int) (rnd.nextInt(26)) + 65)); // 대문자
				break;
			case 2:
				key.append((rnd.nextInt(10))); // 숫자
				break;
			}
		}
		
		return key.toString();
	}
	
	
	// 메일 발송 (javaMail 객체 이용)
	@Override
	public String sendSimpleMessage(String to) throws Exception {
		ePw = createKey(); // 랜덤 인증번호 생성
		
		MimeMessage message = createMessage(to); //  message = 전송할 메일 내용
		try {
			emailSender.send(message);
		}catch(MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
		return ePw; // 메일로 보냈던 인증코드를 서버로 반환
	}
}
