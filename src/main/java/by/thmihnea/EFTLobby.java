package by.thmihnea;

import by.thmihnea.data.SQLConnection;
import by.thmihnea.listener.NPCClickListener;
import by.thmihnea.listener.PlayerJoinListener;
import by.thmihnea.listener.PlayerLeaveListener;
import by.thmihnea.object.EFTPlayer;
import by.thmihnea.world.WorldUtil;
import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class EFTLobby extends AbstractPlugin {

    /**
     * Time at which the plugin is enabled
     */
    private long start;

    /**
     * Main class instance. Correlates to AbstractPlugin's instance.
     * {@link AbstractPlugin#getInstance()}
     */
    private static EFTLobby instance;

    /**
     * SQLConnection object so that we can connect to the database
     * {@link SQLConnection}
     */
    private SQLConnection con;

    /**
     * A list of all the listeners that have to be registered by our plugin
     */
    private final List<Listener> listeners = Arrays.asList(
            new PlayerJoinListener(),
            new PlayerLeaveListener(),
            new NPCClickListener()
    );

    /**
     * Accesses the instance of AbstractPlugin via EFTLobby.
     *
     * @return AbstractPlugin instance {@link AbstractPlugin#getInstance()}
     */
    public static EFTLobby getInstance() {
        return instance;
    }

    /**
     * Accesses the instance of the Connection established via MySQL
     *
     * @return SQLConnection object {@link SQLConnection#getConnection()}
     */
    public SQLConnection getCon() {
        return this.con;
    }

    /**
     * Slime World Manager's instance
     * This instance is being used to pull/push world
     * information to the database.
     * {@link WorldUtil} {@link by.thmihnea.world.WorldGenerator}
     */
    private static final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    /**
     * @return an instance of SlimePlugin
     */
    public static SlimePlugin getWorldAPI() {
        return slimePlugin;
    }

    /**
     * Start method that replaced onEnable.
     * Check {@link AbstractPlugin#start()}
     */
    @Override
    protected void start() {
        this.start = System.currentTimeMillis();
        this.con = new SQLConnection();
        this.setupInstance();
        this.registerEvents(this.listeners);
        this.setupFiles();
        this.setupObjects();
        this.logInfo("Plugin has been successfully enabled! Process took: " + (System.currentTimeMillis() - this.start) + "ms");
    }

    /**
     * Stop method that replaced onDisable.
     * Check {@link AbstractPlugin#stop()}
     */
    @Override
    protected void stop() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = Bukkit.getWorld(player.getUniqueId().toString());
            WorldUtil.deleteWorld(world);
        });
        this.logInfo("Plugin has been successfully disabled!");
    }

    /**
     * Setups the {@link EFTPlayer} objects
     * for each {@link org.bukkit.entity.Player}
     * on the server (in case we reload).
     */
    private void setupObjects() {
        Bukkit.getOnlinePlayers().forEach(EFTPlayer::new);
    }

    /**
     * Sets up the instance for the
     * {@link EFTLobby} class.
     */
    private void setupInstance() {
        instance = this;
    }
}
