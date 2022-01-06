package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.chatvergehen.spigotapi.util.other.Region;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CR_Bohrer_Mk_2 implements ChallengeRecipe {

    private int radius = 2;

    public int getRadius() {
        return radius;
    }

    @Override
    public ItemStack getResult() {
        return new Item(Material.HOPPER)
                .setName("§eBohrer §7Mk. §62")
                .addLoreLine("§8Platzieren: §7Bohrt ein Loch in den Boden und packt den")
                .addLoreLine("§7Loot in eine Kiste.")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "bohrermk2"), getResult());

        sr.shape(
                "III",
                "IRI",
                "IMI"
        );

        sr.setIngredient('I', Material.IRON_BLOCK);
        sr.setIngredient('R', new RecipeChoice.ExactChoice(new CR_Powersource().getResult()));
        sr.setIngredient('M', new RecipeChoice.ExactChoice(new CR_Bohrer_Mk_1().getResult()));

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
