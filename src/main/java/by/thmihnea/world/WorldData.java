package by.thmihnea.world;

import com.grinderwolf.swm.api.world.SlimeWorld;

import java.util.HashMap;
import java.util.Map;

public class WorldData {

    /**
     * All {@link SlimeWorld} SlimeWorlds are
     * stored in this Map with their names
     * being used as keys.
     * The names are generally player UUIDs.
     * {@link WorldGenerator} {@link WorldUtil}
     */
    private static Map<String, SlimeWorld> worlds = new HashMap<>();

    /**
     * Adds a new SlimeWorld to the HashMap, this is pretty much
     * our way of storing worlds so that we can retrieve them
     * with ease when the player leaves.
     *
     * @param worldName  World's name
     * @param slimeWorld The SlimeWorld object
     *                   {@link SlimeWorld}
     */
    public static void addSlimeWorld(String worldName, SlimeWorld slimeWorld) {
        worlds.put(worldName, slimeWorld);
    }

    /**
     * This method removes a SlimeWorld from the HashMap
     * data structure.
     *
     * @param worldName World to be removed.
     */
    public static void removeSlimeWorld(String worldName) {
        worlds.remove(worldName);
    }

    /**
     * This method retrieves a SlimeWorld from the
     * HashMap by passing in its name
     *
     * @param worldName World to look for
     * @return {@link SlimeWorld}
     */
    public static SlimeWorld getSlimeWorld(String worldName) {
        return worlds.get(worldName);
    }
}
