package controller;

import model.Customer;
import service.CustomerService;

import java.util.List;

public class CustomerController {

    private final CustomerService customerService = new CustomerService();

    public int addCustomer(Customer customer) {
        return customerService.addCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
    }

    public void deleteCustomer(int customerId) {
        customerService.deleteCustomer(customerId);
    }

    public Customer findCustomerByPhone(String phone) {
        return customerService.findCustomerByPhone(phone);
    }
    public List<Customer> findCustomerByName(String name) {
        return customerService.findCustomerByName(name);
    }
    public Customer findCustomerByEmail(String email) {
        return customerService.findCustomerByEmail(email);
    }

    public List<Customer> getCustomersPaginated(int pageNumber , int pageSize)
    {
        return customerService.getCustomersPaginated(pageNumber , pageSize);
    }
    public int getTotalCustomerCount()
    {
        return customerService.getTotalCustomerCount();
    }
}
