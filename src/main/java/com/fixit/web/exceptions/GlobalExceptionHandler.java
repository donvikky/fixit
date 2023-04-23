package com.fixit.web.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class, NoSuchElementException.class})
    public ModelAndView handleEmployeeNotFoundException(HttpServletRequest request, Exception ex){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", "The requested resource was not found");
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ModelAndView handleAccessDeniedException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", "You must complete your profile before attempting to post a job.");
        modelAndView.addObject("url", request.getRequestURL());

        modelAndView.setViewName("error");
        return modelAndView;
    }
}
