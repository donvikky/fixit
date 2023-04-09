package com.fixit.web.controller;

import com.fixit.web.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    private AuthUtils authUtils;

    @Autowired
    private TelegramController(AuthUtils authUtils){
        this.authUtils = authUtils;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> getUpdate(@RequestBody String message){
        System.out.println("The message is : "+ message);
        return ResponseEntity.ok(message);
    }

//    @PostMapping(path = "/webhook", consumes = "application/json")
//    public void createUser(HttpServletRequest request) throws IOException {
//        BufferedReader reader = null;
//        try {
//            reader = request.getReader();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        String line;
//        StringBuilder requestBody = new StringBuilder();
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
//        System.out.println(requestBody.toString());
//    }

    @PostMapping("/webhook")
    public void getUpdate(@RequestBody Update update){
        System.out.println(update.getMessage());
    }

    
}
