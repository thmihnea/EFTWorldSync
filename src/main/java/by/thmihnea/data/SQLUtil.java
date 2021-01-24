package by.thmihnea.data;

import by.thmihnea.EFTLobby;
import by.thmihnea.object.EFTPlayer;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLUtil {

    /**
     * Returns whether or not an UUID exists in our database records
     *
     * @param uuid - UUID that has to be check
     * @return false/true depending if the said UUID exists or not in db records
     */
    public static boolean playerExists(UUID uuid) {
        PreparedStatement ps = null;
        try {
            ps = EFTLobby.getInstance().getCon().getConnection().prepareStatement("SELECT * FROM `player_data` WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            EFTLobby.getInstance().logSevere("An error occured while checking wether or not the player with UUID " + uuid + " exists in our database records. Please, report this stacktrace to thmihnea!");
            return false;
        }
    }

    /**
     * Adds a player to our database records. Uses {@link #setup(Player)}
     *
     * @param player - player to be added to database
     */
    private static void addPlayer(Player player) {
        try {
            PreparedStatement ps = EFTLobby.getInstance().getCon().getConnection().prepareStatement("SELECT * FROM `player_data` WHERE UUID = ?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (!(playerExists(player.getUniqueId())))
                setup(player);
            EFTLobby.getInstance().logInfo("Successfully managed to add player " + player.getName() + " (UUID: " + player.getUniqueId() + ") to our database records.");
        } catch (SQLException e) {
            e.printStackTrace();
            EFTLobby.getInstance().logSevere("An error occurred while attempting to add player with name " + player.getName() + " to our database records. Please, report this stacktrace to thmihnea!");
        }
    }

    /**
     * Sets up the tables for the specific player. Is used in {@link #addPlayer(Player)}
     *
     * @param player - player to have his tables set up
     */
    private static void setup(Player player) {
        try {
            PreparedStatement ps1 = getPreparedStatementPlayerData(player);
            PreparedStatement ps2 = getPreparedStatementStations(player);
            ps2.execute();
            ps1.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            EFTLobby.getInstance().logSevere("An error occurred while attempting to add player with name " + player.getName() + " to our database records. Please, report this stacktrace to thmihnea!");
        }
    }

    /**
     * Extracted method so that we can make
     * use of it in the {@link #setup(Player)}
     * method.
     *
     * @param player Player to setup.
     * @return {@link PreparedStatement}
     * @throws SQLException
     */
    private static PreparedStatement getPreparedStatementStations(Player player) throws SQLException {
        PreparedStatement ps2 = EFTLobby.getInstance().getCon().getConnection()
                .prepareStatement("INSERT INTO `data_stations` (UUID, EXTRA_CHESTS, INTELLIGENCE, WORKBENCH, FARM, SHOOTING_RANGE) VALUE (?, ?, ?, ?, ?, ?)");
        ps2.setString(1, player.getUniqueId().toString());
        ps2.setInt(2, 0);
        ps2.setInt(3, 0);
        ps2.setInt(4, 0);
        ps2.setInt(5, 0);
        ps2.setInt(6, 0);
        return ps2;
    }

    /**
     * Extracted method so that we can make
     * use of it in the {@link #setup(Player)}
     * method.
     *
     * @param player Player to setup.
     * @return {@link PreparedStatement}
     * @throws SQLException
     */
    private static PreparedStatement getPreparedStatementPlayerData(Player player) throws SQLException {
        PreparedStatement ps1 = EFTLobby.getInstance().getCon().getConnection()
                .prepareStatement("INSERT INTO `player_data` (UUID, NAME, COINS) VALUE (?, ?, ?)");
        ps1.setString(1, player.getUniqueId().toString());
        ps1.setString(2, player.getName());
        ps1.setInt(3, 0);
        return ps1;
    }

    /**
     * Method to be used in outer classes when we want to initialize a Player.
     * Uses {@link #setup(Player)} & {@link #addPlayer(Player)}
     *
     * @param player - player to be initialized
     */
    public static void init(Player player) {
        if (!(playerExists(player.getUniqueId()))) {
            EFTLobby.getInstance().logInfo("Player " + player.getName() + " (UUID: " + player.getUniqueId() + ") was not found in our database records.");
            EFTLobby.getInstance().logInfo("Attempting to initiate setup for this player.");
            addPlayer(player);
        }
    }

    /**
     * Method used to pull integer values from our database records using the field and the UUID we want to lookup
     *
     * @param UUID      UUID we're searching with
     * @param tableType TableType we're looking for
     * @return a value from database
     * Used to instantiate {@link by.thmihnea.object.EFTPlayer}
     */
    public static int getValue(TableType tableType, String field, String UUID) {
        String statement = "SELECT * FROM `" + tableType.getName() + "` WHERE UUID = ?";
        try {
            PreparedStatement ps = EFTLobby.getInstance().getCon().getConnection().prepareStatement(statement);
            ps.setString(1, UUID);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(field);
            else
                return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            EFTLobby.getInstance().logInfo("An error occurred while attempting to retrieve data from our database.");
            return -1;
        }
    }

    /**
     * Method used to set a certain value in the database
     * so that we can sync DB with {@link EFTPlayer#getCoins()}
     *
     * @param tableType TableType to set a value into
     * @param field     Field to alter/update
     * @param UUID      UUID of the player we're
     *                  looking after.
     * @param value     Value to set.
     */
    public static void setValue(TableType tableType, String field, String UUID, int value) {
        String statement = "UPDATE " + tableType.getName() + " SET " + field + " = ? WHERE UUID = ?";
        PreparedStatement ps = null;
        try {
            ps = EFTLobby.getInstance().getCon().getConnection().prepareStatement(statement);
            ps.setInt(1, value);
            ps.setString(2, UUID);
            ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
