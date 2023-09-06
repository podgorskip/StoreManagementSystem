package managementSystem.gui.admins;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginUpdater extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JTextField newLoginField;
    private JButton confirmationButton, backButton;

    public AdminLoginUpdater(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void showAdminLoginUpdater() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == confirmationButton) {
            String newLogin = newLoginField.getText().trim();

            if (newLogin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Login field cannot be empty.");
                return;
            }

            if (managementSystem.isAdminAvailable(newLogin)) {
                JOptionPane.showMessageDialog(this, "Provided login is already taken.");
                resetLoginField();
            } else {
                managementSystem.updateAdminLogin(managementSystem.getCurrentAdminLogin(), newLogin);
                JOptionPane.showMessageDialog(this, "Login updated correctly.");
                resetLoginField();
            }

        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        setLayout(layout);
        setTitle("Update login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel newLoginLabel = new JLabel("Provide a new login");

        newLoginField = new JTextField();

        confirmationButton = new JButton("Confirm");
        confirmationButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(newLoginLabel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(newLoginField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(confirmationButton)
                                        .addComponent(backButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(newLoginLabel)
                                .addComponent(newLoginField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(confirmationButton)
                                .addComponent(backButton))
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }

    private void resetLoginField() {
        newLoginField.setText("");
    }

    public static void main(String[] args) {
        new AdminLoginUpdater(new ManagementSystem()).showAdminLoginUpdater();
    }
}
