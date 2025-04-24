package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

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
    public String AdminManageVendors()
    {
        return "/AdminManageVendors";
    }
    @GetMapping("/VendorLogin")
    public String VendorLogin()
    {
        return "/VendorLogin";
    }
    @GetMapping("/VendorHome")
    public String RedirectingToVendorHome()
    {
        return "/VendorHome";
    }
    @GetMapping("/VendorAddPhotos")
    public String VendorAddPhotos()
    {
        return "/VendorAddPhotos";
    }
}
