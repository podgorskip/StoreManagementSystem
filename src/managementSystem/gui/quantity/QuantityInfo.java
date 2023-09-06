package managementSystem.gui.quantity;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuantityInfo extends JFrame implements ActionListener {
    private final ManagementSystem managementSystem;
    private JButton confirmButton, showAllButton, clearButton, backButton;
    private JTextField quantityInfoField, productNameField;

    public QuantityInfo(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    public void showQuantityInfo() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String productName = productNameField.getText().trim().toLowerCase();

        if (event.getSource() == confirmButton) {
            if (managementSystem.isProductAvailable(productName)) {
                quantityInfoField.setText(String.valueOf(managementSystem.getQuantity(productName)));
            } else {
                JOptionPane.showMessageDialog(this, "No such product available.");
            }

        } else if (event.getSource() == showAllButton)  {
            SwingUtilities.invokeLater(() -> {
                QuantityInfoAll quantityInfoAll = new QuantityInfoAll(managementSystem);
                quantityInfoAll.showAllQuantityInfo();
            });

        } else if (event.getSource() == clearButton) {
            productNameField.setText("");
            quantityInfoField.setText("");

        } else if (event.getSource() == backButton) {
            dispose();
        }
    }

    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHonorsVisibility(false);

        setLayout(layout);
        setTitle("Quantity information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel productNameLabel = new JLabel("Product name");

        productNameField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity");

        quantityInfoField = new JTextField();
        quantityInfoField.setEditable(false);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(this);

        showAllButton = new JButton("Show all");
        showAllButton.addActionListener(this);

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
                                .addComponent(quantityInfoField)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(confirmButton)
                                        .addComponent(showAllButton)
                                        .addComponent(clearButton)
                                        .addComponent(backButton)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(productNameLabel)
                                .addComponent(productNameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(quantityLabel)
                                .addComponent(quantityInfoField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(confirmButton)
                                .addComponent(showAllButton)
                                .addComponent(clearButton)
                                .addComponent(backButton))
        );

        pack();
        setMinimumSize(new Dimension(500, 300));
        setLocationRelativeTo(null);
    }
}
