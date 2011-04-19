package cdkid.WorldInventory;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/****************************************
 * Format for saving inventories is as follows plugins/WorldInventory/<world
 * name>/<player name>.inv Format of .inv file:
 * id,size,durability,data|id2,size2,durability2,data2|. . . of type
 * int, int, short, byte
 * the first 4 positions store head, chest, legs, boots respectively
 */
public class WIInventoryManager {
    private static final String stackSep        = "|";
    private static final String fieldSep        = ",";
    public static final String  stackSepPattern = "[\\|]";
    public static final String  fieldSepPattern = "[,]";
    private static final String separator = System.getProperty("file.separator");
    private static final String pluginPath = "plugins" + separator + "WorldInventory" + separator;

    public static void saveInventory(Player player, World world) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        String path = pluginPath + world.getName() + separator + player.getName() + ".inv";
        FileWriter writer;
        File inventoryFile = createOrOpenFile(path);
        inventoryFile.delete();
        try {
            inventoryFile.createNewFile();
            writer = new FileWriter(inventoryFile);
        } catch (IOException e) {
            return;
        }
        String outLine;
        //write player armor: head, chest, legs boots
        ItemStack head = inventory.getHelmet();
        ItemStack chest = inventory.getChestplate();
        ItemStack legs = inventory.getLeggings();
        ItemStack boots = inventory.getBoots();
        if (head.getAmount() == 0)
            outLine = fieldSep+fieldSep+fieldSep+stackSep;
        else
            outLine = itemToString(head)+stackSep;
        if (chest.getAmount() == 0)
            outLine += fieldSep+fieldSep+fieldSep+stackSep;
        else
            outLine += itemToString(chest)+stackSep;
        if (legs.getAmount() == 0)
            outLine += fieldSep+fieldSep+fieldSep+stackSep;
        else
            outLine += itemToString(legs)+stackSep;
        if (boots.getAmount() == 0)
            outLine += fieldSep+fieldSep+fieldSep;
        else
            outLine += itemToString(boots);
        try {
            writer.write(outLine);
        } catch (IOException e) {
            System.out.println(WorldInventory.pluginName + " - Error Saving Player File. Unable to write.");
            return;
        }
        for (ItemStack item : items)
        {
            if (item == null)
                continue;
            //format: id, size, dur, data
            outLine = itemToString(item);
            try {
                writer.write(stackSep + outLine);
            } catch (IOException e) {
                System.out.println(WorldInventory.pluginName + " - Error Saving Player File. Unable to write.");
                return;
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println(WorldInventory.pluginName + " - Error Saving Player File. Unable to close file.");
            return;
        }
    }
    private static String itemToString(ItemStack item)
    {
        if (item == null)
        {
            return fieldSep+fieldSep+fieldSep;
        }
        String ret = item.getTypeId() + fieldSep + item.getAmount() + fieldSep + item.getDurability() + fieldSep;
        byte data;
        if (item.getData() != null)
            data = item.getData().getData();
        else
            data = 0x0;
        ret += data;
        return ret;
    }
    public static void loadInventory(Player player, World world) {
        PlayerInventory pi = player.getInventory();
        pi.clear();
        pi.setHelmet(null);
        pi.setChestplate(null);
        pi.setLeggings(null);
        pi.setBoots(null);
        LinkedList<ItemStack> items = getPlayerInventory(player.getName(), world.getName());
        if (items == null)
        {
            System.out.println("No Inventory");
            return;
        }
        int i = 0;
        for (ItemStack item : items)
        {
            if (item == null)
            {
                i++;
                continue;
            }
            if (i == 0) //head
            {
                pi.setHelmet(item);
                i++;
                continue;
            }
            else if (i == 1) //chest
            {
                pi.setChestplate(item);
                i++;
                continue;
            }
            else if (i == 2) //legs
            {
                pi.setLeggings(item);
                i++;
                continue;
            }
            else if (i == 3) //boots
            {  
                pi.setBoots(item);
                i++;
                continue;
            }
            pi.addItem(item);
        }
    }
    private static File createOrOpenFile(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            return file;
        }
        //file doesn't exist, make directory structure & file
        String parent = file.getParent();
        if (parent != null)
        {
            File parentDir = new File(file.getParent());
            parentDir.mkdirs();
        }
        try
        {
            file.createNewFile();
        }
        catch (Exception ex)
        {
            return null;
        }
        return file;
    }
    private static LinkedList<ItemStack> getPlayerInventory(String player, String world)
    {
        String path = pluginPath + world + separator + player + ".inv";
        File playerFile = createOrOpenFile(path);
        Scanner reader;
        if (playerFile == null)
        {
            return null;
        }
        LinkedList<ItemStack> ret = new LinkedList<ItemStack>();
        try {
            reader = new Scanner(playerFile);
        } catch (FileNotFoundException e) {
            reader = new Scanner("");
        }
        reader.useDelimiter(stackSepPattern);
        while (reader.hasNext())
        {
            String stack = reader.next();
            if (stack.equals(fieldSep+fieldSep+fieldSep))
            {
                ret.add(null);
                continue;
            }
            String[] stackData = stack.split(fieldSepPattern);
            if (stackData.length < 4)
            {
                continue;
            }
            int id, count;
            byte data;
            short durability;
            id = Integer.parseInt(stackData[0]);
            count = Integer.parseInt(stackData[1]);
            durability = Short.parseShort(stackData[2]);
            data = Byte.parseByte(stackData[3]);
            ItemStack i = new ItemStack(id, count);
            MaterialData md = new MaterialData(id, data);
            i.setData(md);
            i.setDurability(durability);
            ret.add(i);
        }
        return ret;
    }
}
