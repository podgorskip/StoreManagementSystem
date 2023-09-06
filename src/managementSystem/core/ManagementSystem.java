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

public class ManagementSystem {
    private final Connection connection;
    private final Map<String, Integer> goods;
    private final Map<String, User> admins;
    private String currentAdminLogin;

    public ManagementSystem() {
        connection = DatabaseConnection.getConnection();
        goods = new HashMap<>();
        admins = new HashMap<>();

        restoreFromDatabase();
    }

    public void startLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginManager loginManager = new LoginManager(this);
            loginManager.showLoginManager();
        });
    }

    // Administrators management

    public String getCurrentAdminLogin() {
        return currentAdminLogin;
    }

    public void setCurrentAdminLogin(String login) {
        currentAdminLogin = login;
    }

    public boolean isAdminAvailable(String login) {
        return admins.get(login) != null;
    }

    public boolean validatePassword(String login, String password) {
        User user = admins.get(login);
        return user != null && user.logIn(login, password);
    }

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

    public int getQuantity(String name) {
        return goods.get(name);
    }

    public boolean isProductAvailable(String productName) {
        return goods.get(productName) != null;
    }

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

    public String[] productsInfo() {
        StringBuilder productNames = new StringBuilder();
        StringBuilder productQuantities = new StringBuilder();

        for (Map.Entry<String, Integer> entry : goods.entrySet()) {
            productNames.append(entry.getKey()).append('\n');
            productQuantities.append(entry.getValue()).append('\n');
        }

        return new String[] { productNames.toString(), productQuantities.toString() };
    }

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
