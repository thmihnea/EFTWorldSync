package by.thmihnea.world;

import by.thmihnea.EFTLobby;
import by.thmihnea.Settings;
import by.thmihnea.object.Region;
import by.thmihnea.object.Station;
import com.boydti.fawe.util.TaskManager;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldUtil {

    /**
     * Copies the file structure of a world and imports it to another file.
     *
     * @param source the original world which we have to copy
     * @param target the target destination to which files have to go
     */
    public static void copyFileStructure(File source, File target) {
        try {
            ArrayList<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes every file under a certain path
     *
     * @param path parent file
     * @return
     */
    public static void deleteFile(File path) {
        try {
            FileUtils.deleteDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a world from the WorldContainer
     *
     * @param world World to be deleted
     */
    public static void deleteWorld(World world) {
        String name = world.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete " + name);
        Bukkit.getScheduler().scheduleSyncDelayedTask(EFTLobby.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvconfirm");
        }, 10L);
    }

    /**
     * Imports a world into Bukkit from files
     *
     * @param worldName World to be imported
     */
    public static void importWorldMultiverse(String worldName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv import " + worldName + " NORMAL");
    }

    /**
     * Saves all worlds so that we don't lose data.
     */
    public static void saveWorlds() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
    }

    /**
     * Loads the specified world into memory
     *
     * @param worldName World to be loaded
     */
    public static void loadWorldMultiverse(String worldName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load " + worldName);
    }

    /**
     * Loads a slime world from the database.
     * Before loading, make sure the world actually exists
     * and that it is imported.
     *
     * @param worldName World to be loaded.
     */
    public static void loadSlimeWorld(String worldName) {
        SlimeLoader slimeLoader = EFTLobby.getWorldAPI().getLoader("mysql");
        try {
            SlimeWorld slimeWorld = EFTLobby.getWorldAPI().loadWorld(slimeLoader, worldName, false, getSlimePropertyMap());
            slimeLoader.unlockWorld(slimeWorld.getName());
            WorldData.addSlimeWorld(worldName, slimeWorld);
            EFTLobby.getWorldAPI().generateWorld(slimeWorld);
        } catch (IOException | CorruptedWorldException | WorldInUseException | NewerFormatException | UnknownWorldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imports a Slime World to the Database.
     * This is the method we first have to call before
     * actually loading a world (pulling it).
     *
     * @param worldName World to be imported.
     */
    public static void importSlimeWorld(String worldName) {
        String path = Bukkit.getWorldContainer().getAbsolutePath();
        File worldDir = new File(path + "/" + worldName);
        SlimeLoader slimeLoader = EFTLobby.getWorldAPI().getLoader("mysql");
        try {
            EFTLobby.getWorldAPI().importWorld(worldDir, worldName, slimeLoader);
        } catch (IOException | InvalidWorldException | WorldLoadedException | WorldAlreadyExistsException | WorldTooBigException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unloads a world from the server.
     * Use this as {@link Bukkit#unloadWorld(String, boolean)}
     * tends to be buggy and might not work for odd reasons.
     *
     * @param worldName World to be unloaded.
     */
    public static void unloadWorld(String worldName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "swm unload " + worldName);
    }

    /**
     * Returns the property map for a Slime World.
     * Change it as you wish.
     *
     * @return {@link SlimePropertyMap}
     */
    public static SlimePropertyMap getSlimePropertyMap() {
        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        slimePropertyMap.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, (int) Settings.SPAWN_X);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, (int) Settings.SPAWN_Z);
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, (int) Settings.SPAWN_Y);
        return slimePropertyMap;
    }

    /**
     * Pastes a schematic at a given location.
     * Operation might be laggy, but we've made it
     * async via FAWE.
     *
     * @param file     File which contains the schematic.
     * @param location Location at which the schematic should be pasted.
     */
    public static boolean pasteSchematic(File file, Location location) {
        TaskManager.IMP.async(() -> {
            try {
                Vector vector = new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1);
                MCEditSchematicFormat.getFormat(file).load(file).paste(editSession, vector, false);
            } catch (MaxChangedBlocksException | IOException | DataException | NullPointerException e) {
                e.printStackTrace();
                EFTLobby.getInstance().logSevere("A NullPointerException has occurred while attempting to paste in the schematic for file " + file.getName() + ".");
                EFTLobby.getInstance().logSevere("Chunks are being re-allocated.");
            }
        });
        return true;
    }

    /**
     * Method to generate all regions accordingly.
     * This method is extremely important for
     * each hideout and is used along with
     * {@link by.thmihnea.object.Region}, {@link by.thmihnea.object.RegionManager}
     * and {@link by.thmihnea.object.Station}. Please, modify carefully!
     *
     * @param player Player to have his regions
     *               generated. Check {@link WorldGenerator},
     *               {@link by.thmihnea.listener.PlayerJoinListener}
     *               and {@link by.thmihnea.listener.PlayerLeaveListener}
     *               to understand the thought process behind this.
     *               We are assuming the player has had his world
     *               already generated before running this method,
     *               otherwise it could cause serious trouble!
     */
    public static void generateRegions(Player player) {
        World world = player.getWorld();
        Arrays.stream(Station.values()).forEach(station -> {
            Location location1 = new Location(world, station.getX1(), station.getY1(), station.getZ1());
            Location location2 = new Location(world, station.getX2(), station.getY2(), station.getZ2());
            new Region(player, location1, location2, station.name());
        });
    }

}
