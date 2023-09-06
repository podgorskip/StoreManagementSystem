package managementSystem.gui;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuantityManager extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JTextField productNameField, productQuantityField;
    private JButton addButton, withdrawalButton, clearButton, backButton;

    public QuantityManager(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void getQuantityManager() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String name = productNameField.getText().trim().toLowerCase();
        String quantityString = productQuantityField.getText().trim();
        int quantity;

        if (event.getSource() == addButton) {
            if (isProductNameEmpty(name)) { return; }

            if (isQuantityNotParsable(quantityString) || isQuantityIncorrect(quantityString)) { return; }
            else { quantity = Integer.parseInt(quantityString); }

            if (managementSystem.isProductAvailable(name)) {
                managementSystem.updateQuantity(name, quantity, "supply");
                resetFields();
                JOptionPane.showMessageDialog(this, "Correctly supplied the product.");
            }
            else {
                managementSystem.insertProduct(name, quantity);
                resetFields();
                JOptionPane.showMessageDialog(this, "Correctly added the new product.");
            }

        } else if (event.getSource() == withdrawalButton) {
            if (isProductNameEmpty(name)) { return; }

            if (isQuantityNotParsable(quantityString)) { return; }
            else { quantity = Integer.parseInt(quantityString); }

            if (isProductAvailable(name)) {
                if (isEnoughResources(name, quantity)) {
                    managementSystem.updateQuantity(name, quantity, "withdrawal");
                    resetFields();
                    JOptionPane.showMessageDialog(this, "Correctly withdrawn the product.");
                }
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
        layout.setHonorsVisibility(false);

        setTitle("Product quantity manager");
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel productNameLabel = new JLabel("Product name");

        productNameField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity");

        productQuantityField = new JTextField();

        addButton = new JButton("Add");
        addButton.addActionListener(this);

        withdrawalButton = new JButton("Withdraw");
        withdrawalButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(productNameLabel)
                                .addComponent(quantityLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(productNameField)
                                .addComponent(productQuantityField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(addButton)
                                        .addComponent(withdrawalButton)
                                        .addComponent(clearButton)
                                        .addComponent(backButton)
                                )
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(productNameLabel)
                                .addComponent(productNameField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(quantityLabel)
                                .addComponent(productQuantityField)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton)
                                .addComponent(withdrawalButton)
                                .addComponent(clearButton)
                                .addComponent(backButton)
                        )

        );

        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 300));
    }

    private boolean isProductNameEmpty(String productName) {
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product name field cannot be empty.");
            return true;
        }
        return false;
    }

    private boolean isQuantityNotParsable(String quantity) {
        try {
            Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Provide a correct amount.");
            resetProductQuantityField();
            return true;
        }
        return false;
    }

    private boolean isQuantityIncorrect(String quantity) {
        if (Integer.parseInt(quantity) < 1) {
            JOptionPane.showMessageDialog(this, "Quantity cannot be less than 1.");
            resetProductQuantityField();
            return true;
        }
        return false;
    }

    private boolean isProductAvailable(String productName) {
        if (!managementSystem.isProductAvailable(productName)) {
            JOptionPane.showMessageDialog(this, "No such product to withdraw.");
            resetProductNameField();
            return false;
        }
        return true;
    }

    private boolean isEnoughResources(String productName, int quantity) {
        if (managementSystem.getQuantity(productName) < quantity) {
            JOptionPane.showMessageDialog(this, "Quantity exceeds available resources.");
            resetProductQuantityField();
            return false;
        }
        return true;
    }

    private void resetFields() {
        productNameField.setText("");
        productQuantityField.setText("");
    }

    private void resetProductNameField() {
        productNameField.setText("");
    }

    private void resetProductQuantityField() {
        productQuantityField.setText("");
    }
}
