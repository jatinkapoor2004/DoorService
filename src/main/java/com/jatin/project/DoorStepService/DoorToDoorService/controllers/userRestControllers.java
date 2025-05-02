package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.DBLoader;
import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class userRestControllers {

    @PostMapping("/UserSignUp")
    public String UserSignUp(@RequestParam String name, @RequestParam String email, @RequestParam String pass, @RequestParam String contact, @RequestParam String address, @RequestParam MultipartFile photo) {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName = "/" + photo.getOriginalFilename();
        try {
            ResultSet rs = DBLoader.executeQuery("select * from users where email='" + email + "'");
            if (rs.next()) {
                return "Already Exist";
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
                    byte b1[] = photo.getBytes();
                    fos.write(b1);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Error Uploading file to local folder";
                }
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("email", email);
                rs.updateString("pass", pass);

                rs.updateString("contact", contact);
                rs.updateString("address", address);

                rs.updateString("photo", orgName);

                rs.insertRow();
                return "Success";

            }
        } catch (Exception e) {
            return e.toString();
        }
    }

    @PostMapping("/checkUserCreds")
    public String checkUserCreds(HttpSession session, @RequestParam String email, @RequestParam String pass) {
        try {
            //ResultSet rs = DBLoader.executeQuery("select * from admin where email='"+email+"' and pass='"+pass);
            ResultSet rs = DBLoader.executeQuery("select * from users where email='" + email + "' and pass='" + pass + "'");

            if (rs.next()) {
                session.setAttribute("id", rs.getInt("id"));

                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }

    @GetMapping("/getCitiesDropdown")
    public String getCitiesDropdown() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");
        return ans;
    }

    @GetMapping("/showServicesInUserHome")
    public String showServicesInUserHome(@RequestParam String cityId)
    {
        int cityid = Integer.parseInt(cityId);
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT distinct services.* FROM services JOIN vendors ON vendors.service = services.serviceid WHERE vendors.city = "+cityid+" and vendors.status ='Accepted'");
        return ans;
    }
    @GetMapping("/showVendorsInUserHome")
    public String showVendorsInUserHome(@RequestParam String cityId,@RequestParam String serviceId)
    {
        int cityid = Integer.parseInt(cityId);
        int serviceid = Integer.parseInt(serviceId);
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM vendors where city="+cityid+" and service="+serviceid+" and status='Accepted'" );
        return ans;
    }

}
