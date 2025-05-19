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
public class adminRestControllers 
{
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
}
