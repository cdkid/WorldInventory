package cdkid.WorldInventory;

import java.util.HashMap;
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
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    public WorldInventory()
    {
    }

    public void onEnable() {
        // TODO: Place any custom enable code here including the registration of any events

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.WORLD_SAVE, worldListener, Priority.Normal, this);      //world save hook, save all players
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);     //leave event hook, save player
        pm.registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Normal, this); //teleport event hook, switch player inventory
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Priority.Low, this); //player death, update inventory (fixes item duplication via multiworld respawn)

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println("[" + pdfFile.getName() +"] v" + pdfFile.getVersion() + " is enabled!" );
    }
    public void onDisable() {
        // TODO: Place any custom disable code here
        Player[] players = this.getServer().getOnlinePlayers();
        for (Player p : players)
        {
            WIInventoryManager.saveInventory(p, p.getWorld());
        }
        // NOTE: All registered events are automatically unregistered when a plugin is disabled
        PluginDescriptionFile pdfFile = this.getDescription();
        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        System.out.println("[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + " disabled.");
    }
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
}

