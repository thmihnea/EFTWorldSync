package by.thmihnea.listener;

import by.thmihnea.EFTLobby;
import by.thmihnea.object.EFTPlayerManager;
import by.thmihnea.object.RegionManager;
import by.thmihnea.world.NPCData;
import by.thmihnea.world.WorldData;
import by.thmihnea.world.WorldUtil;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.nms.CraftSlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerLeaveListener implements Listener {

    /**
     * Player Quit Event, main event for when a player
     * leaves the server. Here, we make sure we save his
     * hideout in the database and remove it from the server
     * so no memory is being occupied.
     *
     * @param e The event itself
     *          {@link PlayerQuitEvent}
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        WorldUtil.saveWorlds();
        Player player = e.getPlayer();
        World world = Bukkit.getWorld(player.getUniqueId().toString());
        WorldUtil.unloadWorld(world.getName());
        SlimeWorld slimeWorld = WorldData.getSlimeWorld(world.getName());
        SlimeLoader slimeLoader = EFTLobby.getWorldAPI().getLoader("mysql");
        try {
            slimeLoader.saveWorld(slimeWorld.getName(), ((CraftSlimeWorld) slimeWorld).serialize(), true);
            slimeLoader.unlockWorld(slimeWorld.getName());
        } catch (IOException | UnknownWorldException exception) {
            exception.printStackTrace();
        }
        RegionManager.removePlayerFromCache(player);
        EFTPlayerManager.removePlayer(player);
        WorldData.removeSlimeWorld(player.getUniqueId().toString());
        NPCData.removeNPC(player);
    }
}
