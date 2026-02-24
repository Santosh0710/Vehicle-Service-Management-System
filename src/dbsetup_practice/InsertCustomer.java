package dbsetup_practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertCustomer{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String username = "root";
        String password = "root123";

        try{
            //step 1 connect
            Connection con = DriverManager.getConnection(url,username,password);
            //SQL with placeholders
            String sql = "INSERT INTO customers VALUES (?,?)";
            //Step 3 : Prepare Statement
            PreparedStatement ps = con.prepareStatement(sql);
            //Step 4 Set Values
            ps.setInt(1,501);
            ps.setString(2,"Santosh");
            //Step 5: Execute
            ps.executeUpdate();

            System.out.println("Customer Inserted Successfully");
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}