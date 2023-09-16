package managementSystem.core;

import managementSystem.gui.LoginManager;
import managementSystem.user.User;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that handle business logic, communicating with a database and UI.
 */
public class ManagementSystem {
    private final Connection connection;
    private final Map<String, Integer> goods;
    private final Map<String, User> admins;
    private String currentAdminLogin;

    /**
     * Constructs a ManagementSystem object.
     */
    public ManagementSystem() {
        connection = DatabaseConnection.getConnection();
        goods = new HashMap<>();
        admins = new HashMap<>();

        restoreFromDatabase();
    }

    /**
     * Starts the whole application.
     */
    public void startLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginManager loginManager = new LoginManager(this);
            loginManager.showLoginManager();
        });
    }

    // Administrators management

    /**
     * Returns a login of the currently logged admin.
     * @return a login of the currently logged admin
     */
    public String getCurrentAdminLogin() {
        return currentAdminLogin;
    }

    /**
     * Sets a login of the currently logged admin.
     * @param login administrator's login
     */
    public void setCurrentAdminLogin(String login) {
        currentAdminLogin = login;
    }

    /**
     * Checks whether the login is available.
     * @param login login to be checked
     * @return true if an admin already has this login, false otherwise
     */
    public boolean isAdminAvailable(String login) {
        return admins.get(login) != null;
    }

    /**
     * Validates password with the linked admin account.
     * @param login login of the admin whose password is checked
     * @param password password to be checked
     * @return true if logged successfully, false otherwise
     */
    public boolean validatePassword(String login, String password) {
        User user = admins.get(login);
        return user != null && user.logIn(login, password);
    }

    /**
     * Adds an admin to the database and the local admins variable.
     * @param adminLogin login of a new admin
     * @param adminPassword password of a new admin
     */
    public void addAdmin(String adminLogin, String adminPassword) {
        String sql = "INSERT INTO admins (login, password) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, adminLogin);
            preparedStatement.setString(2, adminPassword);
            preparedStatement.executeUpdate();

            admins.put(adminLogin, new User(adminLogin, adminPassword));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an admin from database and the local admins variable.
     * @param adminLogin login of the admin to be removed
     */
    public void removeAdmin(String adminLogin) {
        String sql = "DELETE FROM admins WHERE login=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, adminLogin);
            preparedStatement.executeUpdate();

            admins.remove(adminLogin);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a password of an admin with the provided login.
     * @param login login of an admin to have a password updated
     * @param password a new password
     */
    public void updateAdminPassword(String login, String password) {
        String sql = "UPDATE admins SET password=? WHERE login=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, password);
            preparedStatement.setString(2, login);
            preparedStatement.executeUpdate();

            admins.put(login, new User(login, password));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a login of an admin with the provided login.
     * @param oldLogin an administrator's old login
     * @param newLogin an administrator's new login
     */
    public void updateAdminLogin(String oldLogin, String newLogin) {
        String sql = "UPDATE admins SET login=? WHERE login=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newLogin);
            preparedStatement.setString(2, oldLogin);
            preparedStatement.executeUpdate();

            String password  = admins.get(oldLogin).getPassword();
            admins.remove(oldLogin);
            admins.put(newLogin, new User(newLogin, password));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Products management

    /**
     * Returns a quantity of the provided product name
     * @param name product name to have a quantity checked
     * @return quantity of the product
     */
    public int getQuantity(String name) {
        return goods.get(name);
    }

    /**
     * Checks whether a product already exists in the store.
     * @param productName a product name of which availability is checked
     * @return true if already exists, false otherwise
     */
    public boolean isProductAvailable(String productName) {
        return goods.get(productName) != null;
    }

    /**
     * Updates quantity of a product in a database.
     * @param productName a product name to have the quantity updated
     * @param quantity an amount which should be supplied/withdrawn
     * @param type supply if the product should be restocked, withdrawal
     *             if one should be withdrawn
     */
    public void updateQuantity(String productName, int quantity, String type) {
        int initialQuantity = goods.get(productName);
        String sql = "UPDATE goods SET quantity=? WHERE name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            if (type.equals("supply")) {
                preparedStatement.setInt(1, quantity + initialQuantity);
                fluctuate(productName, quantity, type, false);
            } else if (type.equals("withdrawal")) {
                preparedStatement.setInt(1, initialQuantity - quantity);
                fluctuate(productName, (-1) * quantity, type, false);
            }

            preparedStatement.setString(2, productName);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new product to the database.
     * @param productName a new product name
     * @param quantity a quantity how much should be stocked
     */
    public void insertProduct(String productName, int quantity) {
        String sql = "INSERT INTO goods (name, quantity) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, quantity);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            fluctuate(productName, quantity, "supply", true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notes all the changes made with the product quantity in a database.
     * @param productName a product name of which the quantity was change
     * @param quantity a quantity of supply/withdrawal
     * @param type manipulation type - supply/withdrawal
     * @param isNew flag is a product is newly-added
     */
    private void fluctuate(String productName, int quantity, String type, boolean isNew) {
        String sql = "INSERT INTO fluctuation (goodID, activity, amount) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, productID(productName));
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();

            if (isNew) {
                goods.put(productName, quantity);
            } else {
                int initialQuantity = goods.get(productName);
                goods.put(productName, initialQuantity + quantity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array containing all products names and quantities.
     * Index 0 contains all the product names.
     * Index 1 contains all the quantities.
     * @return an array with information about products
     */
    public String[] productsInfo() {
        StringBuilder productNames = new StringBuilder();
        StringBuilder productQuantities = new StringBuilder();

        for (Map.Entry<String, Integer> entry : goods.entrySet()) {
            productNames.append(entry.getKey()).append('\n');
            productQuantities.append(entry.getValue()).append('\n');
        }

        return new String[] { productNames.toString(), productQuantities.toString() };
    }

    /**
     * Returns an ID of a product with the specified name.
     * @param productName a name of a product to have the ID checked
     * @return product's ID
     */
    private int productID(String productName) {
        int ID = -1;
        String sql = "SELECT * FROM goods WHERE name='" + productName + "'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            ID = Integer.parseInt(resultSet.getString("ID"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ID;
    }

    /**
     * Restores data from the database, initializing admins and goods class fields.
     */
    private void restoreFromDatabase() {
        try {

            String sql = "SELECT * FROM goods";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();

            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                goods.put(resultSet.getString("name"), resultSet.getInt("quantity"));
            }

            sql = "SELECT * FROM admins";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                admins.put(resultSet.getString("login"), new User(resultSet.getString("login"), resultSet.getString("password")));
            }

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
