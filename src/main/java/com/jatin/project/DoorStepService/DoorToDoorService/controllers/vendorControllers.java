package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class vendorControllers 
{
    @GetMapping("/VendorSignUp")
    public String VendorSignUp(HttpSession session)
    {
        session.invalidate();
        return "/VendorSignUp";
    }
    @GetMapping("/AdminManageVendors")
    public String AdminManageVendors(HttpSession session)
    {
        String id=(String)session.getAttribute("email");;
        if(id==null)
        {
            return "redirect:/AdminLogin";
        }
        else
        return "/AdminManageVendors";
    }
    @GetMapping("/VendorLogin")
    public String VendorLogin(HttpSession session)
    {
        session.invalidate();
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
    @GetMapping("/VendorChangePassword")
    public String VendorChangePassword(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/VendorLogin";
        }
        else
        return "/Vendor_Change_Password";
    }
}
