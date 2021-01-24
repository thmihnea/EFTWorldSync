package by.thmihnea.runnables;

import by.thmihnea.EFTLobby;
import by.thmihnea.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class HideoutTeleportTask implements Runnable {

    /**
     * The world where we have to
     * teleport the player.
     * {@link World}
     */
    private World world;

    /**
     * The player object itself.
     * {@link Player}
     */
    private Player player;

    /**
     * The BukkitTask (scheduled runnable).
     * {@link BukkitTask}
     */
    private BukkitTask bukkitTask;

    /**
     * Constructor for the Hideout
     * Teleportation task.
     *
     * @param player Player to be teleported.
     * @param world  World to teleport the player in.
     */
    public HideoutTeleportTask(Player player, World world) {
        this.bukkitTask = Bukkit.getScheduler().runTaskTimer(EFTLobby.getInstance(), this, 60L, 20L);
        this.player = player;
        this.world = world;
    }

    /**
     * Run method.
     * See {@link Runnable}
     */
    @Override
    public void run() {
        if (this.player.getWorld().getName().equalsIgnoreCase(world.getName())) {
            this.clear();
        } else {
            player.teleport(world.getSpawnLocation());
            Location location = player.getLocation();
            location.setPitch(Settings.PITCH);
            location.setYaw(Settings.YAW);
            player.teleport(location);
        }
    }

    /**
     * This method helps us
     * clear the runnable when we're done with
     * it so we don't keep the object
     * in memory. Basically a garbage collector.
     */
    public void clear() {
        if (this.bukkitTask != null) {
            Bukkit.getScheduler().cancelTask(this.bukkitTask.getTaskId());
            this.bukkitTask = null;
        }
    }
}
