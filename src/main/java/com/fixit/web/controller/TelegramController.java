package com.fixit.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/telegram")
public class TelegramController {

    @GetMapping("/webhook")
    public ResponseEntity<String> getUpdate(@RequestBody String message){
        return ResponseEntity.ok(message);
    }

    
}
