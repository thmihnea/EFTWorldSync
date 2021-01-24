package by.thmihnea.object;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EFTPlayerManager {

    /**
     * This class is highly used for {@link EFTPlayer}
     * Edit things with precaution
     */

    /**
     * Map to store player objects keyed to their uuid
     */
    private static final Map<UUID, EFTPlayer> cachedObjects = new HashMap<>();

    /**
     * Adds an EFTPlayer object into cached memory
     *
     * @param object - object to be added
     */
    public static void addPlayer(EFTPlayer object) {
        cachedObjects.put(object.getUniqueId(), object);
    }

    /**
     * Removes an EFTPlayer object from cached memory
     *
     * @param player - played to be removed
     */
    public static void removePlayer(Player player) {
        cachedObjects.remove(player.getUniqueId());
    }

    /**
     * @param player - player to lookup for
     * @return an EFTPlayer object
     * @Getter gets an EFTPlayer object from cached memory
     */
    public static EFTPlayer getEFTPlayer(Player player) {
        return cachedObjects.get(player.getUniqueId());
    }
}
