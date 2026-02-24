package dao;
import exception.DatabaseException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import jdbc.DbConnectionUtil;

public class CustomerDAO {

    //  INSERT CUSTOMER
    public int insertCustomer(Customer customer) {

        String sql = "INSERT INTO customers (customer_name, email, phone, address, created_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS )
        ) {

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setDate(5, Date.valueOf(customer.getCreatedDate()));

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);   // returning  generated customer_id
            }

        }
        catch (SQLException e) {
            throw new DatabaseException("Error inserting customer into database", e);
        }
        return -1; // if insert failed
    }

    //  CHECK IF CUSTOMER EXISTS
    public boolean exists(int customerId) {

        String sql = "SELECT 1 FROM customers WHERE customer_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new DatabaseException("Error checking customer existence", e);
        }
    }

    //  GET ALL CUSTOMERS
    public List<Customer> getAllCustomers() {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customers";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));

                Date sqlDate = rs.getDate("created_date");
                if (sqlDate != null) {
                    customer.setCreatedDate(sqlDate.toLocalDate());
                }

                customers.add(customer);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching customers", e);
        }

        return customers;
    }

    //  UPDATE CUSTOMER
    public boolean updateCustomer(Customer customer) {

        String sql = "UPDATE customers SET "
                + "customer_name = ?, "
                + "email = ?, "
                + "phone = ?, "
                + "address = ? "
                + "WHERE customer_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setInt(5, customer.getCustomerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error updating customer", e);
        }
    }

    //  DELETE CUSTOMER
    public boolean deleteCustomer(int customerId) {

        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting customer", e);
        }
    }

    //  SEARCH BY PHONE (Very Important for Garage)
    public Customer findByPhone(String phone)
    {

        String sql = "SELECT * FROM customers WHERE phone = ?";

        try (
                Connection con = DbConnectionUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));

                Date sqlDate = rs.getDate("created_date");
                if (sqlDate != null) {
                    customer.setCreatedDate(sqlDate.toLocalDate());
                }

                return customer;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error searching customer by phone", e);
        }

        return null;
    }

//    method to find customers by their name
    public List<Customer> findByName(String name)
    {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE customer_name LIKE ?";
        try( Connection con = DbConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1 , "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setCreatedDate(rs.getDate("created_date").toLocalDate());

                list.add(customer);
            }
        }catch(SQLException e)
        {
            throw new DatabaseException("Error fetching Customer by Name" , e);
        }
        return list;
    }

    //    method to find customers by their name
    public Customer findByEmail(String email)
    {

        String sql = "SELECT * FROM customers WHERE email = ?";
        try( Connection con = DbConnectionUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1 , email);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
            {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                Date sqlDate = rs.getDate("created_date");
                if (sqlDate != null) {
                    customer.setCreatedDate(sqlDate.toLocalDate());
                }
                return  customer;
            }
        }catch(SQLException e)
        {
            throw new DatabaseException("Error fetching Customer by Email" , e);
        }
        return null;
    }
//    Method to get Paginated customers.
    public List<Customer> getCustomersPaginated(int pageNumber , int pageSize)
    {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id LIMIT ? OFFSET ?";

        try(
                Connection con = DbConnectionUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
        )
        {
          int offset = (pageNumber - 1) * pageSize;
          ps.setInt(1 , pageSize);
          ps.setInt(2 , offset);

          ResultSet rs = ps.executeQuery();

          while(rs.next())
          {
              Customer customer = new Customer();
              customer.setCustomerId(rs.getInt("customer_id"));
              customer.setCustomerName(rs.getString("customer_name"));
              customer.setEmail(rs.getString("email"));
              customer.setPhone(rs.getString("phone"));
              customer.setAddress(rs.getString("address"));
              customer.setCreatedDate(rs.getDate("created_date").toLocalDate());

              list.add(customer);

          }
        }catch(SQLException e)
        {
            throw new DatabaseException("Error fetching paginated customers" , e);
        }
        return list;
    }

//    We must know how many customers exist
    public int getTotalCustomerCount()
    {
        String sql = "SELECT COUNT(*) FROM customers";
        try(Connection con = DbConnectionUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
        {
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            {
            return rs.getInt(1);
            }

        }
        catch(SQLException e)
        {
        throw new DatabaseException("Error getting Customer Count" , e);

        }
        return 0;
    }
}
