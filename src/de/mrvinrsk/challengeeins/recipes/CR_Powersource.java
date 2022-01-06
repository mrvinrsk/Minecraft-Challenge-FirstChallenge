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
import org.bukkit.inventory.ShapedRecipe;

public class CR_Powersource implements ChallengeRecipe {

    @Override
    public ItemStack getResult() {
        return new Item(Material.REDSTONE_BLOCK)
                .setName("§cPower Quelle")
                .addLoreLine("§7Ein Block von dem eine Menge Kraft aus geht,")
                .addLoreLine("§7er wird häufig zum Craften verwendet.")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "powersource"), getResult());

        sr.shape(
                "RRR",
                "RBR",
                "RRR"
        );

        sr.setIngredient('R', Material.REDSTONE);
        sr.setIngredient('B', Material.REDSTONE_BLOCK);

        return sr;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().isSimilar(getResult())) {
            e.setCancelled(true);
        }
    }

}
