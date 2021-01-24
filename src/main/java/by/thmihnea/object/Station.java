package by.thmihnea.object;

import by.thmihnea.EFTLobby;
import org.bukkit.ChatColor;

public enum Station {

    /**
     * These enum values are going
     * to be used to generate the specific coordinates
     * of each Station per player.
     * See {@link Region} & {@link RegionManager} for more
     * information.
     */
    EXTRA_CHESTS(16, 62, 36, 18, 60, 34, ChatColor.translateAlternateColorCodes('&', "&eStorage Room")),
    INTELLIGENCE(25, 62, 32, 22, 60, 37, ChatColor.translateAlternateColorCodes('&', "&eIntelligence Station")),
    WORKBENCH(25, 63, 26, 22, 60, 31, ChatColor.translateAlternateColorCodes('&', "&eWorkbench")),
    FARM(16, 61, 31, 16, 60, 31, ChatColor.translateAlternateColorCodes('&', "&eFarm")),
    SHOOTING_RANGE(17, 62, 24, 24, 60, 24, ChatColor.translateAlternateColorCodes('&', "&eShooting Range"));

    /**
     * First X coordinate.
     */
    private int x1;

    /**
     * First Y coordinate.
     */
    private int y1;

    /**
     * First Z coordinate.
     */
    private int z1;

    /**
     * Second X coordinate.
     */
    private int x2;

    /**
     * Second Y coordinate.
     */
    private int y2;

    /**
     * Second Z coordinate.
     */
    private int z2;

    /**
     * The name of the station
     * used for the menus.
     */
    private String stationName;

    /**
     * The cost to upgrade
     * this station.
     */
    private int cost;

    /**
     * Constructor for each Enum value.
     *
     * @param x1 First X coordinate.
     * @param y1 First Y coordinate.
     * @param z1 First Z coordinate.
     * @param x2 Second X coordinate.
     * @param y2 Second Y coordinate.
     * @param z2 Second Z coordinate.
     * @param stationName The name of the station
     *             used for the menus it is
     *             displayed in.
     */
    Station(int x1, int y1, int z1, int x2, int y2, int z2, String stationName) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.stationName = stationName;
        String path = "stations.prices." + name();
        this.cost = EFTLobby.getCfg().getInt(path);
    }

    /**
     * Returns the first
     * X coordinate.
     * @return {@link Integer}
     */
    public int getX1() {
        return this.x1;
    }

    /**
     * Returns the second
     * X coordinate.
     * @return {@link Integer}
     */
    public int getX2() {
        return this.x2;
    }

    /**
     * Returns the first
     * Y coordinate.
     * @return {@link Integer}
     */
    public int getY1() {
        return this.y1;
    }

    /**
     * Returns the second
     * Y coordinate.
     * @return {@link Integer}
     */
    public int getY2() {
        return this.y2;
    }

    /**
     * Returns the first
     * Z coordinate.
     * @return {@link Integer}
     */
    public int getZ1() {
        return this.z1;
    }

    /**
     * Returns the second
     * Z coordinate.
     * @return {@link Integer}
     */
    public int getZ2() {
        return this.z2;
    }

    /**
     * Returns the name of
     * the station.
     * @return {@link String}
     */
    public String getName() {
        return this.stationName;
    }

    /**
     * Returns the cost to
     * upgrade this {@link Station}
     * @return {@link Integer}
     */
    public int getCost() {
        return this.cost;
    }
}
