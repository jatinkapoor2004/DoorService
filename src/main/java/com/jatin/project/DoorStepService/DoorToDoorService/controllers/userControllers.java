package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userControllers {

    @GetMapping("/UserSignUp")
    public String UserSignUp(HttpSession session) {
        session.invalidate();
        return "/UserSignUp";
    }

    @GetMapping("/UserLogin")
    public String UserLogin(HttpSession session) {
        session.invalidate();
        return "/UserLogin";
    }

    @GetMapping("/")
    public String UserHome() 
    {
        return "/UserHome";
    }
    @GetMapping("/ServicesForSelectedCity")
    public String ServicesForSelectedCity(HttpSession session) 
    {
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
    public String ServiceBookingDateSelection(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/UserLogin";
        }
        else
        return "/User_Service_Booking_Date_Selection";
    }
    @GetMapping("/payment")
    public String PaymentModeSelection(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/UserLogin";
        }
        else
        return "/User_Payment_Mode";
    }
    @GetMapping("/BookingHistory")
    public String BookingHistory(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/UserLogin";
        }
        else
        return "/User_Show_Booking_History";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/about")
    public String about(HttpSession session)
    {
        //session.invalidate();
        return "/About_Us";
    }
    @GetMapping("/UserChangePassword")
    public String UserChangePassword(HttpSession session)
    {
        Integer id=(Integer)session.getAttribute("id");
        if(id==null)
        {
            return "redirect:/UserLogin";
        }
        else
        return "/User_Change_Password";
    }
}
