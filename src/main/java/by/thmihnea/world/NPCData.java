package by.thmihnea.world;

import by.thmihnea.Settings;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCData {

    /**
     * The {@link Map} HashMap which
     * stores all cached NPCs and bind
     * them to an {@link UUID} of the
     * specified player.
     */
    private static final Map<UUID, NPC> cachedNPCs = new HashMap<>();

    /**
     * Method used to spawn the Raider NPC
     * in each hideout per player.
     * The NPC gets a {@link SkinTrait} trait set
     * to them and a {@link LookClose} trait as
     * well.
     *
     * @param player Player to have the NPC spawned
     */
    public static void spawnNPC(Player player) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.YELLOW + "Raider " + ChatColor.GRAY + "(Right-Click)");
        Location location = new Location(player.getWorld(), Settings.NPC_X, Settings.NPC_Y, Settings.NPC_Z);
        npc.spawn(location);
        npc.getOrAddTrait(LookClose.class).toggle();
        npc.getOrAddTrait(SkinTrait.class).setSkinName("RedCan_TV");
        cachedNPCs.put(player.getUniqueId(), npc);
    }

    /**
     * Method used to despawn the Raider NPC
     * in each hideout per player.
     *
     * @param player Player to despawn the NPC
     *               for.
     */
    public static void removeNPC(Player player) {
        NPC npc = cachedNPCs.get(player.getUniqueId());
        if (npc == null) return;
        npc.despawn();
        cachedNPCs.remove(player.getUniqueId());
    }

    /**
     * Returns an instance of the NPC
     * which is attributed to each individual
     * player in our Map. {@link NPC}
     *
     * @param player The player that we're
     *               looking after.
     * @return {@link NPC}
     */
    public static NPC getNPCByPlayer(Player player) {
        return cachedNPCs.get(player.getUniqueId());
    }
}
