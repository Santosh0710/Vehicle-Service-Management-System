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

        setTitle("Vehicle Service Management System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10,10));

        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= HEADER =================
    private JPanel createHeader(){

        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("User Login", JLabel.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0,70,140));

        panel.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));

        panel.add(title, BorderLayout.CENTER);

        return panel;
    }

    // ================= FORM =================
    private JPanel createForm(){

        JPanel panel = new JPanel(new GridLayout(2,2,15,15));

        panel.setBorder(BorderFactory.createEmptyBorder(20,40,20,40));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        return panel;
    }

    // ================= BUTTON =================
    private JPanel createButtonPanel(){

        JPanel panel = new JPanel();

        loginButton = new RoundedButton("Login");

        panel.add(loginButton);

        loginButton.addActionListener(e -> login());

        return panel;
    }

    // ================= LOGIN =================
    private void login(){

        try{

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = controller.login(username,password);

            JOptionPane.showMessageDialog(this,
                    "Welcome " + user.getUsername());

            dispose();

            new MainAppLauncher(user);

        }
        catch(Exception ex){

            JOptionPane.showMessageDialog(this,
                    ex.getMessage());
        }
    }
}