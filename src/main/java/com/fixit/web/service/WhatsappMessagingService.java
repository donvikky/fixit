package com.fixit.web.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WhatsappMessagingService implements MessagingService{

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.account.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.mobile.number}")
    private String TWILIO_MOBILE_NUMBER;

    private static final String COUNTRY_CODE = "+234";

    @Override
    public void send(String contactDetail, String subject, String content) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(String.format("whatsapp:%s%s",COUNTRY_CODE, contactDetail)),
                new com.twilio.type.PhoneNumber(String.format("whatsapp:%s", TWILIO_MOBILE_NUMBER)),
                content
                ).create();

        System.out.println(message.getSid());
    }

    @Override
    public void sendAll(List<String> contactDetails) {

    }
}
