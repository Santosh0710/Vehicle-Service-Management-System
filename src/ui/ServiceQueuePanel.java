package ui;

import controller.ServiceQueueController;
import exception.DuplicateBookingException;
import exception.VehicleAlreadyBookedException;
import model.ServiceBooking;
import model.ServiceType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServiceQueuePanel extends JPanel {

    private JTextField bookingIdField;
    private JTextField vehicleIdField;
    private JComboBox<ServiceType> serviceTypeBox;
    private JTextArea outputArea;

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

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));

        bookingIdField = new JTextField();
        vehicleIdField = new JTextField();
        serviceTypeBox = new JComboBox<>(ServiceType.values());

        formPanel.add(new JLabel("Booking ID:"));
        formPanel.add(bookingIdField);

        formPanel.add(new JLabel("Vehicle ID:"));
        formPanel.add(vehicleIdField);

        formPanel.add(new JLabel("Service Type:"));
        formPanel.add(serviceTypeBox);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBorder(BorderFactory.createTitledBorder("Queue Status"));

        JScrollPane scrollPane = new JScrollPane(outputArea);

        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton addBtn = new JButton("Add Booking");
        serveBtn = new JButton("Serve Next");
        completeBtn = new JButton("Complete Service");
        JButton viewPendingBtn = new JButton("View Pending");
        JButton viewCompletedBtn = new JButton("View Completed");
        JButton viewInProgressBtn = new JButton("View In Progress");


        buttonPanel.add(addBtn);
        buttonPanel.add(serveBtn);
        buttonPanel.add(viewPendingBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(viewCompletedBtn);
        buttonPanel.add(viewInProgressBtn);


        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Button Actions =====
        addBtn.addActionListener(e -> addBooking());
        serveBtn.addActionListener(e -> serveNext());
        viewPendingBtn.addActionListener(e -> showPending());
        completeBtn.addActionListener(e -> completeService());
        viewCompletedBtn.addActionListener(e -> showCompleted());
        viewInProgressBtn.addActionListener(e -> viewInProgressBookings());

        updateStats();
    }

    // =============================
    // ADD BOOKING
    // =============================
    private void addBooking() {

        try {
            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            int vehicleId = Integer.parseInt(vehicleIdField.getText().trim());
            ServiceType type = (ServiceType) serviceTypeBox.getSelectedItem();

            ServiceBooking booking =
                    new ServiceBooking(bookingId, vehicleId, type);

            controller.addBooking(booking);

            outputArea.append("Booking added successfully\n");

            bookingIdField.setText("");
            vehicleIdField.setText("");

            updateStats();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "IDs must be numeric",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (DuplicateBookingException | VehicleAlreadyBookedException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============================
    // SERVE NEXT
    // =============================
    private void serveNext() {

        ServiceBooking booking = controller.serveNext();

        if (booking == null) {
            outputArea.append("No available bays OR no waiting bookings.\n");
        } else {
            outputArea.append("Booking "
                    + booking.getBookingId()
                    + " assigned to Bay "
                    + booking.getBayId()
                    + "\n");
        }

        updateStats();
    }

    // =============================
    // COMPLETE SERVICE
    // =============================
    private void completeService()
    {
        String bookingText = bookingIdField.getText().trim();

        // 1️⃣ Check empty
        if (bookingText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Booking ID.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {

            int bookingId = Integer.parseInt(bookingText);
            ServiceBooking completed =
                    controller.completeService(bookingId);

            if (completed != null) {
                outputArea.append("Booking "
                        + bookingId
                        + " completed successfully.\n");
            } else {
                outputArea.append("No booking found for completion.\n");
            }

            updateStats();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Booking ID must be a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);

        }catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Completion Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =============================
    // VIEW PENDING
    // =============================
    private void showPending() {

        outputArea.setText("");

        List<ServiceBooking> pending =
                controller.getPendingBookings();

        if (pending.isEmpty()) {
            outputArea.append("No pending bookings\n");
            return;
        }

        outputArea.append("--- Pending Bookings ---\n");

        for (ServiceBooking booking : pending) {
            outputArea.append(booking.toString() + "\n");
        }
    }

    // =============================
    // VIEW COMPLETED
    // =============================
    private void showCompleted() {

        outputArea.setText("");

        List<ServiceBooking> completed =
                controller.getCompletedBookings();

        if (completed.isEmpty()) {
            outputArea.append("No completed services\n");
            return;
        }

        outputArea.append("--- Completed Services ---\n");

        for (ServiceBooking booking : completed) {
            outputArea.append(booking.toString() + "\n");
        }
    }

//  Method to view in- Progress Bookings
    private void viewInProgressBookings() {

        outputArea.setText("");

        List<ServiceBooking> inProgressBookings = controller.getInProgressBookings();

        if (inProgressBookings.isEmpty()) {
            outputArea.append("No In-Progress bookings.\n");
            return;
        }

        for (ServiceBooking booking : inProgressBookings) {
            outputArea.append("Booking Id: "
                    + booking.getBookingId()
                    + ", Vehicle Id: "
                    + booking.getVehicleId()
                    + ", Service: "
                    + booking.getServiceType()
                    + ", Status: "
                    + booking.getStatus()
                    + "\n");
        }
    }


    // =============================
    // UPDATE STATS
    // =============================
    private void updateStats() {

        int waiting = controller.getPendingBookings().size();
        int inProgress = controller.getInProgressBookings().size();
        int completed = controller.getCompletedBookings().size();

        waitingLabel.setText("Waiting: " + waiting);
        inProgressLabel.setText("In Progress: " + inProgress);
        completedLabel.setText("Completed: " + completed);
    }

}
