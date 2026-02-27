package ui;

import controller.ServiceQueueController;
import exception.BusinessException;
import exception.DatabaseException;
import exception.DuplicateBookingException;
import exception.VehicleAlreadyBookedException;
import exception.InvalidInputException;
import exception.BookingNotFoundException;
import model.ServiceBooking;
import model.ServiceType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class ServiceQueuePanel extends JPanel {

    private JTextField bookingIdField;
    private JTextField vehicleIdField;
    private JComboBox<ServiceType> serviceTypeBox;
    private JTextField bookingDateField;

    private JTable table;
    private DefaultTableModel tableModel;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPages;

    private JButton btnNext;
    private JButton btnPrev;
    private JLabel pageLabel;

    private List<ServiceBooking> currentList = new ArrayList<>();

    private JButton serveBtn;
    private JButton completeBtn;

    private JLabel waitingLabel;
    private JLabel inProgressLabel;
    private JLabel completedLabel;

    private final ServiceQueueController controller;

    public ServiceQueuePanel(ServiceQueueController controller) {

        this.controller = controller;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== Title =====
        JLabel title = new JLabel("Service Queue Management", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0, 70, 140));

        // ===== Stats Panel =====
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));

        waitingLabel = new JLabel("Waiting: 0");
        inProgressLabel = new JLabel("In Progress: 0");
        completedLabel = new JLabel("Completed: 0");

        waitingLabel.setForeground(Color.BLUE);
        inProgressLabel.setForeground(Color.ORANGE);
        completedLabel.setForeground(new Color(0, 128, 0));

        statsPanel.add(waitingLabel);
        statsPanel.add(inProgressLabel);
        statsPanel.add(completedLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));

        bookingIdField = new JTextField();
        vehicleIdField = new JTextField();
        serviceTypeBox = new JComboBox<>(ServiceType.values());
        bookingDateField = new JTextField(LocalDate.now().toString());
        bookingDateField.setEditable(false);

        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(bookingIdField);

        formPanel.add(new JLabel("Vehicle ID:"));
        formPanel.add(vehicleIdField);

        formPanel.add(new JLabel("Service Type:"));
        formPanel.add(serviceTypeBox);

        formPanel.add(new JLabel("Booking Date:"));
        formPanel.add(bookingDateField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {
                "Booking ID", "Vehicle ID", "Service Type",
                "Status", "Booking Date"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    bookingIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    vehicleIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());

                    // Set Service Type (Enum)
                    String serviceTypeStr = tableModel.getValueAt(selectedRow, 2).toString();
                    serviceTypeBox.setSelectedItem(ServiceType.valueOf(serviceTypeStr));

                    // Set Booking Date
                    Object dateObj = tableModel.getValueAt(selectedRow, 4);
                    if (dateObj != null) {
                        bookingDateField.setText(dateObj.toString());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Queue Status"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton addBtn = new JButton("Add Booking");
        serveBtn = new JButton("Serve Next");
        completeBtn = new JButton("Complete Service");
        JButton viewPendingBtn = new JButton("View Pending");
        JButton viewCompletedBtn = new JButton("View Completed");
        JButton viewInProgressBtn = new JButton("View In Progress");
        JButton updateButton = new JButton("Update Booking");
        JButton deleteBtn = new JButton("Delete Booking");
        JButton viewAllBtn = new JButton("View All");


        btnPrev = new JButton("Previous");
        btnNext = new JButton("Next");
        pageLabel = new JLabel("Page 1");

        buttonPanel.add(btnPrev);
        buttonPanel.add(btnNext);
        buttonPanel.add(pageLabel);
        buttonPanel.add(addBtn);
        buttonPanel.add(serveBtn);
        buttonPanel.add(viewPendingBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(viewCompletedBtn);
        buttonPanel.add(viewInProgressBtn);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewAllBtn);

        //deleteBtn.setEnabled(false);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        addBtn.addActionListener(e -> addBooking());
        serveBtn.addActionListener(e -> serveNext());
        viewPendingBtn.addActionListener(e -> showPending());
        completeBtn.addActionListener(e -> completeService());
        viewCompletedBtn.addActionListener(e -> showCompleted());
        viewInProgressBtn.addActionListener(e -> viewInProgressBookings());
        updateButton.addActionListener(e -> updateBooking());
        deleteBtn.addActionListener(e -> deleteBooking());
        viewAllBtn.addActionListener(e -> loadAllBookings());

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadTableData(currentList);
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadTableData(currentList);
            }
        });

        updateStats();
    }

    // ================= TABLE =================
    private void loadTableData(List<ServiceBooking> list) {

        currentList = list;

        int totalRecords = list.size();
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        tableModel.setRowCount(0);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecords);

        for (ServiceBooking b : currentList) {

            tableModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getVehicleId(),
                    b.getServiceType(),
                    b.getStatus(),
                    b.getBookingDate()
            });
        }

        pageLabel.setText("Page " + currentPage + " of " + totalPages);
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);
    }

    // ================= ADD =================
    private void addBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
            ServiceType type = (ServiceType) serviceTypeBox.getSelectedItem();

            ServiceBooking booking =
                    new ServiceBooking(bookingId, vehicleId, type, null, null, LocalDate.now());

            controller.addBooking(booking);

            JOptionPane.showMessageDialog(this, "Booking added successfully");

            bookingIdField.setText("");
            vehicleIdField.setText("");

            showPending();
            updateStats();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be numeric");
        } catch (DuplicateBookingException | VehicleAlreadyBookedException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (BusinessException | DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
//    Update Booking Method

    private void updateBooking() {
        try {
            if (bookingIdField.getText().trim().isEmpty()) {
                throw new InvalidInputException("Booking ID is required");
            }

            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());

            ServiceType type = (ServiceType) serviceTypeBox.getSelectedItem();
            String bookingDateStr = bookingDateField.getText().trim();
            LocalDate bookingDate = LocalDate.parse(bookingDateStr);

            if (bookingDate == null) {
                throw new InvalidInputException("Booking date cannot be empty");
            }

            ServiceBooking booking = new ServiceBooking(bookingId, vehicleId, type);
            booking.setBookingDate(bookingDate);

            controller.updateBooking(booking);

            JOptionPane.showMessageDialog(this, "Booking Updated Successfully!");

            List<ServiceBooking> bookings = controller.getPendingBookings();
            loadTableData(bookings);

        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());

        }catch (BookingNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format");

        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected Error Occurred");
            ex.printStackTrace(); // for developer
        }
    }

