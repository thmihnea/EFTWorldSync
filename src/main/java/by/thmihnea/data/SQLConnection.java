package by.thmihnea.data;

import by.thmihnea.EFTLobby;
import by.thmihnea.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SQLConnection {

    /**
     * Connection object to the database
     */
    public static Connection connection;

    /**
     * The host, database, username and password for the database
     */
    public static String host, database, username, password;

    /**
     * The port for the database
     */
    public static Integer port;

    /**
     * Default constructor for the
     * {@link SQLConnection} class.
     * Used in {@link EFTLobby}
     */
    public SQLConnection() {
        this.connect();
        this.setupDefaults();
    }

    /**
     * Returns the connection to the database
     *
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the connection to the MySQL server
     *
     * @param connection - connection to be set to
     */
    public void setConnection(Connection connection) {
        SQLConnection.connection = connection;
    }

    /**
     * Creates default plugin tables.
     * Default tables are {@link TableType#PLAYER_DATA}
     * and {@link TableType#DATA_STATIONS}.
     */
    public void setupDefaults() {
        EFTLobby.getInstance().logInfo("Attempting to create default tables in the database.");
        try {
            PreparedStatement ps1 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `player_data` (UUID varchar(256), NAME varchar(256), COINS int(255))");
            PreparedStatement ps2 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `data_stations` (UUID varchar(256), EXTRA_CHESTS tinyint(1), INTELLIGENCE tinyint(1), WORKBENCH tinyint(1), FARM tinyint(1), SHOOTING_RANGE tinyint(1))");
            List<PreparedStatement> statements = Arrays.asList(ps1, ps2);
            statements.forEach(statement -> {
                try {
                    statement.executeUpdate();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
            EFTLobby.getInstance().logInfo("An error has occured while setting up SQL drivers. Tables couldn't get created. (Check if your database has been set up correctly!)");
        }
    }

    /**
     * Connects to the SQL database.
     * Pulls up the Database details
     * from the config file of the plugin.
     */
    public void connect() {
        EFTLobby.getInstance().logInfo("Attempting to establish a connection to the database.");
        host = Settings.SQL_HOST;
        port = Settings.SQL_PORT;
        database = Settings.SQL_DATABASE;
        username = Settings.SQL_USERNAME;
        password = Settings.SQL_PASSWORD;
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) return;
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
            }
            List<String> conMsg = Arrays.asList("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-",
                    "Successfully connected to the database!",
                    "Connection details:",
                    "Host: " + host,
                    "Port: " + port,
                    "Database: " + database,
                    "Username: " + username,
                    "Password: " + password,
                    "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            conMsg.forEach(msg -> EFTLobby.getInstance().logInfo(msg));
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            EFTLobby.getInstance().logSevere("An error has occurred while trying to connect to the database (is it set up correctly?). Please, report the stacktrace above to thmihnea!");
        }
    }

}
