package dbsetup_practice;

import java.sql.*;
import java.util.Scanner;
public class DeleteService{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String user = "root";
        String password = "root123";

        Scanner sc = new Scanner(System.in);
        try{
//            Input
            System.out.println("Enter service id to delete");
            int id = sc.nextInt();

            // 2. Connect
            Connection con = DriverManager.getConnection(url, user, password);
//            3. SQL
            String sql = "DELETE FROM services WHERE service_id = ?";

            // 4. PrepareStatement
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            // 5. Execute delete
            int rows = ps.executeUpdate();

            if(rows > 0)
                System.out.println("Service deleted successfully!");
            else
                System.out.println("Service ID not found!");

            // 6. Close
            ps.close();
            con.close();
            sc.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}