//    Method for deleting the booking
private void deleteBooking() {

    try {
        if (bookingIdField.getText().trim().isEmpty()) {
            throw new InvalidInputException("Select a booking to delete");
        }

        int bookingId = Integer.parseInt(bookingIdField.getText().trim());

        // 🔥 Confirmation Dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this booking?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        controller.deleteBooking(bookingId);

        JOptionPane.showMessageDialog(this, "Booking Deleted Successfully!");

        // Refresh current view
        loadAllBookings(); // or your current filter method


    } catch (InvalidInputException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());

    } catch (BookingNotFoundException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid Booking ID");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error deleting booking");
        ex.printStackTrace();
    }
}

    // ================= SERVE =================
    private void serveNext() {
        try {
            ServiceBooking booking = controller.serveNext();

            if (booking == null) {
                JOptionPane.showMessageDialog(this,
                        "No available bays or no waiting bookings.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Booking " + booking.getBookingId() +
                                " assigned to Bay " + booking.getBayId());
            }

            showPending();
            updateStats();

        } catch (BusinessException | DatabaseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // ================= COMPLETE =================
    private void completeService() {

        String bookingText = bookingIdField.getText().trim();

        if (bookingText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Booking ID");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingText);

            ServiceBooking completed = controller.completeService(bookingId);

            if (completed == null) {
                JOptionPane.showMessageDialog(this, "No booking found.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Booking " + bookingId + " completed.");
            }

            showPending();
            updateStats();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Booking ID must be a number.");
        } catch (BusinessException | DatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= VIEW =================
    private void showPending() {
        try {
            currentPage = 1;
            loadTableData(controller.getPendingBookings());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void showCompleted() {
        try {
            currentPage = 1;
            loadTableData(controller.getCompletedBookings());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void viewInProgressBookings() {
        try {
            currentPage = 1;
            loadTableData(controller.getInProgressBookings());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
//    Method for loading all the bookings
private void loadAllBookings() {
    try {
        List<ServiceBooking> bookings = controller.getAllBookings();
        loadTableData(bookings);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading bookings");
        e.printStackTrace(); // for debugging
    }
}

    // ================= STATS =================
    private void updateStats() {
        try {
            int waiting = controller.getPendingBookings().size();
            int inProgress = controller.getInProgressBookings().size();
            int completed = controller.getCompletedBookings().size();

            waitingLabel.setText("Waiting: " + waiting);
            inProgressLabel.setText("In Progress: " + inProgress);
            completedLabel.setText("Completed: " + completed);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading stats");
        }
    }
}