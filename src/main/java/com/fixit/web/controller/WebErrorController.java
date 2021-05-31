package com.fixit.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping("/error")
    public String displayError(ModelAndView modelAndView){
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
