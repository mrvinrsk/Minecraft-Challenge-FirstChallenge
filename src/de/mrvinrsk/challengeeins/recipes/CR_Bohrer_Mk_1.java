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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CR_Bohrer_Mk_1 implements ChallengeRecipe {

    private int radius = 1;

    @Override
    public ItemStack getResult() {
        return new Item(Material.HOPPER)
                .setName("§eBohrer §7Mk. §61")
                .addLoreLine("§8Platzieren: §7Bohrt ein Loch in den Boden und packt den")
                .addLoreLine("§7Loot in eine Kiste.")
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.LUCK, 1)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "bohrermk1"), getResult());

        sr.shape(
                "III",
                "IRI",
                "IHI"
        );

        sr.setIngredient('I', Material.IRON_INGOT);
        sr.setIngredient('R', Material.REDSTONE_BLOCK);
        sr.setIngredient('H', Material.HOPPER);

        return sr;
    }

    private List<Block> bohrer = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (bohrer.contains(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().isSimilar(getResult()) || e.getItemInHand().isSimilar(new CR_Bohrer_Mk_2().getResult()) || e.getItemInHand().isSimilar(new CR_Bohrer_Mk_3().getResult())) {

            if (e.getItemInHand().isSimilar(getResult())) {
                radius = radius;
            } else if (e.getItemInHand().isSimilar(new CR_Bohrer_Mk_2().getResult())) {
                radius = new CR_Bohrer_Mk_2().getRadius();
            } else {
                radius = new CR_Bohrer_Mk_3().getRadius();
            }

            Block b = e.getBlockPlaced();

            Location c_loc = b.getLocation().add(0, 1, 0);
            c_loc.getWorld().getBlockAt(c_loc).setType(Material.CHEST);

            Location loc1 = b.getLocation().subtract(radius, 1, radius);
            Location loc2 = b.getLocation().add(radius, -1, radius);
            List<Block> reg = Region.getBlocks(loc1, loc2, true);

            for (Block bl : reg) {
                bl.setType(Material.GLASS);
            }

            new BukkitRunnable() {
                int Y = b.getLocation().getBlockY() - 1;
                int height = 3;

                Chest c = (Chest) c_loc.getWorld().getBlockAt(c_loc).getState();

                @Override
                public void run() {
                    for (int i = 1; i <= height; i++) {
                        Location loc1 = b.getLocation().subtract(radius, 0, radius);
                        Location loc2 = b.getLocation().add(radius, 0, radius);
                        if (Y > 0) {
                            loc1.setY(Y);
                            loc2.setY(Y--);

                            List<Block> region = Region.getBlocks(loc1, loc2, false);


                            for (Block block : region) {
                                for (ItemStack stack : block.getDrops()) {
                                    if (c.getInventory().firstEmpty() == -1) {
                                        c.getWorld().getBlockAt(c.getLocation().add(0, 1, 0)).setType(Material.CHEST);
                                        c = (Chest) c.getWorld().getBlockAt(c.getLocation().add(0, 1, 0)).getState();
                                    }

                                    c.getInventory().addItem(stack);
                                }

                                if (block.getType() != Material.GLASS) {
                                    block.setType(Material.AIR);

                                    block.getWorld().spawnParticle(Particle.FLAME, LocationHelper.getCenter3D(block.getLocation().add(0, .5, 0)), 15, .15, .15, .15, .075);
                                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_POP, 2, 2);
                                }
                            }
                        } else {
                            bohrer.remove(b);
                            b.setType(Material.REDSTONE_BLOCK);
                            this.cancel();
                        }
                    }
                }
            }.runTaskTimer(Main.getPlugin(), 0, 7);
        }
    }

}
