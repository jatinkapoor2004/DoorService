
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
public class vendorRestControllers 
{
    @PostMapping("/VendorSignUp")
    public String VendorSignUp(@RequestParam String name, @RequestParam String email, @RequestParam String pass, @RequestParam String city, @RequestParam String service, @RequestParam String subservice, @RequestParam String starttime, @RequestParam String endtime, @RequestParam String price, @RequestParam String contact, @RequestParam String desc,@RequestParam MultipartFile photo)
    {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName ="/"+photo.getOriginalFilename();
        try 
        {
            ResultSet rs=DBLoader.executeQuery("select * from vendors where email='"+email+"'");
            if(rs.next())
            {
                return "Already Exist";
            }
            else
            {
                try 
                {
                    FileOutputStream fos =new FileOutputStream(projectPath+internal_path+folderName+orgName);
                    byte b1[] = photo.getBytes();
                    fos.write(b1);
                    fos.close();
                } catch (Exception e) 
                {
                    e.printStackTrace();
                    return "Error Uploading file to local folder";
                }
                rs.moveToInsertRow();
                rs.updateString("name", name);
                rs.updateString("email", email);
                rs.updateString("pass", pass);
                
                rs.updateInt("city", Integer.parseInt(city));
                rs.updateInt("service", Integer.parseInt(service));
               
                
               
                rs.updateString("subservice", subservice);
                rs.updateString("starttime", starttime);
                rs.updateString("endtime", endtime);
                rs.updateString("price", price);
                rs.updateString("contact", contact);
                rs.updateString("desc", desc);
                rs.updateString("status", "Blocked");
                
                
                rs.updateString("photo", orgName);
                
                rs.insertRow();
                return "Success";
                
            }
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @GetMapping("/getCitiesForVendor")
    public String getCitiesForVendor()
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");
        return ans;
    }
    @GetMapping("/getServicesForVendor")
    public String getServicesForVendor()
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from services");
        return ans;
    }
    @GetMapping("/getVendorData")
    public String getVendorData()
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select vendors.*, cities.cityname, services.servicename  from vendors JOIN cities ON cities.cityid = vendors.city  JOIN services ON vendors.service = services.serviceid");
        return ans;
    }

    
    @GetMapping("/AcceptVendor")
    public String AcceptVendor(@RequestParam String vendorid)
    {
        int vendorid1 = Integer.parseInt(vendorid);
        try {
            ResultSet rs = DBLoader.executeQuery("select * from vendors where id="+vendorid1 );
            if(rs.next())
            {
                rs.updateString("status", "Accepted");
                rs.updateRow();
                return "Status Updated to Accepted";
            }
            else
            {
                return "Vendor does not exist";
            }
            
        } 
        catch (Exception e)
        {
            System.out.println("*************Error Updating vendor status to accepted************8");
            return e.toString();
        }
    }
    @GetMapping("/BlockVendor")
    public String BlockVendor(@RequestParam String vendorid)
    {
        int vendorid1 = Integer.parseInt(vendorid);
        try {
            ResultSet rs = DBLoader.executeQuery("select * from vendors where id="+vendorid1 );
            if(rs.next())
            {
                rs.updateString("status", "Blocked");
                rs.updateRow();
                return "Status Updated to Blocked";
            }
            else
            {
                return "Vendor does not exist";
            }
            
        } 
        catch (Exception e)
        {
            System.out.println("*************Error Updating vendor status to Blocled ************8");
            return e.toString();
        }
    }
    
    
    @PostMapping("/VendorLogin")
    public String vendorLogin(HttpSession session,@RequestParam String email, @RequestParam String pass)
    {
        try 
        {
            //ResultSet rs = DBLoader.executeQuery("select * from admin where email='"+email+"' and pass='"+pass);
            ResultSet rs = DBLoader.executeQuery("select * from vendors where email='" + email + "' and pass='" + pass + "'");

            if(rs.next())
            {
                session.setAttribute("id", rs.getInt("id"));
                
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
    
    @PostMapping("/VendorAddPhotos")
    public String VendorAddPhotos(HttpSession session,@RequestParam String sdesc, @RequestParam MultipartFile sphoto)
    {
         {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName ="/"+sphoto.getOriginalFilename();
        System.out.println(projectPath+internal_path+folderName+orgName);
        try 
        {
            ResultSet rs = DBLoader.executeQuery("select * from vendorphotos");
            
                try 
                {
                    FileOutputStream fos =new FileOutputStream(projectPath+internal_path+folderName+orgName);
                    byte b1[] = sphoto.getBytes();
                    fos.write(b1);
                    fos.close();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                    return "Error Uploading file to local folder";
                }
                
                rs.moveToInsertRow();
                rs.updateString("pphoto",orgName);
                rs.updateString("pdesc",sdesc);
                int sid = (int) session.getAttribute("id");
                System.out.println(sid);
                rs.updateInt("vid",sid);
                rs.insertRow();
                
                return "Photo Added SuccessFully";
            
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    }
    
    @GetMapping("/getAlreadyExistedPhotos")
    public String getAlreadyExistedPhotos(HttpSession session)
    {
        int sid = (int) session.getAttribute("id");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from vendorphotos where vid="+sid);
        return ans;
    }
    
    @GetMapping("/deletePhoto")
    public String deletePhoto(@RequestParam String id)
    {
        int myid = Integer.parseInt(id);
        try 
        {
            ResultSet rs= DBLoader.executeQuery("select * from vendorphotos where pid="+myid);
            if(rs.next())
            {
                rs.deleteRow();
                return "Photo Deleted Successfully";
            }
            else
            {
                return "Photo Does not Exist";
            }
            
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
    @GetMapping("/FetchVendorData")
    public String fetchVendorData(HttpSession session)
    {
        int sid = (int) session.getAttribute("id");
        String ans = new RDBMS_TO_JSON().generateJSON("select vendors.*, cities.cityname, services.servicename  from vendors JOIN cities ON cities.cityid = vendors.city  JOIN services ON vendors.service = services.serviceid where id="+sid);
        return ans;      
    }
    
    @PostMapping("/UpdateVendorProfile")
    public String UpdateVendorProfile(HttpSession session,@RequestParam String name, @RequestParam String city, @RequestParam String starttime, @RequestParam String endtime, @RequestParam String price, @RequestParam String contact, @RequestParam String desc)
    {
        
        try 
        {
            int sid = (int) session.getAttribute("id");
            ResultSet rs=DBLoader.executeQuery("select * from vendors where id="+sid);
            if(rs.next())
            {
                rs.updateString("name", name);                
                rs.updateInt("city", Integer.parseInt(city));
                rs.updateString("starttime", starttime);
                rs.updateString("endtime", endtime);
                rs.updateString("price", price);
                rs.updateString("contact", contact);
                rs.updateString("desc", desc);
                rs.updateRow();
                return "Profile Updated Successfully !";
            }
            else
            {
                return "Profile Not found ";
            }
        } 
        catch (Exception e) 
        {
            return e.toString();
        }
    
    }
    //    @GetMapping("/getCityForVendorTable")
//    public String getCityForVendorTable(@RequestParam String cityid)
//    {
//        int cityid1 = Integer.parseInt(cityid);
//        try {
//            ResultSet rs = DBLoader.executeQuery("select cityname from cities where cityid="+cityid1);
//            if(rs.next())
//            {
//                return rs.getString("cityname");
//            }
//            else
//            {
//                return "City Not Exist";
//            }
//            
//        } 
//        catch (Exception e)
//        {
//            System.out.println("Error Fetching city for vendor table");
//            return e.toString();
//        }
//    }
//    
//     @GetMapping("/getServiceForVendorTable")
//    public String getServiceForVendorTable(@RequestParam String serviceid)
//    {
//        int serviceid1 = Integer.parseInt(serviceid);
//        try {
//            ResultSet rs = DBLoader.executeQuery("select servicename from services where serviceid="+serviceid1);
//            if(rs.next())
//            {
//                return rs.getString("servicename");
//            }
//            else
//            {
//                return "Service Not Exist";
//            }
//            
//        } 
//        catch (Exception e)
//        {
//            System.out.println("Error Fetching Service name for vendor table");
//            return e.toString();
//        }
//    }
}
