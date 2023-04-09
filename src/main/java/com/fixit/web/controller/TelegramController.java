package com.fixit.web.controller;

import com.fixit.web.entity.Profile;
import com.fixit.web.service.MessagingService;
import com.fixit.web.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    private MessagingService messagingService;

    private ProfileService profileService;

    @Autowired
    private TelegramController(@Qualifier("telegramMessagingService") MessagingService messagingService,
                               ProfileService profileService) {
        this.messagingService = messagingService;
        this.profileService = profileService;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> getUpdate(@RequestBody String message) {
        System.out.println("The message is : " + message);
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
    public void getUpdate(@RequestBody Update update) {
        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            Optional<Profile> profileOptional = profileService.findByTelegramId(chatId);
            String response = "";
            if (profileOptional.isEmpty() && update.getMessage().getContact() == null) {
                response = "You have not linked your telegram account. Please access the menu options and " +
                        "click the 'Share Contact' option to link your telegram account to your fixit profile.";
            } else if (profileOptional.isEmpty() && update.getMessage().getContact() != null) {
                String mobileNumber = update.getMessage().getContact().getPhoneNumber();
                Optional<Profile> telegramProfileOptional = profileService.findByMobileNumber(mobileNumber);
                if (telegramProfileOptional.isEmpty()) {
                    response = "Sorry, your telegram number does not match any record on " +
                            "our service. Please update the mobile number you used to register on the Fixit service " +
                            "to match your telegram mobile number";
                } else {
                    Profile telegramProfile = telegramProfileOptional.get();
                    telegramProfile.setTelegramId(chatId);
                    profileService.save(telegramProfile);
                    response = "Your telegram account has been linked successfully!";
                }
            } else if (profileOptional.isPresent()) {
                response = "Please use one of the preset options. You can start by typing /.";
            } else {
                response = "Please use one of the preset options. You can start by typing /.";
            }
            messagingService.send(chatId, response);
        }
    }
}

    

