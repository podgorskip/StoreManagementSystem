package managementSystem.gui.quantity;

import managementSystem.core.ManagementSystem;

import javax.swing.*;
import java.awt.*;

/**
 * A class that displays information about all the products in a database.
 */
public class QuantityInfoAll extends JFrame {
    private final ManagementSystem managementSystem;
    JTextArea productNameArea, productQuantityArea;
    JButton backButton;

    /**
     * Constructs a QuantityInfoAll object.
     * @param managementSystem
     */
    public QuantityInfoAll(ManagementSystem managementSystem) {
        this.managementSystem = managementSystem;
        initForm();
    }

    /**
     * Displays the products' information.
     */
    public void showAllQuantityInfo() {
        setVisible(true);
    }

    /**
     * Initializes the window to display product information.
     */
    private void initForm() {
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        setLayout(layout);
        setTitle("Product quantities");
        setMinimumSize(new Dimension(500, 300));

        JLabel productNameLabel = new JLabel("Product");
        productNameLabel.setFont(new Font(null, Font.BOLD, 15));

        productNameArea = new JTextArea();
        productNameArea.setEditable(false);

        JLabel productQuantityLabel = new JLabel("Quantity");
        productQuantityLabel.setFont(new Font(null, Font.BOLD, 15));

        productQuantityArea = new JTextArea();
        productQuantityArea.setEditable(false);

        backButton = new JButton("Back");
        backButton.addActionListener(event -> {
            dispose();
        });

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(productNameLabel)
                                .addComponent(productNameArea))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(productQuantityLabel)
                                .addComponent(productQuantityArea)
                                .addComponent(backButton))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(productNameLabel)
                                .addComponent(productQuantityLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(productNameArea)
                                .addComponent(productQuantityArea))
                        .addComponent(backButton)
        );

        String productNames = managementSystem.productsInfo()[0];
        String productQuantities = managementSystem.productsInfo()[1];

        productNameArea.setText(productNames);
        productQuantityArea.setText(productQuantities);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
}
