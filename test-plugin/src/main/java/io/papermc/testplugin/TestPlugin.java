package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import java.util.List;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class TestPlugin extends JavaPlugin implements Listener {

    static final NamespacedKey SHAPELESS_KEY = new NamespacedKey("test", "shapeless");
    static final NamespacedKey SHAPED_KEY = new NamespacedKey("test", "shaped");

    static final ItemStack SHAPELESS_INGREDIENT = new ItemStack(Material.EMERALD);
    static final NamespacedKey PDC_KEY = new NamespacedKey("test", "pdc_key");
    static {
        SHAPELESS_INGREDIENT.editMeta(meta -> {
            meta.displayName(text("Epic Name", YELLOW));
            meta.getPersistentDataContainer().set(PDC_KEY, PersistentDataType.BOOLEAN, true);
        });
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        final ShapelessRecipe shapeless = new ShapelessRecipe(SHAPELESS_KEY, new ItemStack(Material.DIAMOND));
        shapeless.addIngredient(Material.STICK);
        for (int i = 0; i < 4; i++) {
            shapeless.addIngredient(SHAPELESS_INGREDIENT);
        }
        this.getServer().addRecipe(shapeless);

        final ShapedRecipe shaped = new ShapedRecipe(SHAPED_KEY, new ItemStack(Material.PUMPKIN));
        shaped.shape(" # ", "#$#", " # ");
        shaped.setIngredient('#', new RecipeChoice.PredicateChoice( stack -> stack.hasItemMeta() && stack.getItemMeta().getPersistentDataContainer().has(PDC_KEY), SHAPELESS_INGREDIENT.clone()));
        shaped.setIngredient('$', Material.STICK);
        this.getServer().addRecipe(shaped);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().discoverRecipe(SHAPELESS_KEY);
        event.getPlayer().discoverRecipe(SHAPED_KEY);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        final ItemStack cloned = SHAPELESS_INGREDIENT.clone();
        cloned.setAmount(64);
        event.getPlayer().getInventory().addItem(cloned);
    }
}
