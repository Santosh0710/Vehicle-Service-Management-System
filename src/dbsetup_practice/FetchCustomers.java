package dbsetup_practice;

import java.sql.*;

public class FetchCustomers{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String user = "root";
        String password = "root123";

        try{
            //1. Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2. Create Connection
            Connection con = DriverManager.getConnection(url,user,password);
            //3. Create Statement
            Statement stmt = con.createStatement();
            //4. Execute SElect Query
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers");

            System.out.println("----Customers List----");
            //5. Read Data Row By Row
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + " " + name);


            }
            // Close Connection
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}