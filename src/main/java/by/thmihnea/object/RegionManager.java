package by.thmihnea.object;

import by.thmihnea.EFTLobby;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionManager {

    /**
     * This class is highly used for {@link Region}
     * Edit things with precaution!
     */

    /**
     * Map to store player region objects, keyed
     * to their UUID. We store a Map of the regions that are
     * being keyed to their own regionName, since regionNames are
     * the same for the same type of station.
     */
    private static final Map<UUID, Map<String, Region>> cachedPlayerRegions = new HashMap<UUID, Map<String, Region>>();

    /**
     * Adds a region to a specified player.
     *
     * @param player Player to add the region to.
     * @param region The region object itself that has to
     *               be added to the player's Map of
     *               String, Region. {@link #cachedPlayerRegions}
     */
    public static void addRegionToPlayer(Player player, Region region) {
        Map<String, Region> regionMap = getRegionsByPlayer(player);
        if (regionMap == null) regionMap = new HashMap<String, Region>();
        regionMap.put(region.getStationName(), region);
        cachedPlayerRegions.put(player.getUniqueId(), regionMap);
    }

    /**
     * Returns the Region Map of a specified player.
     * Keep in mind, every player has different
     * Region objects.
     *
     * @param player Player to lookup for.
     * @return {@link Map}
     */
    public static Map<String, Region> getRegionsByPlayer(Player player) {
        return cachedPlayerRegions.get(player.getUniqueId());
    }

    /**
     * Removes a specified Region from the Region Map
     * of a specified player.
     *
     * @param player     Player to remove a region from.
     * @param regionName The region's name.
     */
    public static void removeRegionFromPlayer(Player player, String regionName) {
        Map<String, Region> regionMap = getRegionsByPlayer(player);
        if (regionMap == null) return;
        regionMap.remove(regionName);
        cachedPlayerRegions.put(player.getUniqueId(), regionMap);
        EFTLobby.getInstance().logInfo("Removed Region " + regionName + "from player " + player.getName() + "!");
    }

    /**
     * Removes a specified Region from the Region Map
     * of a specified player.
     *
     * @param player Player to remove a region from.
     * @param region The region itself.
     */
    public static void removeRegionFromPlayer(Player player, Region region) {
        removeRegionFromPlayer(player, region.getStationName());
        EFTLobby.getInstance().logInfo("Removed Region " + region.getStationName() + " (OBJ: " + region + ") from player " + player.getName() + "!");
    }

    /**
     * Player to have his data fully removed from our
     * HashMap-based data structure.
     * Note that this will remove all local cache, so only
     * use it when the player has already left the server!
     *
     * @param player Player to have his local data wiped.
     */
    public static void removePlayerFromCache(Player player) {
        cachedPlayerRegions.remove(player.getUniqueId());
        EFTLobby.getInstance().logInfo("Player " + player.getName() + " (UUID: " + player.getUniqueId() + ") has had his regions wiped from cache memory to free up disk space.");
    }

    /**
     * Returns a Region based of a player
     * and the regionName.
     *
     * @param player     Player to lookup for.
     * @param regionName Region name that we have to
     *                   search for.
     * @return {@link Region}
     */
    public static Region getRegionByPlayer(Player player, String regionName) {
        Map<String, Region> regionMap = getRegionsByPlayer(player);
        return regionMap.get(regionName);
    }

    /**
     * Returns the big HashMap which contains
     * every single Player hooked up to his
     * specified Region Map.
     *
     * @return {@link Map}
     */
    public static Map<UUID, Map<String, Region>> getAllRegions() {
        return cachedPlayerRegions;
    }

}
