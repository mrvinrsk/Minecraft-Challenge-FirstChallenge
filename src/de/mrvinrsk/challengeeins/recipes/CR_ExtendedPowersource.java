package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.*;

public class CR_ExtendedPowersource implements ChallengeRecipe {

    @Override
    public ItemStack getResult() {
        return new Item(Material.LAPIS_BLOCK)
                .setName("§cErweiterte Power Quelle")
                .addLoreLine("§7Ein Block von dem eine Menge Kraft aus geht,")
                .addLoreLine("§7er wird häufig zum Craften verwendet.")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "extended_powersource"), getResult());

        sr.shape(
                "RRR",
                "RPR",
                "RRR"
        );

        sr.setIngredient('R', Material.REDSTONE_BLOCK);
        sr.setIngredient('P', new RecipeChoice.ExactChoice(new CR_Powersource().getResult()));

        return sr;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(e.getItemInHand().isSimilar(getResult())) {
            e.setCancelled(true);
        }
    }

}
