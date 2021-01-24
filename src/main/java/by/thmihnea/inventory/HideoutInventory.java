package by.thmihnea.inventory;

import by.thmihnea.Settings;
import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HideoutInventory implements InventoryProvider {

    /**
     * SmartInventory builder of
     * our Hideout Menu GUI.
     * Check {@link SmartInventory}
     */
    public static final SmartInventory HIDEOUT_INVENTORY = SmartInventory.builder()
            .id("1")
            .provider(new HideoutInventory())
            .size(5, 9)
            .title(Settings.HIDEOUT_INVENTORY_TITLE)
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
        ItemStack glass = getGlass();
        ItemStack regions = getRegions();
        ItemStack arenas = getJoinGame();
        contents.fillBorders(ClickableItem.empty(glass));
        contents.set(2, 3, ClickableItem.of(regions, e -> UpgradesInventory.REGION_INVENTORY.open(player)));
        contents.set(2, 5, ClickableItem.empty(arenas));
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
        glassMeta.setDisplayName(" "); // 2,3 2,5
        glassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        glass.setItemMeta(glassMeta);
        return glass;
    }

    /**
     * Returns our region item
     * which we then use in our GUI.
     *
     * @return {@link org.bukkit.Material}
     */
    private ItemStack getRegions() {
        assert XMaterial.NETHER_STAR.parseMaterial() != null;
        ItemStack regions = new ItemStack(XMaterial.NETHER_STAR.parseMaterial());
        ItemMeta meta = regions.getItemMeta();
        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(Settings.HIDEOUT_INVENTORY_REGIONS);
        meta.setLore(Settings.HIDEOUT_INVENTORY_REGIONS_LORE);
        regions.setItemMeta(meta);
        return regions;
    }

    /**
     * Returns our arena item
     * which we then use in our GUI.
     *
     * @return {@link org.bukkit.Material}
     */
    private ItemStack getJoinGame() {
        assert XMaterial.DIAMOND_AXE.parseMaterial() != null;
        ItemStack regions = new ItemStack(XMaterial.DIAMOND_AXE.parseMaterial());
        ItemMeta meta = regions.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Settings.HIDEOUT_INVENTORY_JOIN_GAME);
        meta.setLore(Settings.HIDEOUT_INVENTORY_JOIN_GAME_LORE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        regions.setItemMeta(meta);
        return regions;
    }
}
