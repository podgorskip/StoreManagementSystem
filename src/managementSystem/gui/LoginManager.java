package managementSystem.gui;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginManager extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    JTextField loginField;
    JPasswordField passwordField;
    JButton loginButton, clearButton;
    private int loginAttempts = 3;

    public LoginManager(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void showLoginManager() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (event.getSource() == loginButton) {
            if (!managementSystem.isAdminAvailable(login)) {
                JOptionPane.showMessageDialog(this, "No such user available.");
            } else {
                if (managementSystem.validatePassword(login, password)) {
                    SwingUtilities.invokeLater(() -> {
                        managementSystem.setCurrentAdminLogin(login);
                        ActivityManager activityManager = new ActivityManager(managementSystem);
                        activityManager.showActivityManager();
                    });
                    dispose();
                } else {
                    String message;
                    if (loginAttempts == 1) {
                        message = "No attempts left. System is about to exit.";
                        JOptionPane.showMessageDialog(this, message);
                        System.exit(0);
                    } else {
                        message = "Incorrect password for provided login. You've got " + (loginAttempts - 1) + " attempt(s) left.";
                        JOptionPane.showMessageDialog(this, message);
                    }
                    loginAttempts--;
                }
            }

        } else if (event.getSource() == clearButton) {
            loginField.setText("");
            passwordField.setText("");
        }
    }

    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHonorsVisibility(false);

        setTitle("Login form");
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel loginLabel = new JLabel("Login");

        loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Password");

        passwordField = new JPasswordField();

        loginButton = new JButton("Log in");
        loginButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginLabel)
                                .addComponent(passwordLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginField)
                                .addComponent(passwordField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(loginButton)
                                        .addComponent(clearButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginLabel)
                                .addComponent(loginField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginButton)
                                .addComponent(clearButton))
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }
}
