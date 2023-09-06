package managementSystem.gui;

import managementSystem.core.ManagementSystem;
import managementSystem.gui.admins.AdministratorSettings;
import managementSystem.gui.quantity.QuantityInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActivityManager extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JButton addWithdrawButton, checkQuantityButton, backButton, administratorSettingsButton;

    public ActivityManager(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void showActivityManager() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == addWithdrawButton) {
            SwingUtilities.invokeLater(() -> {
                QuantityManager quantityManager = new QuantityManager(managementSystem);
                quantityManager.getQuantityManager();
            });

        } else if (event.getSource() == checkQuantityButton) {
            SwingUtilities.invokeLater(() -> {
                QuantityInfo quantityInfo = new QuantityInfo(managementSystem);
                quantityInfo.showQuantityInfo();
            });

        } else if (event.getSource() == backButton) {
            dispose();

        } else if (event.getSource() == administratorSettingsButton) {
            SwingUtilities.invokeLater(() -> {
                AdministratorSettings administratorSettings = new AdministratorSettings(managementSystem);
                administratorSettings.showAdministratorSettings();
            });
        }
    }


    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHonorsVisibility(false);

        setLayout(layout);
        setTitle("Activities");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel addWithdrawLabel = new JLabel("Add/withdraw products");

        addWithdrawButton = new JButton("Add/withdraw");
        addWithdrawButton.addActionListener(this);

        JLabel checkQuantityLabel = new JLabel("Check product quantity");

        checkQuantityButton = new JButton("Check quantity");
        checkQuantityButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        administratorSettingsButton = new JButton("Settings");
        administratorSettingsButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addWithdrawLabel)
                                .addComponent(checkQuantityLabel)
                                .addComponent(administratorSettingsButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(addWithdrawButton)
                                .addComponent(checkQuantityButton)
                                .addComponent(backButton))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addWithdrawLabel)
                                .addComponent(addWithdrawButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(checkQuantityLabel)
                                .addComponent(checkQuantityButton)
                                .addComponent(backButton))
                        .addGap(30)
                        .addComponent(administratorSettingsButton)
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new ActivityManager(new ManagementSystem()).showActivityManager();
    }
}
