package dbsetup_practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/vehicle_service_db";
        String username = "root";
        String password = "root123";

        try {
            Connection con = DriverManager.getConnection(url, username, password);

            Statement stmt = con.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS customers (" +
                    "id INT PRIMARY KEY, " +
                    "name VARCHAR(50))";

            stmt.executeUpdate(sql);

            System.out.println("Customers table created successfully!");

            con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
