package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminControllers 
{
    @GetMapping("/AdminLogin")
    public String adminLogin()
    {
        return "/AdminLogin";
    }
    @GetMapping("/AdminHome")
    public String adminHome()
    {
        return "/AdminHome";
    }
    @GetMapping("/AdminManageCities")
    public String adminManageCities()
    {
        return "/AdminManageCities";
    }
    @GetMapping("/AdminManageServices")
    public String AdminManageServices()
    {
        return "/AdminManageServices";
    }
}
