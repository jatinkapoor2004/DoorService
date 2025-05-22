package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.DBLoader;
import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class userRestControllers {

    @Autowired
        public EmailSenderService email;
    
    @PostMapping("/UserSignUp")
    public String UserSignUp(@RequestParam String name, @RequestParam String email, @RequestParam String pass, @RequestParam String contact, @RequestParam String address, @RequestParam MultipartFile photo) 
    {
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

            if (rs.next())
            {
                session.setAttribute("id", rs.getInt("id"));
                session.setAttribute("email", rs.getString("email"));
                System.out.println(session.getAttribute("id"));
                return "success";
            } 
            else {
                return "fail";
            }
        } 
        catch (Exception e) 
        {
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
    @GetMapping("/getSingleVendorDetails")
    public String getSingleVendorDetails(@RequestParam String vid)
    {
        int vId = Integer.parseInt(vid);
        
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT vendors.*,services.* FROM vendors  JOIN services ON services.serviceid=vendors.service where vendors.id ="+vId );
        return ans;
    }
    
    @GetMapping("getPhotosServiceBySingleVendor")
    public String getPhotosServiceBySingleVendor(@RequestParam String vid)
    {
        int vId = Integer.parseInt(vid);
        String ans = new RDBMS_TO_JSON().generateJSON("select vendorphotos.* from vendorphotos JOIN vendors ON vendors.id = vendorphotos.vid where vid ="+vId);
        return ans;
    }
    
    @GetMapping("/view_slots")
    public String view_slots(@RequestParam String vendorId, @RequestParam String date) {

        System.out.println(date);
        System.out.println(vendorId);
        try {
            ResultSet rs = DBLoader.executeQuery("select * from vendors where id=" + vendorId + " ");

            String start;
            String end;
            String slot;
            if (rs.next()) 
            {
                start = rs.getString("starttime");
                end = rs.getString("endtime");
                slot = rs.getString("price");

            } 
            else 
            {
                String err = "failed";
                return err;
            }
            int Start = Integer.parseInt(start);
            int End = Integer.parseInt(end);
            int Slot = Integer.parseInt(slot);
            JSONObject ans = new JSONObject();

            //Define JSONArray
            JSONArray arr = new JSONArray();
            for (int i = Start; i <= End; i++) {
                JSONObject row = new JSONObject();
                row.put("start_slot", Start);
                row.put("end_slot", ++Start);
                row.put("slot_amount", slot);

                ResultSet rs2 = DBLoader.executeQuery("select * from booking_detail where start_slot ='" + i + "' and booking_id in (select id  from booking where date=\'" + date + "\' and vendor_id =" + vendorId + " ) ");
                if (rs2.next()) {
                    row.put("status", "Booked");
                } else {
                    row.put("status", "Available");
                }

                arr.add(row);
            }
            ans.put("ans", arr);
            System.out.println(ans.toString());
            return (ans.toJSONString());

        } catch (Exception e) {
            return e.toString();
        }

    }
    
    
    @GetMapping("/pay")
    public String payment(@RequestParam String date,
            @RequestParam String vid,
            @RequestParam String amount,
            @RequestParam String slots,
            HttpSession session,
            @RequestParam String type,
            @RequestParam String status) {
        String ans = "";

        try {
            
        int uid = (int) session.getAttribute("id");
        
            System.out.println("In Rest Controller");
            String user_email = "";
            ResultSet rs4 = DBLoader.executeQuery("select * from users where id ="+uid+" ");
            if(rs4.next()){
                user_email = rs4.getString("email");
            }
            
            System.out.println("User Email "+user_email);
            // 1. Insert into booking table
            ResultSet rs = DBLoader.executeQuery("SELECT * FROM booking");
            rs.moveToInsertRow();
            rs.updateString("date", date);
            rs.updateString("vendor_id", vid);
            rs.updateString("user_email", user_email);
            rs.updateString("total_price", amount);
            rs.updateString("payment_type", type);
            rs.updateString("status", status);
            rs.insertRow();

            // 2. Get inserted booking_id
            int booking_id = 0;
            ResultSet rs2 = DBLoader.executeQuery("SELECT MAX(id) AS maxid FROM booking");
            if (rs2.next()) {
                booking_id = rs2.getInt("maxid");
            }
            System.out.println("Booking ID "+booking_id);
            // 3. Insert slots into booking_detail table
            StringTokenizer st = new StringTokenizer(slots, ",");
            while (st.hasMoreTokens()) {
                int start_slot = Integer.parseInt(st.nextToken());
                int end_slot = start_slot + 1;

                ResultSet rs3 = DBLoader.executeQuery("SELECT * FROM booking_detail");
                rs3.moveToInsertRow();
                rs3.updateInt("booking_id", booking_id); // fk to booking_id
                rs3.updateString("start_slot", String.valueOf(start_slot));
                rs3.updateString("end_slot", String.valueOf(end_slot));
                rs3.insertRow();
            }

            ans = "success";
            return ans;
        } catch (Exception ex) {
            return ex.toString();
        }
    }
    @GetMapping("/UserBookingHistory")
    public String userBookingHistory(HttpSession session)
    {
        String email = (String)session.getAttribute("email");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from booking where user_email='"+email+"'");
        return ans;
    }
    
    @PostMapping("/UserChangePasswordRC")
    public String UserChangePasswordRC(HttpSession session, @RequestParam String old,@RequestParam String new1)
    {
        int id = (int)session.getAttribute("id");
        try 
        {
            ResultSet rs = DBLoader.executeQuery("select * from users where id="+id+" and pass='"+old+"'");
            if(rs.next())
            {
                rs.updateString("pass", new1);
                rs.updateRow();
                return "success";
            }
            else
            {
                return "Wrong Old Password!";
            }
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    
    
        @GetMapping("/userAddReview")
    public String userAddReview(@RequestParam String vendor_email, @RequestParam int rating, @RequestParam String comment, HttpSession session) {
        String user_email = (String) session.getAttribute("email");
        System.out.println(user_email);
//        System.out.println(rating);
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeQuery("Select * from review_table");

            rs.moveToInsertRow();
            rs.updateString("vendor_email", vendor_email);
            rs.updateString("user_email", user_email);
            rs.updateString("comment", comment);
            rs.updateInt("rating", rating);
            rs.insertRow();
            ans = "success";

        } catch (Exception e) {
            ans = e.toString();
        }

        return ans;
    }

    
    
    @GetMapping("/userShowRatings")
    public String userShowRatings(@RequestParam String vendor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select * from review_table where vendor_email='" + vendor_email + "' ");
        System.out.println(ans);
        return ans;

    } 
    
    
    @GetMapping("/userShowAverageRatings")
    public String userShowAverageRatings(@RequestParam String vendor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select avg(rating) as r1 from review_table where vendor_email='" + vendor_email + "' ");
        System.out.println(ans);
        return ans;

    }
    
    @GetMapping("/forgotUserPassword")
    public String forgotUserPassword(@RequestParam String email, @RequestParam String otp) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from users where email='" + email + "'");
            if (rs.next()) {
                String body = "Your otp for login page is =" + otp;
                String subject = "Login Authntication";
                this.email.sendSimpleEmail(email, body, subject);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

    @GetMapping("/otpverifyUser")
    public String otpverify(@RequestParam String email) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from users where email='" + email + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();
                String pass = rs.getString("pass");
                String subject = "Your Account Password - Door Step Services";
                String body = "Dear User,\n\n"
                        + "As per your request, here is your account password:\n\n"
                        + "Password: " + pass + "\n\n"
                        + "Please do not share this password with anyone.\n"
                        + "We recommend changing your password after login for better security.\n\n"
                        + "Regards,\n"
                        + "Team Door Step Services";
                this.email.sendSimpleEmail(email, body, subject);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
    
}
