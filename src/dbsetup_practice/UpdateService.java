package dbsetup_practice;

import java.sql.*;
import java.util.Scanner;

public class UpdateService{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
                String user = "root";
        String password = "root123";
        Scanner sc = new Scanner(System.in);
        try{
//            1. Take Input
            System.out.println("Enter service id to update");
            int id = sc.nextInt();
            System.out.println("Enter the new service cost");
            double newCost = sc.nextDouble();

//            2. Connect
            Connection con = DriverManager.getConnection(url,user,password);
//            3. SQL
            String sql = "UPDATE services SET service_cost = ? WHERE service_id = ?";
//            4. Prepared Statement
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, newCost);
            ps.setInt(2, id);
//            5. Execute Update
            int rows = ps.executeUpdate();
            if(rows>0){
                System.out.println("Service Updated Successfully");
            }else{
                System.out.println("Service Id not found");
            }
//            6. Close Connection
            ps.close();
            con.close();
            sc.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}