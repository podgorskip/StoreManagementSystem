package managementSystem.gui.admins;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class that handles activities available for administrators, including adding/removing
 * admins, updating administrator's logins and passwords..
 */
public class AdministratorSettings extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JButton addAdminButton, removeAdminButton, updateAdminPassword, updateAdminLogin, backButton;

    /**
     * Constructs an AdministratorSettings object.
     * @param managementSystem an object that handles business logic
     */
    public AdministratorSettings(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    /**
     * Displays the AdministratorSettings form.
     */
    public void showAdministratorSettings() {
        setVisible(true);
    }

    /** Handles operations chosen by a user via provided buttons.
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addAdminButton) {
            SwingUtilities.invokeLater(() -> {
                AdminCreator adminCreator = new AdminCreator(managementSystem);
                adminCreator.showAdminCreator();
            });
        } else if (event.getSource() == removeAdminButton) {
            SwingUtilities.invokeLater(() -> {
                AdminRemover adminRemover = new AdminRemover(managementSystem);
                adminRemover.showAdminRemover();
            });

        } else if (event.getSource() == updateAdminLogin) {
            SwingUtilities.invokeLater(() -> {
                AdminLoginUpdater adminLoginUpdater = new AdminLoginUpdater(managementSystem);
                adminLoginUpdater.showAdminLoginUpdater();
            });

        } else if (event.getSource() == updateAdminPassword) {
            SwingUtilities.invokeLater(() -> {
                AdminPasswordUpdater adminPasswordUpdater = new AdminPasswordUpdater(managementSystem);
                adminPasswordUpdater.showAdminPasswordUpdater();
            });
        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    /**
     * Initializes the AdministratorSettings form.
     */
    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setTitle("Administrator settings");
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel manageLabel = new JLabel("Manage administrators");
        addAdminButton = new JButton("Add");
        addAdminButton.addActionListener(this);

        removeAdminButton = new JButton("Remove");
        removeAdminButton.addActionListener(this);

        updateAdminLogin = new JButton("Update login");
        updateAdminLogin.addActionListener(this);

        updateAdminPassword = new JButton("Update password");
        updateAdminPassword.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(manageLabel)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(addAdminButton)
                                .addComponent(removeAdminButton))
                        .addComponent(updateAdminLogin)
                        .addComponent(updateAdminPassword)
                        .addComponent(backButton)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(manageLabel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addAdminButton)
                                .addComponent(removeAdminButton))
                        .addGap(20)
                        .addComponent(updateAdminLogin)
                        .addComponent(updateAdminPassword)
                        .addComponent(backButton)
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }
}
