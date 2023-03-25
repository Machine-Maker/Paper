package io.papermc.testplugin;

import io.papermc.paper.event.inventory.RecipeCraftEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEvent(RecipeCraftEvent event) {
        event.setResult(new ItemStack(Material.STICK, 1));
        System.out.println(event.getResult());
        System.out.println(event.getRecipe());
    }
}
