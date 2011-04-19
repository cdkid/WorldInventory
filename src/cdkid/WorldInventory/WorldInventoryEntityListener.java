package cdkid.WorldInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class WorldInventoryEntityListener extends EntityListener {
    public void onEntityDeath(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Player) //somebody died
        {
            WIInventoryManager.saveInventory((Player) event.getEntity(), event.getEntity().getWorld());
        }
    }
}
