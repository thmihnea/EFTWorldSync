package by.thmihnea.world;

import by.thmihnea.EFTLobby;
import by.thmihnea.Settings;
import by.thmihnea.runnables.HideoutTeleportTask;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class WorldGenerator {

    /**
     * Our method to generate an empty world to which the player gets teleported afterwards
     * We are making the world empty since we only have to store their hideout there
     * We also don't want them to be able to see other players/they won't know the world is void either way
     *
     * @param player - Player to have the world generated for him
     */
    public static void generateEmptyWorld(Player player) {
        WorldCreator worldCreator = getWorldCreator(player);
        worldCreator.createWorld();
        World world = Bukkit.getWorld(player.getUniqueId().toString());
        assert world != null;
        world.setSpawnLocation(Settings.GAP, 65, Settings.GAP);
        File file = new File(Settings.SCHEMATIC_PATH);
        CompletableFuture.runAsync(() -> WorldUtil.unloadWorld(world.getName()))
                .thenRun(() -> initAsyncGeneration(player, world, file));
        new HideoutTeleportTask(player, world);
    }

    /**
     * Util method to generate the Empty Void World
     * for {@link #generateEmptyWorld(Player)}
     *
     * @param player Player to have the world generated
     *               for him.
     * @param world  World to have the schematics pasted
     *               in.
     * @param file   Schematic file.
     */
    private static void initAsyncGeneration(Player player, World world, File file) {
        CompletableFuture.runAsync(() -> WorldUtil.importSlimeWorld(player.getUniqueId().toString()))
                .thenRun(() -> WorldUtil.loadSlimeWorld(world.getName()))
                .thenRun(() -> WorldUtil.pasteSchematic(file, world.getSpawnLocation()))
                .thenRun(() -> world.setSpawnLocation((int) Settings.SPAWN_X, (int) Settings.SPAWN_Y, (int) Settings.SPAWN_Z));
    }

    /**
     * Util method to generate the Empty Void World
     * for {@link #generateEmptyWorld(Player)}
     *
     * @param player Player to get the World Creator for.
     * @return {@link WorldCreator}
     */
    private static WorldCreator getWorldCreator(Player player) {
        WorldCreator worldCreator = new WorldCreator(player.getUniqueId().toString());
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("2;0;1");
        return worldCreator;
    }

    /**
     * Loads the desired player's specific hideout from\ memory.
     *
     * @param player Player to have his hideout loaded.
     */
    public static void loadHideout(Player player) {
        WorldUtil.loadSlimeWorld(player.getUniqueId().toString());
        Bukkit.getScheduler().scheduleSyncDelayedTask(EFTLobby.getInstance(), () -> {
            World world = Bukkit.getWorld(player.getUniqueId().toString());
            player.teleport(world.getSpawnLocation());
            Location location = player.getLocation();
            location.setPitch(Settings.PITCH);
            location.setYaw(Settings.YAW);
            player.teleport(location);
        }, 10L);
    }
}
