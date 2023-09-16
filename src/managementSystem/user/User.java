package managementSystem.user;

/**
 * A class to represent a user entity.
 */
public class User {
    private final String login;
    private final String password;

    /**
     * Constructs a User object.
     * @param login user's login
     * @param password user's password
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Checks if the provided login and password matches the user's details.
     * @param login login to be checked
     * @param password password to be checked
     * @return true if matches, false otherwise
     */
    public boolean logIn(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    /**
     * Returns the user's login.
     * @return user's login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns the user's password.
     * @return user's password
     */
    public String getPassword() {
        return password;
    }
}
