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

    // ================= CONSTRUCTOR =================
    public CustomerManagementFrame() {

        setTitle("Customer Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        topPanel.add(createFormPanel(), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadCustomers();

        setVisible(true);
    }

    // ================= HEADER PANEL =================
    private JPanel createHeaderPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JButton backBtn = new RoundedButton("← Back");
        backBtn.addActionListener(e -> dispose());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(backBtn);

        JLabel title = new JLabel("Customer Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0, 70, 140));

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    // ================= FORM PANEL =================
    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel formFields = new JPanel(new GridLayout(5, 2, 15, 10));
        formFields.setBorder(BorderFactory.createTitledBorder("Customer Details"));

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

        // ===== Search Panel =====

        JPanel searchPanel = new JPanel();
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Customer"));

        searchPhoneField = new JTextField(12);
        searchNameField = new JTextField(12);
        searchEmailField = new JTextField(12);

        JButton searchPhoneButton = new RoundedButton("Find by Phone");
        searchNameButton = new RoundedButton("Search by Name");
        searchEmailButton = new RoundedButton("Search by Email");

        resetButton = new RoundedButton("Reset");

        searchPanel.add(new JLabel("Phone:"));
        searchPanel.add(searchPhoneField);
        searchPanel.add(searchPhoneButton);

        searchPanel.add(new JLabel("Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(searchNameButton);

        searchPanel.add(new JLabel("Email:"));
        searchPanel.add(searchEmailField);
        searchPanel.add(searchEmailButton);

        searchPanel.add(resetButton);

        // ===== ACTIONS =====

        searchPhoneButton.addActionListener(e -> findCustomer());

        searchNameButton.addActionListener(e -> {
            try {

                String name = searchNameField.getText();
                List<Customer> customers = controller.findCustomerByName(name);
                loadTableData(customers);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

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

        resetButton.addActionListener(e -> {

            currentPage = 1;
            loadCustomers();

        });

        panel.add(formFields, BorderLayout.NORTH);
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

        customerTable.setRowHeight(25);
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        customerTable.setSelectionBackground(new Color(180, 220, 255));

        customerTable.getSelectionModel().addListSelectionListener(e -> {

            int row = customerTable.getSelectedRow();

            if (row >= 0) {

                selectedCustomerId =
                        (Integer) tableModel.getValueAt(row, 0);

                nameField.setText(
                        tableModel.getValueAt(row, 1).toString());

                emailField.setText(
                        tableModel.getValueAt(row, 2).toString());

                phoneField.setText(
                        tableModel.getValueAt(row, 3).toString());

                addressField.setText(
                        tableModel.getValueAt(row, 4).toString());

                createdDateField.setText(
                        tableModel.getValueAt(row, 5).toString());
            }
        });

        return new JScrollPane(customerTable);
    }

    // ================= BUTTON PANEL =================
    private JPanel createButtonPanel() {

        JPanel panel =
                new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton addButton = new RoundedButton("Add");
        JButton updateButton = new RoundedButton("Update");
        JButton deleteButton = new RoundedButton("Delete");
        JButton clearButton = new RoundedButton("Clear");

        btnPrev = new RoundedButton("Previous");
        btnNext = new RoundedButton("Next");

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

            totalPages =
                    (int) Math.ceil((double) totalRecords / pageSize);

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

            pageLabel.setText(
                    "Page " + currentPage + " of " + totalPages);

            btnPrev.setEnabled(currentPage > 1);
            btnNext.setEnabled(currentPage < totalPages);

        } catch (BusinessException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= LOAD SEARCH DATA =================
    private void loadTableData(List<Customer> customers) {

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
    }

    // ================= FIND BY PHONE =================
    private void findCustomer() {

        String phone = searchPhoneField.getText().trim();

        if (phone.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Enter phone number");

            return;
        }

        try {

            Customer customer =
                    controller.findCustomerByPhone(phone);

            if (customer == null) {

                JOptionPane.showMessageDialog(this,
                        "Customer not found");

                return;
            }

            selectedCustomerId = customer.getCustomerId();

            nameField.setText(customer.getCustomerName());
            emailField.setText(customer.getEmail());
            phoneField.setText(customer.getPhone());
            addressField.setText(customer.getAddress());
            createdDateField.setText(
                    customer.getCreatedDate().toString());

            JOptionPane.showMessageDialog(this,
                    "Customer Found");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }

    // ================= ADD CUSTOMER =================
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

            JOptionPane.showMessageDialog(this,
                    e.getMessage());

        } catch (DatabaseException e) {

            JOptionPane.showMessageDialog(this,
                    "System error.");
        }
    }

    // ================= UPDATE CUSTOMER =================
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

            customer.setCreatedDate(
                    LocalDate.parse(createdDateField.getText()));

            controller.updateCustomer(customer);

            JOptionPane.showMessageDialog(this,
                    "Customer Updated");

            loadCustomers();
            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }

    // ================= DELETE CUSTOMER =================
    private void deleteCustomer() {

        if (selectedCustomerId == null) {

            JOptionPane.showMessageDialog(this,
                    "Select a customer first");

            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete this customer?",
                        "Confirm",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirm != JOptionPane.YES_OPTION)
            return;

        controller.deleteCustomer(selectedCustomerId);

        JOptionPane.showMessageDialog(this,
                "Customer Deleted");

        loadCustomers();
        clearFields();
    }

    // ================= CLEAR FIELDS =================
    private void clearFields() {

        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");

        createdDateField.setText(LocalDate.now().toString());

        selectedCustomerId = null;

        searchPhoneField.setText("");
        searchNameField.setText("");
        searchEmailField.setText("");
    }
}