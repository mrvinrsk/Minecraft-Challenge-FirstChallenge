package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class CR_Bohrer_Mk_3 implements ChallengeRecipe {

    private int radius = 4;

    public int getRadius() {
        return radius;
    }

    @Override
    public ItemStack getResult() {
        return new Item(Material.HOPPER)
                .setName("§eBohrer §7Mk. §63")
                .addLoreLine("§8Platzieren: §7Bohrt ein Loch in den Boden und packt den")
                .addLoreLine("§7Loot in eine Kiste.")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LURE, 1)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "bohrermk3"), getResult());

        sr.shape(
                "III",
                "PEP",
                "IMI"
        );

        sr.setIngredient('I', new RecipeChoice.ExactChoice(new CR_CompactIronBlock().getResult()));
        sr.setIngredient('P', new RecipeChoice.ExactChoice(new CR_Powersource().getResult()));
        sr.setIngredient('E', new RecipeChoice.ExactChoice(new CR_ExtendedPowersource().getResult()));
        sr.setIngredient('M', new RecipeChoice.ExactChoice(new CR_Bohrer_Mk_2().getResult()));

        return sr;
    }

    private List<Block> bohrer = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (bohrer.contains(e.getBlock())) {
            e.setCancelled(true);
        }
    }

}
