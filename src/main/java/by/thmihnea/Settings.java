package by.thmihnea;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    /**
     * World generation public static
     * data types.
     */
    public static int GAP = 0;
    public static double SPAWN_X = 17.0;
    public static double SPAWN_Y = 60.0;
    public static double SPAWN_Z = 47.0;
    public static float YAW = 45.3f;
    public static float PITCH = 2.3f;
    public static String SCHEMATIC_PATH = "plugins/EFTLobby/schematics/Hideout.schematic";
    public static int NPC_X = 13;
    public static int NPC_Y = 60;
    public static int NPC_Z = 51;

    /**
     * A list of messages which
     * are being used for the {@link by.thmihnea.inventory.UpgradesInventory}
     * and for {@link by.thmihnea.inventory.HideoutInventory}
     */
    public static String HIDEOUT_INVENTORY_TITLE = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("inventory.hideout-menu.title"));
    public static String HIDEOUT_INVENTORY_REGIONS = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("inventory.hideout-menu.upgrades.title"));
    public static String HIDEOUT_INVENTORY_JOIN_GAME = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("inventory.hideout-menu.arenas.title"));
    public static String HIDEOUT_INVENTORY_UPGRADES_TITLE = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("inventory.upgrades-menu.title"));
    public static String HIDEOUT_STATION_ALREADY_MAXED_OUT = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("stations.maxed-out"));
    public static String HIDEOUT_STATION_NOT_ENOUGH_COINS = ChatColor.translateAlternateColorCodes('&', EFTLobby.getCfg().getString("stations.not-enough-coins"));
    public static List<String> HIDEOUT_INVENTORY_REGIONS_LORE = colorList(EFTLobby.getCfg().getStringList("inventory.hideout-menu.upgrades.lore"));
    public static List<String> HIDEOUT_INVENTORY_JOIN_GAME_LORE = colorList(EFTLobby.getCfg().getStringList("inventory.hideout-menu.arenas.lore"));
    public static List<String> HIDEOUT_INVENTORY_STATION_LOCKED_LORE = colorList(EFTLobby.getCfg().getStringList("inventory.upgrades-menu.station.lore-locked"));
    public static List<String> HIDEOUT_INVENTORY_STATION_UNLOCKED_LORE = colorList(EFTLobby.getCfg().getStringList("inventory.upgrades-menu.station.lore-unlocked"));
    public static String SQL_HOST = EFTLobby.getCfg().getString("MySQL.host");
    public static Integer SQL_PORT = EFTLobby.getCfg().getInt("MySQL.port");
    public static String SQL_DATABASE = EFTLobby.getCfg().getString("MySQL.database");
    public static String SQL_USERNAME = EFTLobby.getCfg().getString("MySQL.username");
    public static String SQL_PASSWORD = EFTLobby.getCfg().getString("MySQL.password");

    /**
     * Color a certain {@link List<String>}
     * line by line and returning it back.
     * @param input List which we have
     *              to color.
     * @return {@link List<String>}
     */
    private static List<String> colorList(List<String> input) {
        List<String> ret = new ArrayList<String>();
        for (String line : input)
            ret.add(ChatColor.translateAlternateColorCodes('&', line));
        return ret;
    }

    /**
     * Replaces a certain string in
     * another string.
     * @param list {@link List<String>} which
     *                                 we have to manipulated.
     * @param tag Tag which we have to
     *            scan for.
     * @param newTag New tag which will be
     *               replaced to.
     * @return {@link List<String>}
     */
    public static List<String> replaceTag(List<String> list, String tag, String newTag) {
        List<String> res = new ArrayList<>();
        for (String line : list) {
            String newLine = line.replace(tag, newTag);
            res.add(newLine);
        }
        return res;
    }
}
