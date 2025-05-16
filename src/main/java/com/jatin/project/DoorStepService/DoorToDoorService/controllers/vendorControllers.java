package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class vendorControllers 
{
    @GetMapping("/VendorSignUp")
    public String VendorSignUp()
    {
        return "/VendorSignUp";
    }
    @GetMapping("/AdminManageVendors")
    public String AdminManageVendors(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/VendorLogin";
        }
        else
        return "/AdminManageVendors";
    }
    @GetMapping("/VendorLogin")
    public String VendorLogin()
    {
        return "/VendorLogin";
    }
    @GetMapping("/VendorHome")
    public String RedirectingToVendorHome(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/VendorLogin";
        }
        else
        return "/VendorHome";
    }
    @GetMapping("/VendorAddPhotos")
    public String VendorAddPhotos(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/VendorLogin";
        }
        else
        return "/VendorAddPhotos";
    }
}
