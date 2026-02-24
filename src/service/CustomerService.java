package service;

import dao.CustomerDAO;
import exception.BusinessException;
import model.Customer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomerService {

    private final CustomerDAO customerDAO = new CustomerDAO();

    //  ADD CUSTOMER
    public int addCustomer(Customer customer) {

        // RULE 1: Name cannot be empty
        if (customer.getCustomerName() == null || customer.getCustomerName().trim().isEmpty()) {
            throw new BusinessException("Customer name cannot be empty");
        }

        // RULE 2: Phone cannot be empty
        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new BusinessException("Phone number cannot be empty");
        }

        // RULE 3: Basic email validation (if provided)
        if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
            if (!customer.getEmail().contains("@")) {
                throw new BusinessException("Invalid email format");
            }
        }

        // RULE 4: Prevent duplicate phone numbers
        if (customerDAO.findByPhone(customer.getPhone()) != null) {
            throw new BusinessException("Customer with this phone number already exists");
        }

        // RULE 5: Set created date if not set
        if (customer.getCreatedDate() == null) {
            customer.setCreatedDate(LocalDate.now());
        }

        // Save and return generated ID
        int generatedId = customerDAO.insertCustomer(customer);

        if (generatedId == -1) {
            throw new RuntimeException("Failed to insert customer");
        }

        return generatedId;
    }

    //  GET ALL CUSTOMERS
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    // UPDATE CUSTOMER
    public void updateCustomer(Customer customer) {

        if (customer.getCustomerId() <= 0) {
            throw new BusinessException("Customer ID must be valid");
        }

        if (customer.getCustomerName() == null || customer.getCustomerName().trim().isEmpty()) {
            throw new BusinessException("Customer name cannot be empty");
        }

        if (customer.getPhone() == null || customer.getPhone().trim().isEmpty()) {
            throw new BusinessException("Phone number cannot be empty");
        }

        boolean updated = customerDAO.updateCustomer(customer);

        if (!updated) {
            throw new RuntimeException("Customer not found with ID: " + customer.getCustomerId());
        }
    }

    // DELETE CUSTOMER
    public void deleteCustomer(int customerId) {

        if (customerId <= 0) {
            throw new BusinessException("Customer ID must be valid");
        }

        boolean deleted = customerDAO.deleteCustomer(customerId);

        if (!deleted) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
    }

    //  SEARCH BY PHONE
    public Customer findCustomerByPhone(String phone) {

        if (phone == null || phone.trim().isEmpty()) {
            throw new BusinessException("Phone number cannot be empty");
        }
            return customerDAO.findByPhone(phone);

    }
//    SEARCH BY NAME
    public List<Customer> findCustomerByName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new BusinessException("Name cannot be empty");
        }
        return customerDAO.findByName(name);
    }

//    SEARCH BY EMAIL
    public Customer findCustomerByEmail(String email)
    {
        if (email == null || email.trim().isEmpty())
        {
            throw new BusinessException("Email cannot be empty");
        }

        return customerDAO.findByEmail(email);
    }

//    View Customers in Pages

    public List<Customer> getCustomersPaginated(int pageNumber , int pageSize)
    {
        if(pageNumber<= 0)
        {
            throw new BusinessException("PageNumber must be greater than 0");
        }
        if(pageSize <= 0 || pageSize > 100)
        {
            throw new BusinessException("PageSize must be between 1 and 100");
        }
        int totalRecords = customerDAO.getTotalCustomerCount();
        if (totalRecords == 0)
        {
            return Collections.emptyList();
        }

        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        if(pageNumber > totalPages)
        {
            throw new BusinessException("PageNumber exceeds Total Pages");
        }
        return customerDAO.getCustomersPaginated(pageNumber , pageSize);
    }

//    Count the total number of customerw
    public int getTotalCustomerCount()
    {
        int count = customerDAO.getTotalCustomerCount();
        if(count < 0)
        {
            throw new BusinessException("Invalid Customer Count Received");
        }
        return count;
    }
}
