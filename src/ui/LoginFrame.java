package ui;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private UserController controller = new UserController();

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(e -> login());

        setVisible(true);
    }

    private void login() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = controller.login(username, password);

            JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername());

            // Open main panel
            dispose();
            new MainAppLauncher(user); // my existing panel

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}