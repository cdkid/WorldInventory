package cdkid.WorldInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * WorldInventory block listener
 * @author cdkid
 */
public class WorldInventoryWorldListener extends WorldListener {
    private final WorldInventory plugin;

    public WorldInventoryWorldListener(final WorldInventory plugin) {
        this.plugin = plugin;
    }
    public void onWorldSave(WorldSaveEvent event)
    {
        //save all players
        Player[] players = plugin.getServer().getOnlinePlayers();
        for (Player player : players)
        {
            WIInventoryManager.saveInventory(player, player.getWorld());
        }
    }
}
