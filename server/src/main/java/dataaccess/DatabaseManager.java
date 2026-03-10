package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    /**
    * creates the tables of a database if it does not already exist
    */
    static public void createTables() throws DataAccessException {
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
         var preparedStatementUser = conn.prepareStatement(CREATE_USER_TABLE);
         var preparedStatementAuth = conn.prepareStatement(CREATE_AUTH_TOKEN_TABLE);
         var preparedStatementGame = conn.prepareStatement(CREATE_GAME_TABLE)){
            preparedStatementUser.executeUpdate();
            preparedStatementAuth.executeUpdate();
            preparedStatementGame.executeUpdate();
        }
        catch(SQLException ex){
            throw new DataAccessException("failed to create tables", ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }


    private static final String CREATE_USER_TABLE = """
        CREATE TABLE IF NOT EXISTS user (
        id INT NOT NULL AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        PRIMARY KEY (id),
        UNIQUE KEY user_username_uindex (username)
        );
        """;

    private static final String CREATE_AUTH_TOKEN_TABLE= """
        CREATE TABLE IF NOT EXISTS auth (
        token VARCHAR(255),
        username VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY(username) REFERENCES user(username) ON DELETE CASCADE
        )
        """;

    private static final String CREATE_GAME_TABLE = """
            CREATE TABLE IF NOT EXISTS game (
            id INT AUTO_INCREMENT PRIMARY KEY,
            game_name VARCHAR(255) NOT NULL,
            white_username VARCHAR(255),
            black_username VARCHAR(255),
            game_state TEXT NOT NULL,
            FOREIGN KEY (white_username) REFERENCES user(username),
            FOREIGN KEY (black_username) REFERENCES user(username)
            """;

}


