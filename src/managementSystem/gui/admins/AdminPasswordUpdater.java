package managementSystem.gui.admins;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class that allows for updating the logged administrator's password.
 */
public class AdminPasswordUpdater extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JPasswordField newPasswordField, newPasswordRepeatField;
    private JButton confirmationButton, backButton;

    /**
     * Constructs an AdminPasswordUpdater object.
     * @param managementSystem
     */
    public AdminPasswordUpdater(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    /**
     * Displays the AdminPasswordUpdater form.
     */
    public void showAdminPasswordUpdater() {
        setVisible(true);
    }

    /**
     * Handles operations chosen by a user via provided buttons.
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String password1 = new String(newPasswordField.getPassword());
        String password2 = new String(newPasswordRepeatField.getPassword());

        if (event.getSource() == confirmationButton) {
            if (password1.isEmpty() || password2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password fields cannot be empty.");
                resetPasswordFields();
                return;
            }

            if (arePasswordsNotMatching(password1, password2)) {
                JOptionPane.showMessageDialog(this, "Passwords aren't matching.");
                resetPasswordFields();
            } else {
                managementSystem.updateAdminPassword(managementSystem.getCurrentAdminLogin(), password1);
                JOptionPane.showMessageDialog(this, "Password updated correctly.");
                resetPasswordFields();
            }

        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    /**
     * Initialized the AdminPasswordUpdater form.
     */
    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(layout);
        setTitle("Update password");

        JLabel newPasswordLabel = new JLabel("Provide a new password");

        newPasswordField = new JPasswordField();

        JLabel newPasswordRepeatLabel = new JLabel("Repeat the new password");

        newPasswordRepeatField = new JPasswordField();

        confirmationButton = new JButton("Confirm");
        confirmationButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(newPasswordLabel)
                                .addComponent(newPasswordRepeatLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(newPasswordField)
                                .addComponent(newPasswordRepeatField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(confirmationButton)
                                        .addComponent(backButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(newPasswordLabel)
                                .addComponent(newPasswordField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(newPasswordRepeatLabel)
                                .addComponent(newPasswordRepeatField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(confirmationButton)
                                        .addComponent(backButton))

        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }

    /**
     * Checks whether provided passwords are not equal.
     * @param password1
     * @param password2
     * @return true if the passwords are not equal, false otherwise
     */
    private boolean arePasswordsNotMatching(String password1, String password2) {
        return !password1.equals(password2);
    }

    /**
     * Resets the password fields.
     */
    private void resetPasswordFields() {
        newPasswordField.setText("");
        newPasswordRepeatField.setText("");
    }

    public static void main(String[] args) {
        new AdminPasswordUpdater(new ManagementSystem()).showAdminPasswordUpdater();
    }
}
