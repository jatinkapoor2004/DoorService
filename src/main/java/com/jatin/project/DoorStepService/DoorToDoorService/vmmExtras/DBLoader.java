package com.jatin.project.DoorStepService.DoorToDoorService.vmmExtras;
import java.sql.*;
public class DBLoader 
{
    public static ResultSet executeQuery(String query)
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
            
            Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/door_to_door_service","root","maubilla");
            System.out.println("Connection build successfully");
            
            Statement stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("statement created");
            
            ResultSet rs=stmt.executeQuery(query);
            return rs;
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
