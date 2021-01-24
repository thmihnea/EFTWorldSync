package by.thmihnea.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Region {

    /**
     * Region class.
     * Every Region object is being stored in
     * the {@link RegionManager} class.
     * Edit with precaution.
     */

    /**
     * The player for which
     * we actually build the region itself.
     * {@link #Region(Player, Location, Location, String)}
     */
    private Player player;

    /**
     * First location (point).
     * {@link Location}
     */
    private Location location1;

    /**
     * Second location (point).
     * {@link Location}
     */
    private Location location2;

    /**
     * Region world.
     * {@link World}
     */
    private World world;

    /**
     * Region name.
     */
    private String stationName;

    /**
     * Coordinates for the minimum
     * X value of the region.
     */
    private int xMin;

    /**
     * Coordinates for the maximum
     * X value of the region.
     */
    private int xMax;

    /**
     * Coordinates for the minimum
     * Y value of the region.
     */
    private int yMin;

    /**
     * Coordinates for the maximum
     * Y value of the region.
     */
    private int yMax;

    /**
     * Coordinates for the minimum
     * Z value of the region.
     */
    private int zMin;

    /**
     * Coordinates for the maximum
     * Z value of the region.
     */
    private int zMax;

    /**
     * Constructor for the Region object.
     *
     * @param location1   First location point. See {@link Region#location1}
     * @param location2   Second location point. See {@link Region#location2}
     * @param stationName String name of the station. See {@link Region#stationName}
     */
    public Region(Player player, Location location1, Location location2, String stationName) {
        this.location1 = location1;
        this.location2 = location2;
        this.stationName = stationName;
        this.world = location1.getWorld();
        this.xMin = Math.min(location1.getBlockX(), location2.getBlockX());
        this.xMax = Math.max(location1.getBlockX(), location2.getBlockX());
        this.yMin = Math.min(location1.getBlockY(), location2.getBlockY());
        this.yMax = Math.max(location1.getBlockY(), location2.getBlockY());
        this.zMin = Math.min(location1.getBlockZ(), location2.getBlockZ());
        this.zMax = Math.max(location1.getBlockZ(), location2.getBlockZ());
        this.player = player;
        RegionManager.addRegionToPlayer(player, this);
    }

    /**
     * Gets a list of blocks that are
     * inside of the given Region.
     *
     * @return all blocks inside region.
     */
    public List<Block> getBlocks() {
        final ArrayList<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for (int x = this.xMin; x <= this.xMax; ++x) {
            for (int y = this.yMin; y <= this.yMax; ++y) {
                for (int z = this.zMin; z <= this.zMax; ++z) {
                    final Block b = Bukkit.getWorld(player.getUniqueId().toString()).getBlockAt(x, y, z);
                    bL.add(b);
                }
            }
        }
        return bL;
    }

    /**
     * Calculates the total blocksize in
     * between the region bounds so that we can
     * attribute the ArrayList an initial value
     * in the {@link #getBlocks()}} method.
     *
     * @return total block size.
     */
    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    /**
     * Returns the maximum width on the
     * X coordinate.
     *
     * @return X width
     */
    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }

    /**
     * Returns the maximum width on the
     * Z coordinate.
     *
     * @return Z width
     */
    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }

    /**
     * Returns the height of the
     * region, also known as the distance
     * between ymax and ymin (dy)
     *
     * @return dy
     */
    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }

    /**
     * Return the first location
     * parameter.
     *
     * @return {@link #location1}
     */
    public Location getLocation1() {
        return this.location1;
    }

    /**
     * Return the second location
     * parameter.
     *
     * @return {@link #location2}
     */
    public Location getLocation2() {
        return this.location2;
    }

    /**
     * Return the station name
     * of the given Region object.
     *
     * @return {@link #stationName}
     */
    public String getStationName() {
        return this.stationName;
    }

    /**
     * Returns the Player that owns
     * this Region.
     *
     * @return {@link Player}
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Replaces all blocks in the {@link Region}
     * which are of a certain {@link Material} type
     * to the specified {@link Material} type.
     * Used to unlock regions.
     *
     * @param to   Replace material
     */
    public void replaceAll(Material from, Material to) {
        List<Block> blockList = getBlocks();
        blockList.forEach(block -> {
            Location location = block.getLocation();
            Block toBeReplaced = location.getBlock();
            System.out.println(toBeReplaced);
            if (toBeReplaced.getType().equals(from)) {
                toBeReplaced.setType(to);
            }
        });
    }
}
