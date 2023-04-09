package com.fixit.web.service;

import java.util.List;

public interface MessagingService {
    public void send(String contactDetail, String subject, String  content);

    public void send(String  contactDetail, String message);
    public void sendAll(List<String> contactDetails);
}
