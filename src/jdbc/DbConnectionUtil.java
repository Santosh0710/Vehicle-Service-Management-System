package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnectionUtil {
    private static final String URL  = "jdbc:mysql://localhost:3306/vehicle_service_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";

//    this is what DAO will use
    public static Connection getConnection(){
        Connection con = null;
        try{
            con = DriverManager.getConnection(URL , USERNAME, PASSWORD);

        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
//    this main method is OPTIONAL (for testing only)
public static void main(String[] args) {
        try {
            Connection con = getConnection();
            if (con != null) {
                System.out.println("Database Connected Successfully");
                con.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
}
}