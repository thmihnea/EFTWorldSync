package by.thmihnea.listener;

import by.thmihnea.inventory.HideoutInventory;
import by.thmihnea.world.NPCData;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClickListener implements Listener {

    /**
     * This is an event which listens
     * for clicks on NPCs so that we know
     * when to open the {@link HideoutInventory}
     *
     * @param e Event that we listen for.
     *          See {@link NPCRightClickEvent}
     */
    @EventHandler
    public void onNPCClick(NPCRightClickEvent e) {
        NPC npc = e.getNPC();
        if (!(NPCData.getNPCByPlayer(e.getClicker()).equals(npc))) return;
        HideoutInventory.HIDEOUT_INVENTORY.open(e.getClicker());
    }
}
