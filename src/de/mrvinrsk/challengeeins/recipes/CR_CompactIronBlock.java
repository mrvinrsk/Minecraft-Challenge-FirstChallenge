package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CR_CompactIronBlock implements ChallengeRecipe {

    @Override
    public ItemStack getResult() {
        return new Item(Material.IRON_BLOCK, 2)
                .setName("§cKomprimiertes Eisen")
                .enchant(Enchantment.LUCK, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapelessRecipe sr = new ShapelessRecipe(new NamespacedKey(Main.getPlugin(), "compact_iron"), getResult());

        sr.addIngredient(4, Material.IRON_BLOCK);

        return sr;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().isSimilar(getResult())) {
            e.setCancelled(true);
        }
    }

}
