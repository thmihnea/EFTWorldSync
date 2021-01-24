package by.thmihnea.inventory;

import by.thmihnea.Settings;
import by.thmihnea.data.SQLUtil;
import by.thmihnea.data.TableType;
import by.thmihnea.object.*;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradesInventory implements InventoryProvider {

    /**
     * SmartInventory builder of
     * our Upgrades Menu GUI.
     * Check {@link SmartInventory}
     */
    public static final SmartInventory REGION_INVENTORY = SmartInventory.builder()
            .id("2")
            .provider(new UpgradesInventory())
            .size(5, 9)
            .title(Settings.HIDEOUT_INVENTORY_UPGRADES_TITLE)
            .build();

    /**
     * Overriding the {@link InventoryProvider#init(Player, InventoryContents)}
     * method in order to initialize our
     * inventory object.
     *
     * @param player   Player to have the inventory initialized
     * @param contents Contents of the inventory.
     *                 For more information
     *                 in regards to this object, please
     *                 check {@link InventoryContents}
     */
    @Override
    public void init(Player player, InventoryContents contents) {
        EFTPlayer eftPlayer = EFTPlayerManager.getEFTPlayer(player);
        ItemStack glass = getGlass();
        contents.fillBorders(ClickableItem.empty(glass));
        AtomicInteger i = new AtomicInteger(2);
        Arrays.stream(Station.values()).forEach(station -> {
            ItemStack itemStack = new ItemStack(XMaterial.CRAFTING_TABLE.parseMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(station.getName());
            int isLocked = SQLUtil.getValue(TableType.DATA_STATIONS, station.name(), player.getUniqueId().toString());
            switch (isLocked) {
                case 0:
                    List<String> lore = Settings.HIDEOUT_INVENTORY_STATION_LOCKED_LORE;
                    lore = Settings.replaceTag(Settings.replaceTag(Settings.replaceTag(lore, "%name%", station.getName()), "%amount%", String.valueOf(station.getCost())), "%has%", String.valueOf(eftPlayer.getCoins()));
                    itemMeta.setLore(lore);
                    break;
                default:
                    List<String> lockedLore = Settings.HIDEOUT_INVENTORY_STATION_UNLOCKED_LORE;
                    itemMeta.setLore(lockedLore);
                    break;
            }
            itemStack.setItemMeta(itemMeta);
            contents.set(2, i.get(), ClickableItem.of(itemStack, e -> {
                this.upgradeStation(player, station, isLocked);
                e.getWhoClicked().closeInventory();
            }));
            i.set(i.get() + 1);
        });
        assert XMaterial.BARRIER.parseMaterial() != null;
        ItemStack barrier = new ItemStack(XMaterial.BARRIER.parseMaterial());
        ItemMeta itemMeta = barrier.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cReturn to Hideout Menu"));
        barrier.setItemMeta(itemMeta);
        contents.set(3, 7, ClickableItem.of(barrier, e -> HideoutInventory.HIDEOUT_INVENTORY.open(player)));
    }

    /**
     * Overriding the {@link InventoryProvider#update(Player, InventoryContents)}
     * method in order to update
     * our inventory object every tick.
     *
     * @param player   Player to have the inventory initialized
     * @param contents Contents of the inventory.
     *                 For more information
     *                 in regards to this object, please
     *                 check {@link InventoryContents}
     */
    @Override
    public void update(Player player, InventoryContents contents) {
        this.init(player, contents);
    }

    /**
     * Returns our Glass item which
     * is used as a delimiter.
     *
     * @return {@link org.bukkit.Material}
     */
    private ItemStack getGlass() {
        assert XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial() != null;
        ItemStack glass = new ItemStack(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial());
        ItemMeta glassMeta = glass.getItemMeta();
        assert glassMeta != null;
        glassMeta.setDisplayName(" ");
        glassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        glass.setItemMeta(glassMeta);
        return glass;
    }

    /**
     * Upgrades a specific {@link Station}
     * for a specific {@link Player}, depending on
     * whether or not the station is locked.
     *
     * @param player   The player which we're upgrading
     *                 the station for.
     * @param station  The station which we're
     *                 upgrading.
     * @param isLocked If the station is locked
     *                 or not.
     */
    private void upgradeStation(Player player, Station station, int isLocked) {
        if (isLocked != 0) {
            player.sendMessage(Settings.HIDEOUT_STATION_ALREADY_MAXED_OUT);
            return;
        }
        EFTPlayer eftPlayer = EFTPlayerManager.getEFTPlayer(player);
        if (eftPlayer.getCoins() < station.getCost()) {
            player.sendMessage(Settings.HIDEOUT_STATION_NOT_ENOUGH_COINS);
            return;
        }
        Region region = RegionManager.getRegionByPlayer(player, station.name());
        region.getBlocks().forEach(block -> {
            if (block.getType().equals(XMaterial.BARRIER.parseMaterial())) block.setType(XMaterial.AIR.parseMaterial());
        });
        SQLUtil.setValue(TableType.DATA_STATIONS, station.name(), player.getUniqueId().toString(), 1);
        eftPlayer.setCoins(eftPlayer.getCoins() - station.getCost());
        assert XSound.ENTITY_PLAYER_LEVELUP.parseSound() != null;
        player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0f, 1.0f);
    }
}
