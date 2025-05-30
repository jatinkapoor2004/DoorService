package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class adminControllers 
{
    @GetMapping("/AdminLogin")
    public String adminLogin(HttpSession session)
    {
        session.invalidate();
        return "/AdminLogin";
    }
    @GetMapping("/AdminHome")
    public String adminHome(HttpSession session)
    {
        String id=(String)session.getAttribute("email");
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
        String id=(String)session.getAttribute("email");
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
        String id=(String)session.getAttribute("email");
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/AdminManageServices";
    }
    @GetMapping("/AdminChangePassword")
    public String AdminChangePassword(HttpSession session)
    {
        String id=(String)session.getAttribute("email");
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/Admin_Change_Password";
    }
    @GetMapping("/about3")
    public String about(HttpSession session)
    {
        //session.invalidate();
        return "/About_Us_Admin";
    }
}
