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

    private JTextField startDateField;
    private JTextField endDateField;
    private RoundedButton filterBtn;

    private JTable table;
    private DefaultTableModel tableModel;

    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalPages;

    private RoundedButton btnNext;
    private RoundedButton btnPrev;
    private JLabel pageLabel;

    private List<ServiceBooking> currentList = new ArrayList<>();

    private RoundedButton serveBtn;
    private RoundedButton completeBtn;

    private JLabel waitingLabel;
    private JLabel inProgressLabel;
    private JLabel completedLabel;

    private RoundedButton updateBtn;
    private RoundedButton deleteBtn;

//    this onBack variable is used here for going back to default window in MainAppLauncher.
    private Runnable onBack;


    private final ServiceQueueController controller;

    public ServiceQueuePanel(ServiceQueueController controller, Runnable onBack) {

        this.controller = controller;
        this.onBack = onBack;

        setLayout(new BorderLayout(20, 20));
        setPreferredSize(new Dimension(1100, 700));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= HEADER =================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        RoundedButton backBtn = new RoundedButton("← Back");
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBackground(new Color(230, 230, 230));

        backBtn.addActionListener(e -> {
            if (onBack != null) {
                onBack.run();
            }
        });

        JLabel title = new JLabel("Service Queue Management", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 70, 140));

        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);

        // ================= STAT CARDS =================
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        waitingLabel = createStatCard("Waiting", Color.BLUE);
        inProgressLabel = createStatCard("In Progress", Color.ORANGE);
        completedLabel = createStatCard("Completed", new Color(0, 150, 0));

        statsPanel.add(waitingLabel);
        statsPanel.add(inProgressLabel);
        statsPanel.add(completedLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

        bookingIdField = new JTextField();
        vehicleIdField = new JTextField();
        serviceTypeBox = new JComboBox<>(ServiceType.values());

        bookingDateField = new JTextField(LocalDate.now().toString());
        bookingDateField.setEditable(false);

        formPanel.add(new JLabel("Booking ID"));
        formPanel.add(new JLabel("Vehicle ID"));
        formPanel.add(new JLabel("Service Type"));
        formPanel.add(new JLabel("Booking Date"));

        formPanel.add(bookingIdField);
        formPanel.add(vehicleIdField);
        formPanel.add(serviceTypeBox);
        formPanel.add(bookingDateField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // ================= FILTER PANEL =================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        filterBtn = new RoundedButton("Filter");

        startDateField.setText(LocalDate.now().minusDays(7).toString());
        endDateField.setText(LocalDate.now().toString());

        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Bookings"));

        filterPanel.add(new JLabel("Start Date"));
        filterPanel.add(startDateField);

        filterPanel.add(new JLabel("End Date"));
        filterPanel.add(endDateField);

        filterPanel.add(filterBtn);

        centerPanel.add(filterPanel, BorderLayout.SOUTH);

        // ================= TABLE =================
        String[] columns = {
                "Booking ID", "Vehicle ID", "Service Type",
                "Status", "Booking Date", "Completed At"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(184, 207, 229));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

//       Selection model of table that enables autofill of form fields.
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {

                    bookingIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    vehicleIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());

                    // Service Type Enum
                    String serviceTypeStr = tableModel.getValueAt(selectedRow, 2).toString();
                    serviceTypeBox.setSelectedItem(ServiceType.valueOf(serviceTypeStr));

                    // Booking Date
                    Object dateObj = tableModel.getValueAt(selectedRow, 4);

                    if (dateObj != null) {
                        bookingDateField.setText(dateObj.toString());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Service Queue"));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================
        JPanel buttonPanel = new JPanel(new GridLayout(2, 6, 10, 10));

        RoundedButton addBtn = new RoundedButton("Add Booking");
        serveBtn = new RoundedButton("Serve Next");
        completeBtn = new RoundedButton("Complete Service");

        RoundedButton viewPendingBtn = new RoundedButton("View Pending");
        RoundedButton viewCompletedBtn = new RoundedButton("View Completed");
        RoundedButton viewInProgressBtn = new RoundedButton("View In Progress");

        updateBtn = new RoundedButton("Update");
        deleteBtn = new RoundedButton("Delete");
        RoundedButton viewAllBtn = new RoundedButton("View All");

        btnPrev = new RoundedButton("Previous");
        btnNext = new RoundedButton("Next");
        pageLabel = new JLabel("Page 1", JLabel.CENTER);

        buttonPanel.add(addBtn);
        buttonPanel.add(serveBtn);
        buttonPanel.add(completeBtn);
        buttonPanel.add(viewPendingBtn);
        buttonPanel.add(viewCompletedBtn);
        buttonPanel.add(viewInProgressBtn);

        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(btnPrev);
        buttonPanel.add(pageLabel);
        buttonPanel.add(btnNext);

        add(buttonPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        addBtn.addActionListener(e -> addBooking());
        serveBtn.addActionListener(e -> serveNext());
        viewPendingBtn.addActionListener(e -> showPending());
        completeBtn.addActionListener(e -> completeService());
        viewCompletedBtn.addActionListener(e -> showCompleted());
        viewInProgressBtn.addActionListener(e -> viewInProgressBookings());
        updateBtn.addActionListener(e -> updateBooking());
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

        filterBtn.addActionListener(e -> filterByDate());

        updateStats();
    }

//    Stats Card
    private JLabel createStatCard(String title, Color color)
    {

        JLabel label = new JLabel(title + ": 0", JLabel.CENTER);
        label.setOpaque(true);

        label.setBackground(new Color(245, 245, 245));
        label.setForeground(color);

        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        return label;
    }

//
    // ================= TABLE =================
    private void loadTableData(List<ServiceBooking> list) {

        currentList = list;

        int totalRecords = list.size();
        totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        tableModel.setRowCount(0);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRecords);

        for (int i = start; i < end; i++) {
            ServiceBooking b = currentList.get(i);
            tableModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getVehicleId(),
                    b.getServiceType(),
                    b.getStatus(),
                    b.getBookingDate(),
                    b.getCompletedAt()
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
                    new ServiceBooking(bookingId, vehicleId, type, null, null, LocalDate.now() , null);

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
    public void applyRole(String role) {

        if ("STAFF".equalsIgnoreCase(role)) {
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
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

// METHOD FOR GETTING BOOKINGS BY DATE
private void filterByDate() {
    try {
        LocalDate start = LocalDate.parse(startDateField.getText().trim());
        LocalDate end = LocalDate.parse(endDateField.getText().trim());

        currentPage = 1;
        List<ServiceBooking> list = controller.getBookingsBetweenDates(start, end);
        loadTableData(list);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD");
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