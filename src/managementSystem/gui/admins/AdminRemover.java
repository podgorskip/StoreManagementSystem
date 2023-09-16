package managementSystem.gui.admins;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class that allows for removing administrators.
 */
public class AdminRemover extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton confirmationButton, clearButton, backButton;
    private int loginAttempts = 3;

    /**
     * Constructs an AdminRemover object.
     * @param managementSystem an object that handles business logic
     */
    public AdminRemover(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    /**
     * Displays the AdminRemover form.
     */
    public void showAdminRemover() {
        setVisible(true);
    }

    /**
     * Handles operations chosen by a user via provided buttons.
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (event.getSource() == confirmationButton) {
            JOptionPane.showConfirmDialog(this, "Are you sure to remove this admin?");
            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Login cannot be empty");
                resetPasswordField();
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty");
                return;
            }

            if (!managementSystem.isAdminAvailable(login)) {
                JOptionPane.showMessageDialog(this, "No such administrator available");
                resetFields();
                return;
            }

            if (managementSystem.validatePassword(login, password)) {
                managementSystem.removeAdmin(login);
                JOptionPane.showMessageDialog(this, "Admin removed");
                dispose();

            } else {
                if (loginAttempts == 1) {
                    JOptionPane.showMessageDialog(this, "No attempts left. System is about to exit.");
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(this,  "Incorrect password for provided login. You've got " + (loginAttempts - 1) + " attempt(s) left.");
                }
                loginAttempts--;
            }

        } else if (event.getSource() == clearButton) {
            resetFields();

        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    /**
     * Initializes the AdminRemover form.
     */
    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setLayout(layout);
        setTitle("Remove administrator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel loginLabel = new JLabel("Provide login");

        loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Provide password");

        passwordField = new JPasswordField();

        confirmationButton = new JButton("Confirm");
        confirmationButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginLabel)
                                .addComponent(passwordLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginField)
                                .addComponent(passwordField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(confirmationButton)
                                        .addComponent(clearButton)
                                        .addComponent(backButton)))
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
                                .addComponent(confirmationButton)
                                .addComponent(clearButton)
                                .addComponent(backButton))
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }

    /**
     * Resets all the fields.
     */
    private void resetFields() {
        loginField.setText("");
        passwordField.setText("");
    }

    /**
     * Resets the password field.
     */
    private void resetPasswordField() {
        passwordField.setText("");
    }

    public static void main(String[] args) {
        new AdminRemover(new ManagementSystem()).showAdminRemover();
    }

}
