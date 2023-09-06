package managementSystem.gui.admins;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminCreator extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JTextField loginField;
    private JPasswordField passwordField, passwordRepeatField;
    private JButton confirmationButton, clearButton, backButton;

    public AdminCreator(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void showAdminCreator() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String login = loginField.getText().trim();
        String password1 = new String(passwordField.getPassword());
        String password2 = new String(passwordRepeatField.getPassword());

        if (event.getSource() == confirmationButton) {
            if (managementSystem.isAdminAvailable(login)) {
                JOptionPane.showMessageDialog(this, "Provided login is already taken.");
                resetFields();
                return;
            }

            if (login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Login field cannot be empty.");
                resetFields();
                return;
            }

            if (password1.isEmpty() || password2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password fields cannot be empty.");
                resetPasswordFields();
                return;
            }

            if (arePasswordsNotMatching(password1, password2)) {
                JOptionPane.showMessageDialog(this, "Passwords aren't matching.");
                resetPasswordFields();
            } else {
                managementSystem.addAdmin(login, password1);
                resetFields();
                JOptionPane.showMessageDialog(this, "Admin added.");
            }

        } else if (event.getSource() == clearButton) {
            resetFields();

        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(layout);
        setTitle("Create administrator");

        JLabel loginLabel = new JLabel("Provide login");

        loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Provide password");

        passwordField = new JPasswordField();

        JLabel passwordRepeatLabel = new JLabel("Repeat password");

        passwordRepeatField = new JPasswordField();

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
                                .addComponent(passwordLabel)
                                .addComponent(passwordRepeatLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loginField)
                                .addComponent(passwordField)
                                .addComponent(passwordRepeatField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(confirmationButton)
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
                                .addComponent(passwordRepeatLabel)
                                .addComponent(passwordRepeatField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(confirmationButton)
                                .addComponent(backButton))
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }

    private boolean arePasswordsNotMatching(String password1, String password2) {
        return !password1.equals(password2);
    }

    private void resetPasswordFields() {
        passwordField.setText("");
        passwordRepeatField.setText("");
    }

    private void resetFields() {
        loginField.setText("");
        passwordField.setText("");
        passwordRepeatField.setText("");
    }

    public static void main(String[] args) {
        new AdminCreator(new ManagementSystem()).showAdminCreator();
    }
}
