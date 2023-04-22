package com.fixit.web.controller;

import io.sentry.Sentry;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WebErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping("/error")
    public String displayError(HttpServletRequest request, Model model){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Sentry.captureMessage(RequestDispatcher.ERROR_EXCEPTION);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("exception",
                        "We're sorry, but the page you were looking for doesn't exist");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("exception",
                        "something went wrong. Please  contact the web administrator");
            }else if(statusCode == HttpStatus.BAD_REQUEST.value()){
                model.addAttribute("exception",
                        "We're sorry, but the page you were looking for doesn't exist");
            }else if(statusCode == HttpStatus.UNAUTHORIZED.value()){
                model.addAttribute("exception",
                        "You are not authorized  to carry out this action");
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
