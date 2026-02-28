package dao;

import jdbc.DbConnectionUtil;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO
{
   public User login(String username , String password) throws SQLException
   {
       String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
       try(Connection con = DbConnectionUtil.getConnection();
       PreparedStatement ps = con.prepareStatement(sql))
       {
           ps.setString(1 , username);
           ps.setString(2 , password);

           ResultSet rs = ps.executeQuery();

           if(rs.next())
           {
               return new User(rs.getInt("user_id"),
                       rs.getString("username"),
                       rs.getString("password"),
                       rs.getString("role")
               );
           }
       }
       return null; // This represents invalid login
   }
}
