package com.fixit.web.service;

import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailMessagingService implements MessagingService{

    @Value("${mailersend.api.key}")
    private String accessToken;
    @Value("${email.from.address}")
    private String fromAddress;
    @Value("${email.from.name}")
    private String fromName;
    @Override
    public void send(String contact, String subject, String body) {
        Email email = new Email();

        email.setFrom(fromName, fromAddress);
        email.addRecipient("", contact);
        email.setSubject(subject);
        email.setHtml(body);
        MailerSend ms = new MailerSend();
        ms.setToken(accessToken);
        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String contactDetail, String message) {

    }

    @Override
    public void sendAll(List<String> contactDetails , String message) {

    }
}
