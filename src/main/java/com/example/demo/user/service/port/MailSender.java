package com.example.demo.user.service.port;

public interface MailSender {

	void sendMail(String email, String title, String content);
}
