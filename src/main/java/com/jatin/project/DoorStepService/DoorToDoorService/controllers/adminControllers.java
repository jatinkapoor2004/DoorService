package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
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
    public String adminHome(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/AdminHome";
    }
    @GetMapping("/AdminManageCities")
    public String adminManageCities(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/AdminManageCities";
    }
    @GetMapping("/AdminManageServices")
    public String AdminManageServices(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/AdminManageServices";
    }
}
