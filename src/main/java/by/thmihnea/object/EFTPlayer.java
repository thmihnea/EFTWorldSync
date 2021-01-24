package by.thmihnea.object;

import by.thmihnea.EFTLobby;
import by.thmihnea.data.SQLUtil;
import by.thmihnea.data.TableType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EFTPlayer {

    /**
     * {@link Player} for which we're
     * constructing the {@link EFTPlayer}
     * object.
     */
    private Player player;

    /**
     * {@link Integer} the cost to
     * upgrade the station.
     */
    private int coins;

    /**
     * Constructor for the {@link EFTPlayer}
     * class. We use this to construct objects
     * for each player.
     *
     * @param player The player for which
     *               we're creating the class
     *               object.
     */
    public EFTPlayer(Player player) {
        this.player = player;
        this.coins = SQLUtil.getValue(TableType.PLAYER_DATA, "COINS", this.player.getUniqueId().toString());
        EFTPlayerManager.addPlayer(this);
        EFTLobby.getInstance().logInfo("Successfully instantiated player " + player.getName() + " in cached memory.");
    }

    /**
     * Returns the {@link} Player for
     * which we've constructed the
     * object.
     *
     * @return {@link Player}
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the UUID of the player.
     *
     * @return {@link UUID}
     */
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    /**
     * Sets the amount of coins
     * the Player has. Also updates them
     * to the database.
     *
     * @param value The value which we set
     *              the coins to.
     */
    public void setCoins(int value) {
        this.coins = value;
        SQLUtil.setValue(TableType.PLAYER_DATA, "COINS", this.player.getUniqueId().toString(), value);
    }

    /**
     * Returns the amount of coins a
     * player currently has.
     *
     * @return {@link Integer}
     */
    public int getCoins() {
        return this.coins;
    }
}
