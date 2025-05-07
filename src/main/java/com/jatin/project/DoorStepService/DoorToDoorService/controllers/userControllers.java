package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userControllers {

    @GetMapping("/UserSignUp")
    public String UserSignUp() {
        return "/UserSignUp";
    }

    @GetMapping("/UserLogin")
    public String UserLogin() {
        return "/UserLogin";
    }

    @GetMapping("/UserHome")
    public String UserHome() {
        return "/UserHome";
    }
    @GetMapping("/ServicesForSelectedCity")
    public String ServicesForSelectedCity() {
        return "/UserHome_ServicesForSelectedCity";
    }
    @GetMapping("/VendorsForSelectedCity")
    public String VendorsForSelectedCity() {
        return "/UserHome_Vendors_For_Selected_Service";
    }
    @GetMapping("/SingleVendorDetail")
    public String SingleVendorDetail() {
        return "/UserHome_Single_Vendor_Details";
    }
    @GetMapping("/ServiceBookingDateSelection")
    public String ServiceBookingDateSelection()
    {
        return "/User_Service_Booking_Date_Selection";
    }
}
