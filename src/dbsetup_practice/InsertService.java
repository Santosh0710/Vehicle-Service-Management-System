package dbsetup_practice;

import java.sql.*;
import java.util.Scanner;

public class InsertService{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String user = "root";
        String password = "root123";

        Scanner sc = new Scanner(System.in);

        try{
//            1. Take Input
            System.out.println("Enter Service Id");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter Vehicle Brand");
            String brand = sc.nextLine();
            System.out.println("Enter Service Cost ");
            double cost = sc.nextDouble();

//            2. Connect
            Connection con = DriverManager.getConnection(url,user,password);
//            3. SQL with placeholders
            String sql = "INSERT INTO services VALUES(?,?,?)";
//            4. Prepare Statement
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            ps.setString(2,brand);
            ps.setDouble(3,cost);
//            5. Execute Update
            ps.executeUpdate();

            System.out.println("Service Inserted successfully");

//            6. Close
            ps.close();
            con.close();
            sc.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}