package ui;

import controller.CustomerController;
import exception.BusinessException;
import exception.DatabaseException;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class CustomerManagementFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField createdDateField;
    private JTextField searchPhoneField;
    private JTextField searchNameField;
    private JTextField searchEmailField;
    private JButton searchNameButton;
    private JButton searchEmailButton;
    private JButton resetButton;

    private Integer selectedCustomerId = null;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPages;

    private JButton btnNext;
    private JButton btnPrev;
    private JLabel pageLabel;

    private JTable customerTable;
    private DefaultTableModel tableModel;

    private final CustomerController controller = new CustomerController();

    public CustomerManagementFrame() {

        setTitle("Customer Management");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadCustomers();
        setVisible(true);
    }

    // ================= FORM PANEL =================
    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formFields = new JPanel(new GridLayout(5, 2, 10, 5));

        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();
        createdDateField = new JTextField(LocalDate.now().toString());
        createdDateField.setEditable(false);

        formFields.add(new JLabel("Name:"));
        formFields.add(nameField);

        formFields.add(new JLabel("Email:"));
        formFields.add(emailField);

        formFields.add(new JLabel("Phone:"));
        formFields.add(phoneField);

        formFields.add(new JLabel("Address:"));
        formFields.add(addressField);

        formFields.add(new JLabel("Created Date:"));
        formFields.add(createdDateField);

        JPanel searchPanel = new JPanel();
        searchPhoneField = new JTextField(15);
        searchNameField = new JTextField(15);
        searchEmailField = new JTextField(15);
        JButton searchPhoneButton = new JButton("Find by Phone");
        searchNameButton = new JButton("Search by Name");
        searchEmailButton = new JButton("Search by Email");
        resetButton = new JButton("Reset");

        searchPanel.add(new JLabel("Search Phone:"));
        searchPanel.add(searchPhoneField);
        searchPanel.add(searchPhoneButton);

        searchPhoneButton.addActionListener(e -> findCustomer());

        searchPanel.add(new JLabel("Search By Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(searchNameButton);

        //-> Action listener of search name
        searchNameButton.addActionListener(e -> {
            try {
                String name = searchNameField.getText();

                List<Customer> customers = controller.findCustomerByName(name);

                loadTableData(customers);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        searchPanel.add(new JLabel("Search By Email:"));
        searchPanel.add(searchEmailField);
        searchPanel.add(searchEmailButton);

        // Action Listener of search email.
        searchEmailButton.addActionListener(e -> {
            try {
                String email = searchEmailField.getText();

                Customer customer = controller.findCustomerByEmail(email);

                List<Customer> list = new ArrayList<>();

                if (customer != null) {
                    list.add(customer);
                }

                loadTableData(list);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        searchPanel.add(resetButton);

        resetButton.addActionListener(e -> {
            currentPage = 1;
            loadCustomers(); // your existing pagination method
        });

        panel.add(formFields, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ================= TABLE PANEL =================
    private JScrollPane createTablePanel() {

        String[] columns = {
                "ID", "Name", "Email", "Phone", "Address", "Created Date"
        };

        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                selectedCustomerId = (Integer) tableModel.getValueAt(row, 0);
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                emailField.setText(tableModel.getValueAt(row, 2).toString());
                phoneField.setText(tableModel.getValueAt(row, 3).toString());
                addressField.setText(tableModel.getValueAt(row, 4).toString());
                createdDateField.setText(tableModel.getValueAt(row, 5).toString());
            }
        });

        return new JScrollPane(customerTable);
    }

    // ================= BUTTON PANEL =================
    private JPanel createButtonPanel() {

        JPanel panel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        btnPrev = new JButton("Previous");
        btnNext = new JButton("Next");
        pageLabel = new JLabel("Page 1");

        panel.add(btnPrev);
        panel.add(btnNext);
        panel.add(pageLabel);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadCustomers();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadCustomers();
            }
        });

        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearFields());

        return panel;
    }

    // ================= LOAD CUSTOMERS =================
    private void loadCustomers() {

        try {

            List<Customer> customers =
                    controller.getCustomersPaginated(currentPage, pageSize);

            int totalRecords =
                    controller.getTotalCustomerCount();

            totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            tableModel.setRowCount(0);

            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                        c.getCustomerId(),
                        c.getCustomerName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getAddress(),
                        c.getCreatedDate()
                });
            }

            pageLabel.setText("Page " + currentPage + " of " + totalPages);

            btnPrev.setEnabled(currentPage > 1);
            btnNext.setEnabled(currentPage < totalPages);

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
//    Method to load customers when searched by phone , name or email.
    private void loadTableData(List<Customer> customers) {
        tableModel.setRowCount(0); // clear old data

            for (Customer c : customers)
            {
                tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getCustomerName(),
                c.getEmail(),c.getPhone(),
                c.getAddress(),
                c.getCreatedDate()
                });
             }
}

    // ================= FIND =================
    private void findCustomer() {

        String phone = searchPhoneField.getText().trim();

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter phone number");
            return;
        }

        try {

            Customer customer = controller.findCustomerByPhone(phone);

            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found");
                return;
            }

            selectedCustomerId = customer.getCustomerId();

            nameField.setText(customer.getCustomerName());
            emailField.setText(customer.getEmail());
            phoneField.setText(customer.getPhone());
            addressField.setText(customer.getAddress());
            createdDateField.setText(customer.getCreatedDate().toString());

            JOptionPane.showMessageDialog(this, "Customer Found");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= ADD =================
    private void addCustomer() {

        try {

            Customer customer = new Customer();
            customer.setCustomerName(nameField.getText());
            customer.setEmail(emailField.getText());
            customer.setPhone(phoneField.getText());
            customer.setAddress(addressField.getText());
            customer.setCreatedDate(LocalDate.now());

            int id = controller.addCustomer(customer);

            JOptionPane.showMessageDialog(this,
                    "Customer Added with ID: " + id);

            loadCustomers();
            clearFields();

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, "System error.");
        }
    }

    // ================= UPDATE =================
    private void updateCustomer() {

        if (selectedCustomerId == null) {
            JOptionPane.showMessageDialog(this,
                    "Select a customer first");
            return;
        }

        try {

            Customer customer = new Customer();
            customer.setCustomerId(selectedCustomerId);
            customer.setCustomerName(nameField.getText());
            customer.setEmail(emailField.getText());
            customer.setPhone(phoneField.getText());
            customer.setAddress(addressField.getText());
            customer.setCreatedDate(LocalDate.parse(createdDateField.getText()));

            controller.updateCustomer(customer);

            JOptionPane.showMessageDialog(this, "Customer Updated");

            loadCustomers();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= DELETE =================
    private void deleteCustomer() {

        if (selectedCustomerId == null) {
            JOptionPane.showMessageDialog(this,
                    "Select a customer first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this customer?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        controller.deleteCustomer(selectedCustomerId);

        JOptionPane.showMessageDialog(this, "Customer Deleted");

        loadCustomers();
        clearFields();
    }

    // ================= CLEAR =================
    private void clearFields() {

        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        createdDateField.setText(LocalDate.now().toString());
        selectedCustomerId = null;
        searchPhoneField.setText("");
        searchEmailField.setText("");
        searchNameField.setText("");
    }
}