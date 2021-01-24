package by.thmihnea.listener;

import by.thmihnea.EFTLobby;
import by.thmihnea.Settings;
import by.thmihnea.data.SQLUtil;
import by.thmihnea.object.EFTPlayer;
import by.thmihnea.world.NPCData;
import by.thmihnea.world.WorldGenerator;
import by.thmihnea.world.WorldUtil;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;

public class PlayerJoinListener implements Listener {

    /**
     * Player Join Event, main event for when a player
     * joins the server. Here, we render out his hideout
     * and make sure everything is being set up properly.
     *
     * @param e The event itself
     *          {@link PlayerJoinEvent}
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SQLUtil.init(player);
        EFTPlayer eftPlayer = new EFTPlayer(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 5));
        SlimeLoader slimeLoader = EFTLobby.getWorldAPI().getLoader("mysql");
        boolean exists = false;
        try {
            exists = slimeLoader.worldExists(player.getUniqueId().toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if (exists) {
            try {
                slimeLoader.unlockWorld(player.getUniqueId().toString());
            } catch (UnknownWorldException | IOException unknownWorldException) {
                unknownWorldException.printStackTrace();
            }
            WorldGenerator.loadHideout(player);
        } else
            WorldGenerator.generateEmptyWorld(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(EFTLobby.getInstance(), () -> {
            WorldUtil.generateRegions(player);
            NPCData.spawnNPC(player);
        }, 80L);
    }
}
