package cdkid.WorldInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Handle events for all Player related events
 * @author cdkid
 */
public class WorldInventoryPlayerListener extends PlayerListener {
    public WorldInventoryPlayerListener() {
    }

    public void onPlayerQuit (PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        WIInventoryManager.saveInventory(player, player.getWorld());
    }
    public void onPlayerTeleport (PlayerTeleportEvent event)
    {
        //if they teleport to a different world, save/load inventory
        Player player = event.getPlayer();
        long to, from;
        to = event.getTo().getWorld().getId();
        from = event.getFrom().getWorld().getId();
        if (to == from)
            return;
        WIInventoryManager.saveInventory(player, event.getFrom().getWorld());
        WIInventoryManager.loadInventory(player, event.getTo().getWorld());
    }
}

