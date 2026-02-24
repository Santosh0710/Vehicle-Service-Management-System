package ui;

import controller.VehicleController;
import model.Vehicle;
import exception.BusinessException;
import exception.DatabaseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehicleManagementFrame extends JFrame {

    private JTextField customerIdField, numberField, typeField, brandField, modelField, yearField;
    private JTextField searchNumberField;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnFind;
    private JButton btnNext, btnPrev;
    private JLabel pageLabel;

    private VehicleController controller = new VehicleController();

    private Integer selectedVehicleId = null;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPages;

    public VehicleManagementFrame() {

        setTitle("Vehicle Management");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadVehicles();
        setVisible(true);
    }

    // ================= FORM =================
    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 5));

        customerIdField = new JTextField();
        numberField = new JTextField();
        typeField = new JTextField();
        brandField = new JTextField();
        modelField = new JTextField();
        yearField = new JTextField();

        panel.add(new JLabel("Customer ID:"));
        panel.add(customerIdField);

        panel.add(new JLabel("Vehicle Number:"));
        panel.add(numberField);

        panel.add(new JLabel("Vehicle Type:"));
        panel.add(typeField);

        panel.add(new JLabel("Brand:"));
        panel.add(brandField);

        panel.add(new JLabel("Model:"));
        panel.add(modelField);

        panel.add(new JLabel("Year:"));
        panel.add(yearField);

        // Search
        JPanel searchPanel = new JPanel();
        searchNumberField = new JTextField(15);
        btnFind = new JButton("Find by Number");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchNumberField);
        searchPanel.add(btnFind);

        btnFind.addActionListener(e -> findVehicle());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(searchPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel() {

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Customer ID", "Number", "Type", "Brand", "Model", "Year"}, 0
        );

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selectedVehicleId = (int) tableModel.getValueAt(row, 0);

                customerIdField.setText(tableModel.getValueAt(row, 1).toString());
                numberField.setText(tableModel.getValueAt(row, 2).toString());
                typeField.setText(tableModel.getValueAt(row, 3).toString());
                brandField.setText(tableModel.getValueAt(row, 4).toString());
                modelField.setText(tableModel.getValueAt(row, 5).toString());
                yearField.setText(tableModel.getValueAt(row, 6).toString());
            }
        });

        return new JScrollPane(table);
    }

    // ================= BUTTON PANEL =================
    private JPanel createButtonPanel() {

        JPanel panel = new JPanel();

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        btnPrev = new JButton("Previous");
        btnNext = new JButton("Next");
        pageLabel = new JLabel("Page 1");

        panel.add(btnPrev);
        panel.add(pageLabel);
        panel.add(btnNext);

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        btnAdd.addActionListener(e -> addVehicle());
        btnUpdate.addActionListener(e -> updateVehicle());
        btnDelete.addActionListener(e -> deleteVehicle());
        btnClear.addActionListener(e -> clearFields());

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadVehicles();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadVehicles();
            }
        });

        return panel;
    }

    // ================= LOAD =================
    private void loadVehicles() {
        try {
            List<Vehicle> list = controller.getVehiclesPaginated(currentPage, pageSize);

            int totalRecords = controller.getTotalVehicleCount();
            totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            tableModel.setRowCount(0);

            for (Vehicle v : list) {
                tableModel.addRow(new Object[]{
                        v.getVehicleId(),
                        v.getCustomerId(),
                        v.getVehicleNumber(),
                        v.getVehicleType(),
                        v.getBrand(),
                        v.getModel(),
                        v.getYear()
                });
            }

            pageLabel.setText("Page " + currentPage + " / " + totalPages);

            btnPrev.setEnabled(currentPage > 1);
            btnNext.setEnabled(currentPage < totalPages);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= FIND =================
    private void findVehicle() {

        String number = searchNumberField.getText();

        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter vehicle number");
            return;
        }

        try {
            List<Vehicle> list = controller.getAllVehicles();

            for (Vehicle v : list) {
                if (v.getVehicleNumber().equalsIgnoreCase(number)) {

                    selectedVehicleId = v.getVehicleId();

                    customerIdField.setText(String.valueOf(v.getCustomerId()));
                    numberField.setText(v.getVehicleNumber());
                    typeField.setText(v.getVehicleType());
                    brandField.setText(v.getBrand());
                    modelField.setText(v.getModel());
                    yearField.setText(String.valueOf(v.getYear()));

                    JOptionPane.showMessageDialog(this, "Vehicle Found");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Vehicle not found");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= ADD =================
    private void addVehicle() {

        try {
            Vehicle v = new Vehicle(
                    0,
                    Integer.parseInt(customerIdField.getText()),
                    numberField.getText(),
                    typeField.getText(),
                    brandField.getText(),
                    modelField.getText(),
                    Integer.parseInt(yearField.getText())
            );

            int id = controller.addVehicle(v);

            JOptionPane.showMessageDialog(this, "Vehicle Added ID: " + id);

            loadVehicles();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= UPDATE =================
    private void updateVehicle() {

        if (selectedVehicleId == null) {
            JOptionPane.showMessageDialog(this, "Select vehicle first");
            return;
        }

        try {
            Vehicle v = new Vehicle(
                    selectedVehicleId,
                    Integer.parseInt(customerIdField.getText()),
                    numberField.getText(),
                    typeField.getText(),
                    brandField.getText(),
                    modelField.getText(),
                    Integer.parseInt(yearField.getText())
            );

            controller.updateVehicle(v);

            JOptionPane.showMessageDialog(this, "Vehicle Updated");

            loadVehicles();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= DELETE =================
    private void deleteVehicle() {

        if (selectedVehicleId == null) {
            JOptionPane.showMessageDialog(this, "Select vehicle first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this vehicle?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.deleteVehicle(selectedVehicleId);

            JOptionPane.showMessageDialog(this, "Deleted");

            loadVehicles();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= CLEAR =================
    private void clearFields() {
        customerIdField.setText("");
        numberField.setText("");
        typeField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        selectedVehicleId = null;
    }
}