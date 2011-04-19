package cdkid.WorldInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * WorldInventory for Bukkit
 *
 * @author cdkid
 */
public class WorldInventory extends JavaPlugin {
    public static final String pluginName = "WorldInventory v0.1";
    private final WorldInventoryPlayerListener playerListener = new WorldInventoryPlayerListener();
    private final WorldInventoryWorldListener worldListener = new WorldInventoryWorldListener(this);
    private final WorldInventoryEntityListener entityListener = new WorldInventoryEntityListener();

    public void onEnable() {

        // Register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.WORLD_SAVE, worldListener, Priority.Normal, this);      //world save hook, save all players
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);     //leave event hook, save player
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Normal, this); //teleport event hook, switch player inventory
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Low, this); //player death, update inventory (fixes item duplication via multiworld respawn)
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() +"] v" + pdfFile.getVersion() + " is enabled!" );
    }
    public void onDisable() {
        Player[] players = this.getServer().getOnlinePlayers();
        for (Player p : players)
        {
            WIInventoryManager.saveInventory(p, p.getWorld());
        }
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " disabled.");
    }
}

