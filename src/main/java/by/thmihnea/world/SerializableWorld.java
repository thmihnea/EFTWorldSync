package by.thmihnea.world;

import by.thmihnea.EFTLobby;
import org.bukkit.World;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class SerializableWorld {

    /**
     * World parameter
     */
    private World world;

    /**
     * Serialized string
     */
    private String serialized;

    /**
     * Default constructor
     *
     * @param world First parameter of constructor
     */
    public SerializableWorld(World world) {
        this.world = world;
        this.serialize();
    }

    /**
     * Returns object world
     *
     * @return world
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Serializes the given world
     */
    public void serialize() {
        String encodedObject;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(this.world);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            encodedObject = Base64.getEncoder().encodeToString(serializedObject);
            this.serialized = encodedObject;
        } catch (IOException e) {
            e.printStackTrace();
            EFTLobby.getInstance().logSevere("An error occured while trying to serialize world " + this.world.getName() + "! Please report this stacktrace to thmihnea.");
        }
    }

    /**
     * Returns the serialized string
     *
     * @return serialized
     */
    public String getSerialized() {
        return this.serialized;
    }
}
