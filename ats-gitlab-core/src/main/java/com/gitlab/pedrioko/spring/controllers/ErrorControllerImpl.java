package com.gitlab.pedrioko.spring.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements ErrorController {
 
    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "redirect:/index";
    }
 
    @Override
    public String getErrorPath() {
        return "/error";
    }
}