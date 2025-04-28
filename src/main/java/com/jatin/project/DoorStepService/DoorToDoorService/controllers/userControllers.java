package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userControllers 
{
    @GetMapping("/UserSignUp")
    public String UserSignUp()
    {
        return "/UserSignUp";
    }
    @GetMapping("/UserLogin")
    public String UserLogin()
    {
        return "/UserLogin";
    }
}
