package ui;

import dao.ServiceBookingDAO;
import dao.ServiceBayDAO;
import service.ServiceQueueService;
import controller.ServiceQueueController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class MainAppLauncher extends JFrame {

    private final ServiceBookingDAO bookingDAO = new ServiceBookingDAO();
    private final ServiceBayDAO bayDAO = new ServiceBayDAO();
    private final ServiceQueueService serviceQueueService =
            new ServiceQueueService(bookingDAO, bayDAO);
    private final ServiceQueueController serviceQueueController =
            new ServiceQueueController(serviceQueueService);

    private JPanel contentPanel;
    private JPanel sidebar;

    private JButton selectedButton;

    private User currentUser;
    private String role;

    //  COLORS DEFINED BY ME
    private final Color sidebarColor = new Color(30, 41, 59);
    private final Color buttonColor = new Color(51, 65, 85);
    private final Color hoverColor = new Color(71, 85, 105);
    private final Color selectedColor = new Color(59, 130, 246);

//    Main Constructor

    public MainAppLauncher(User user) {

        this.currentUser = user;
        this.role = user.getRole();

        setTitle("Vehicle Service Management System");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(59, 130, 246));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Vehicle Service Management System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel userLabel = new JLabel("Logged in as: " + role);
        userLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.add(userLabel);
        rightPanel.add(logoutBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        // ================= SIDEBAR =================

        sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 15));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(sidebarColor);
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton customerButton = createSidebarButton("Customer Management");
        JButton vehicleButton = createSidebarButton("Vehicle Management");
        JButton serviceQueueBtn = createSidebarButton("Service Queue");
        JButton exitButton = createSidebarButton("Exit");

        sidebar.add(customerButton);
        sidebar.add(vehicleButton);
        sidebar.add(serviceQueueBtn);
        sidebar.add(new JLabel());
        sidebar.add(exitButton);

        // ================= CONTENT PANEL =================

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 242, 245));

        // ================= ACTIONS =================


        customerButton.addActionListener(e -> {
            setSelectedButton(customerButton);
            new CustomerManagementFrame();
        });

        vehicleButton.addActionListener(e -> {
            setSelectedButton(vehicleButton);
            new VehicleManagementFrame();
        });

        serviceQueueBtn.addActionListener(e -> {
            setSelectedButton(serviceQueueBtn);
            loadPanel(new ServiceQueuePanel(
                    serviceQueueController,
                    () -> loadPanel(createDashboard())
            ));
        });

        exitButton.addActionListener(e -> System.exit(0));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        applyRoleRestrictions(customerButton, vehicleButton);

        // ================= ADD =================

        add(header, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        //  LOADING DASHBOARD DEFAULT

        loadPanel(createDashboard());

        setVisible(true);
    }

    // ================= PANEL SWITCH =================

    private void loadPanel(JPanel panel) {
        contentPanel.removeAll();

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ================= SIDEBAR BUTTON =================

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != selectedButton) {
                    button.setBackground(hoverColor);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != selectedButton) {
                    button.setBackground(buttonColor);
                }
            }
        });

        return button;
    }

    // ================= SELECTED BUTTON =================

    private void setSelectedButton(JButton button) {

        for (Component comp : sidebar.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(buttonColor);
            }
        }

        button.setBackground(selectedColor);
        selectedButton = button;
    }

    // ================= DASHBOARD =================

    private JPanel createDashboard() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        grid.setBackground(new Color(240, 242, 245));


        grid.add(createCard("Today's Bookings", "12"));
        grid.add(createCard("In Progress", "5"));
        grid.add(createCard("Completed", "6"));
        grid.add(createCard("Waiting", "1"));

        panel.add(grid, BorderLayout.CENTER);

        return panel;
    }

    // ================= CARD =================

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // ================= ROLE =================

    private void applyRoleRestrictions(JButton customerButton, JButton vehicleButton) {

        if ("STAFF".equalsIgnoreCase(role)) {
            customerButton.setEnabled(false);
            vehicleButton.setEnabled(false);
        }
    }
}