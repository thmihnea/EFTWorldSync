package by.thmihnea;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public abstract class AbstractPlugin extends JavaPlugin {

    /**
     * Config file of the plugin
     */
    private static File config = new File("plugins/EFTLobby/config.yml");

    /**
     * FileConfiguration of config file
     */
    private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);

    /**
     * The abstract class' instance.
     */
    private static AbstractPlugin instance;

    /**
     * World Directory of player worlds so we can save their Hideouts
     */
    private static File hideoutDirectory;

    /**
     * This is just a shortcut for getDescription().getVersion()
     *
     * @return plugin version
     */
    public String getVersion() {
        return getDescription().getVersion();
    }

    /**
     * This is just a shortcut for getDataFolder()
     *
     * @return plugin data folder
     */
    public File getData() {
        return getDataFolder();
    }

    /**
     * This is just a short for getFile()
     *
     * @return plugin source file (jar file)
     */
    public File getSourceFile() {
        return getFile();
    }

    /**
     * This method returns the Hideout Directory for out worlds
     *
     * @return hideout directory
     */
    public File getHideoutDirectory() {
        return hideoutDirectory;
    }

    /**
     * Sets the hideout directory to the desired file
     *
     * @param file set hideout directory to this file
     */
    public void setHideoutDirectory(File file) {
        hideoutDirectory = file;
    }

    /**
     * Returns the instance of {@link AbstractPlugin}
     *
     * @return this instance
     */
    public static AbstractPlugin getInstance() {
        if (instance == null) {
            try {
                instance = JavaPlugin.getPlugin(AbstractPlugin.class);
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, "Failed to instantiate plugin. Please, make sure you installed everything correctly!");
            }
        }
        return instance;
    }

    /**
     * Return the config file of the plugin
     *
     * @return config file
     */
    public static File getConfigFile() {
        return config;
    }

    /**
     * Return the FileConfiguration of the config file
     *
     * @return cfg
     */
    public static FileConfiguration getCfg() {
        return cfg;
    }

    /**
     * @return if instance is set
     */
    public static boolean hasInstance() {
        return instance != null;
    }

    /**
     * Call this method when the plugin is started.
     * {@link JavaPlugin#onLoad()}
     */
    protected void load() {
    }

    /**
     * Call this method before the plugin gets loaded.
     */
    protected void preStart() {
    }

    /**
     * This is the main starting method of the plugin. Replaces onEnable.
     * {@link JavaPlugin#onEnable()}
     */
    protected abstract void start();

    /**
     * This is the main stopping method of the plugin. Replaces onDisable.
     * {@link JavaPlugin#onDisable()}
     */
    protected abstract void stop();

    /**
     * Invoked after plugin is reloaded.
     */
    protected void reload() {
    }

    /**
     * Invoked before plugin is reloaded.
     */
    protected void preReload() {
    }

    /**
     * Here we override our onLoad method to apply our own methods to it.
     * {@link #onLoad()}
     */
    @Override
    public void onLoad() {
        this.load();
    }

    /**
     * Here we override our onEnable method to apply our own methods to it.
     * {@link #onEnable()}
     */
    @Override
    public void onEnable() {
        if (!isEnabled()) return;
        this.preStart();
        if (!isEnabled()) return; // Detects if an error occurred during plugin prestart.
        this.start();
    }

    /**
     * Here we override our onDisable method to apply our own methods to it.
     * {@link #onDisable()}
     */
    @Override
    public void onDisable() {
        this.stop();
    }

    /**
     * Registers an event if a certain condition is met
     *
     * @param listener  - Listener to be registered
     * @param condition - Condition to be met when registering wanted listener
     */
    protected final void registerEventIf(Listener listener, boolean condition) {
        if (condition) this.registerEvent(listener);
    }

    /**
     * Registers an event
     *
     * @param listener - Listener to be registered
     */
    protected final void registerEvent(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers a list of listeners
     *
     * @param listeners - The list of listeners which have to be registered
     */
    protected final void registerEvents(List<Listener> listeners) {
        listeners.forEach(this::registerEvent);
    }

    /**
     * Registers each listener in a list of listeners if a certain condition is met
     *
     * @param listeners - The list of listeners which have to be registered
     * @param condition - Condition to be met when registering wanted listener
     */
    protected final void registerEventsIf(List<Listener> listeners, boolean condition) {
        listeners.forEach(listener -> this.registerEventIf(listener, condition));
    }

    /**
     * Registers the plugin config.
     */
    protected final void setupFiles() {
        File dir = new File("plugins", getName());
        if (!(dir.exists())) dir.mkdir();
        File schemDir = new File("plugins/EFTLobby/schematics");
        if (!(schemDir.exists())) schemDir.mkdir();
        if (!(getConfigFile().exists())) saveDefaultConfig();
        try {
            getCfg().load(getConfigFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            logSevere("An error has occurred while trying to setup the plugin config. Please report this stacktrace to thmihnea!");
        }
    }

    /**
     * Logs a message to the console using the INFO Level
     *
     * @param message - Message to be logged
     */
    public void logInfo(String... message) {
        String s = Arrays.toString(message);
        Bukkit.getLogger().log(Level.INFO, "[" + getName() + "]" + " " + s.replace(s.substring(0, 1), "").replace(s.substring(s.length() - 1), ""));
    }

    /**
     * Logs a message to the console using the SEVERE Level
     *
     * @param message - Message to be logged
     */
    public void logSevere(String... message) {
        String s = Arrays.toString(message);
        Bukkit.getLogger().log(Level.INFO, "[" + getName() + "]" + " " + s.replace(s.substring(0, 1), "").replace(s.substring(s.length() - 1), ""));
    }

    /**
     * Sets up the Region objects for the
     * staton on plugin load. Used a protected void
     * because we're only using this in {@link EFTLobby}
     * See {@link by.thmihnea.object.Region} and {@link by.thmihnea.object.RegionManager}
     * for more information.
     */
    protected void setupRegions() {

    }
}
