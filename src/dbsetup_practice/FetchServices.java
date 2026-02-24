package dbsetup_practice;

import java.sql.*;
public class FetchServices{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String user = "root";
        String password = "root123";

        try{
//            1.Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

//            2. Connect to Database
            Connection con = DriverManager.getConnection(url , user , password);
            //3. Create Statement
            Statement stmt = con.createStatement();
//            4. Execute SELECT Query
            ResultSet rs = stmt.executeQuery("SELECT * FROM services");

            System.out.println("---Services List---");

//            5. Read Result Set
            while(rs.next())
            {
               int id = rs.getInt("service_id");
               String brand = rs.getString("vehicle_brand");
               double cost = rs.getDouble("service_cost");

                System.out.println(id +"|"+ brand+"|"+cost);
            }
//            6. Close resourses
            rs.close();
            stmt.close();
            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}