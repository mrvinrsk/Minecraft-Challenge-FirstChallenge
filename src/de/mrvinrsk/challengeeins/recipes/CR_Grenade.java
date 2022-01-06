package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CR_Grenade implements ChallengeRecipe {

    @Override
    public ItemStack getResult() {
        return new Item(Material.SNOWBALL, 2)
                .setName("§cGranate")
                .addLoreLine("§7Sieht wie TNT aus, funktioniert auch so.")
                .addLoreLine("§8Wird aber geworfen.")
                .enchant(Enchantment.LUCK, 1)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapelessRecipe sr = new ShapelessRecipe(new NamespacedKey(Main.getPlugin(), "grenade"), getResult());

        sr.addIngredient(3, Material.GUNPOWDER);
        sr.addIngredient(1, Material.TRIPWIRE_HOOK);

        return sr;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().isSimilar(getResult())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() != null) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player p = (Player) e.getEntity().getShooter();

                if (p.getInventory().getItemInMainHand().isSimilar(getResult())) {
                    Snowball sb = (Snowball) e.getEntity();

                    sb.setCustomName("grenade");
                }
            }
        }
    }

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        if (centerBlock == null) {
            return new ArrayList<>();
        }

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);
                    }
                }
            }
        }

        return circleBlocks;
    }

    int strength = 6;

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity().getCustomName() != null) {
            if (e.getEntity().getCustomName().equalsIgnoreCase("grenade")) {
                new BukkitRunnable() {

                    Location loc = e.getEntity().getLocation();
                    double r = 0.;

                    @Override
                    public void run() {
                        r += .15;

                        if (r <= (strength / 2)) {
                            loc.getWorld().playSound(loc, Sound.ENTITY_CAT_HISS, 2, 2);
                            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, (int) r * 10, r, r, r, .005);
                        } else {
                            loc.getWorld().playSound(loc, Sound.BLOCK_LAVA_POP, 2, 2);
                            loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, (int) r * 10, r, r, r, .0175);

                            List<Location> locs = generateSphere(loc, strength, false);

                            for(Location l : locs) {
                                l.getBlock().breakNaturally();
                            }

                            new BukkitRunnable() {

                                int perStep = 40;

                                @Override
                                public void run() {
                                    for(int i = 0; i < perStep; i++) {
                                        if(locs.size() >= 1) {
                                            locs.get(0).getBlock().breakNaturally();
                                            locs.remove(0);
                                        }else {
                                            this.cancel();
                                            break;
                                        }
                                    }
                                }
                            }.runTaskTimer(Main.getPlugin(), 0, 1);

                            this.cancel();
                        }
                    }
                }.runTaskTimer(Main.getPlugin(), 5, 2);
            }
        }
    }

}
