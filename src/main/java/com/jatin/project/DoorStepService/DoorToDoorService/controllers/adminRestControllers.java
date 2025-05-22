package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.DBLoader;
import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class adminRestControllers 
{
    @Autowired
        public EmailSenderService email;
        
    
    @PostMapping("/checkAdminCreds")
    public String checkAdminCreds(HttpSession session,@RequestParam String email, @RequestParam String pass )
    {
        try 
        {
            //ResultSet rs = DBLoader.executeQuery("select * from admin where email='"+email+"' and pass='"+pass);
            ResultSet rs = DBLoader.executeQuery("select * from admin where email='" + email + "' and pass='" + pass + "'");

            if(rs.next())
            {
                session.setAttribute("email", rs.getString("email"));
                System.out.println(session.getAttribute("email"));
                return "success";
            }
            else
            {
                return "fail";
            }
        } 
        catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @PostMapping("/AdminManageCity")
    public String adminAddCity(@RequestParam String cname, @RequestParam String cdesc ,@RequestParam MultipartFile cphoto)
    {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName ="/"+cphoto.getOriginalFilename();
        System.out.println(projectPath+internal_path+folderName+orgName);
        try 
        {
            ResultSet rs = DBLoader.executeQuery("select * from cities where cityname ='"+cname+"' ");
            if(rs.next())
            {
                return "City Already Exists";
            }
            else
            {
                try 
                {
                    FileOutputStream fos =new FileOutputStream(projectPath+internal_path+folderName+orgName);
                    byte b1[] = cphoto.getBytes();
                    fos.write(b1);
                    fos.close();
                } catch (Exception e) 
                {
                    e.printStackTrace();
                    return "Error Uploading file to local folder";
                }
                
               // String query = "INSERT INTO cities (city_name, photo, description) VALUES ('" + cname + "', '" + cphoto.getOriginalFilename() + "', '" + cdesc + "')";

                //ResultSet rs2 = DBLoader.executeQuery("insert into cities (cityname,cityphoto,citydesc)values('"+cname+"','"+cphoto.getOriginalFilename()+"','"+cdesc+"' )");
//                ResultSet rs2 = DBLoader.executeQuery("select * from cities");
//                while(rs2.next())
//                {
//                    
//                }
                rs.moveToInsertRow();
                rs.updateString("cityname",cname);
                rs.updateString("cityphoto",orgName);
                rs.updateString("citydesc",cdesc);
                rs.insertRow();
                
                return "City Added SuccessFully";
            }
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @GetMapping("/getAlreadyExistedCities")
    public String getAlreadyExistedCities()
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");
        return ans;
    }
    @GetMapping("/deleteCity")
    public String deleteCity(@RequestParam String id)
    {
        int myid = Integer.parseInt(id);
        try 
        {
            ResultSet rs= DBLoader.executeQuery("select * from cities where cityid="+myid);
            if(rs.next())
            {
                rs.deleteRow();
                return "City Deleted Successfully";
            }
            else
            {
                return "City Does not Exist";
            }
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    
     @PostMapping("/AdminManageService")
    public String adminAddService(@RequestParam String sname, @RequestParam String sdesc ,@RequestParam MultipartFile sphoto)
    {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName ="/"+sphoto.getOriginalFilename();
        System.out.println(projectPath+internal_path+folderName+orgName);
        try 
        {
            ResultSet rs = DBLoader.executeQuery("select * from services where servicename ='"+sname+"' ");
            if(rs.next())
            {
                return "Service Already Exists";
            }
            else
            {
                try 
                {
                    FileOutputStream fos =new FileOutputStream(projectPath+internal_path+folderName+orgName);
                    byte b1[] = sphoto.getBytes();
                    fos.write(b1);
                    fos.close();
                } catch (Exception e) 
                {
                    e.printStackTrace();
                    return "Error Uploading file to local folder";
                }
                
               // String query = "INSERT INTO cities (city_name, photo, description) VALUES ('" + cname + "', '" + cphoto.getOriginalFilename() + "', '" + cdesc + "')";

                //ResultSet rs2 = DBLoader.executeQuery("insert into cities (cityname,cityphoto,citydesc)values('"+cname+"','"+cphoto.getOriginalFilename()+"','"+cdesc+"' )");
//                ResultSet rs2 = DBLoader.executeQuery("select * from cities");
//                while(rs2.next())
//                {
//                    
//                }
                rs.moveToInsertRow();
                rs.updateString("servicename",sname);
                rs.updateString("servicephoto",orgName);
                rs.updateString("servicedesc",sdesc);
                rs.insertRow();
                
                return "Service Added SuccessFully";
            }
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @GetMapping("/getAlreadyExistedServices")
    public String getAlreadyExistedServices()
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from services");
        return ans;
    }
    
    @GetMapping("/deleteService")
    public String deleteService(@RequestParam String id)
    {
        int myid = Integer.parseInt(id);
        System.out.println(myid);
        try 
        {
            ResultSet rs= DBLoader.executeQuery("select * from services where serviceid="+myid);
            if(rs.next())
            {
                rs.deleteRow();
                return "Service Deleted Successfully";
            }
            else
            {
                return "Service Does not Exist";
            }
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @PostMapping("/AdminChangePasswordRC")
    public String AdminChangePasswordRC(HttpSession session, @RequestParam String old,@RequestParam String new1)
    {
        String id = (String)session.getAttribute("email");
        try 
        {
            ResultSet rs = DBLoader.executeQuery("select * from admin where email='"+id+"' and pass='"+old+"'");
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
    
    
    @GetMapping("/forgot")
    public String forgot(@RequestParam String email, @RequestParam String otp) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from admin where email='" + email + "'");
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

    @GetMapping("/otpverify")
    public String otpverify(@RequestParam String email) {
        try {
            ResultSet rs = DBLoader.executeQuery("select * from admin where email='" + email + "'");
            if (rs.next()) {
                rs.moveToCurrentRow();
                String pass = rs.getString("pass");
                String subject = "Your Account Password - Door Step Services";
                String body = "Dear Admin,\n\n"
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
    
    
//    @PostMapping("/ForgotAdminPassword")
//    public String ForgotAdminPassword(@RequestParam String email,@RequestParam String otp)
//    {
//        System.out.println(email+" "+ otp);
//        try 
//        {
//            ResultSet rs = DBLoader.executeQuery("select * from admin where email='"+email+"'");
//            if(rs.next())
//            {
//                //System.out.println("inside rs");
//                this.email.sendSimpleEmail("jatinkapoor3066@gmail.com", "Your OTP for password forgot is "+otp, "OTP Verification for Forgot Password");               
//                return "success";
//            }
//            else
//            {
//                return "No Such Email Found!";
//            }
//        } catch (Exception e) 
//        {
//            return e.toString();
//        }
//        //return email+" "+ otp;
//    }
    
    
}
