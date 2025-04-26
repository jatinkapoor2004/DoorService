
package com.jatin.project.DoorStepService.DoorToDoorService.controllers;

import com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras.DBLoader;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class userRestControllers 
{
    @PostMapping("/UserSignUp")
    public String UserSignUp(@RequestParam String name,@RequestParam String email,@RequestParam String pass,@RequestParam String contact,@RequestParam String address,@RequestParam MultipartFile photo)
    {
        String projectPath = System.getProperty("user.dir");
        String internal_path = "/src/main/resources/static";
        String folderName = "/myuploads";
        String orgName ="/"+photo.getOriginalFilename();
        try 
        {
            ResultSet rs=DBLoader.executeQuery("select * from users where email='"+email+"'");
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
                } 
                catch (Exception e) 
                {
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
        } catch (Exception e) 
        {
            return e.toString();
        }
    }
    
}
