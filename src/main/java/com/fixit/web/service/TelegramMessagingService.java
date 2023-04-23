package com.fixit.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramMessagingService implements MessagingService{

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.base.url}")
    private String baseUrl;

    @Override
    public void send(String contactDetail, String subject, String content) {

    }

    @Override
    public void send(String contactDetail, String message) {
        Map messageMap = new HashMap<String, String>();
        messageMap.put("chat_id", contactDetail);
        messageMap.put("text", message);
        messageMap.put("parse_mode", "HTML");

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMessage = mapper.writeValueAsString(messageMap);

            String url = String.format("%s/bot%s/sendMessage",baseUrl, botToken);
            HttpResponse response =  Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage)
                    .asJson();
            //System.out.println("Response after sending: "+ response.getBody().toString());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

    }

    @Override
    public void sendAll(List<String> contactDetails, String message) {
        System.out.println("Sending Messages");
        contactDetails.stream().forEach(contact -> send(contact, message));
    }
}
