package de.mrvinrsk.challengeeins.events.challengeEvents.positive;

import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CE_DIAMONDS implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public String getEventName() {
        return "Bessere Diamanten";
    }

    @Override
    public String getConfigName() {
        return "better_diamonds";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        description.add("Jedes Diamant-Erz das du abbaust agiert wie eine");
        description.add("Kiste, welche guten Loot beinhält.");
        description.add("");
        description.add("§fMöglicher Loot:");


        for(Map.Entry<Material, Integer> loot : getLootItems().entrySet()) {
            description.add("§7- §a" + loot.getKey().name() + " (bis zu " + loot.getValue() + "x)");
        }

        return description;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.DIAMOND_ORE);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POSITIVE;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (b.getType() == Material.DIAMOND_ORE || b.getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            e.setCancelled(true);

            b.setType(Material.AIR);
            TNTPrimed tnt = b.getWorld().spawn(LocationHelper.getCenter3D(b.getLocation()), TNTPrimed.class);
            tnt.setCustomName("diamond_loot#" + p.getUniqueId().toString());
            tnt.setCustomNameVisible(false);
            tnt.setFuseTicks(1);
            tnt.setYield(0F);
        }
    }

    private HashMap<Material, Integer> getLootItems() {
        HashMap<Material, Integer> loot = new HashMap<>();
        loot.put(Material.DIAMOND, 5);
        loot.put(Material.EMERALD, 10);
        loot.put(Material.NETHERITE_INGOT, 2);
        loot.put(Material.IRON_INGOT, 16);
        loot.put(Material.GOLD_INGOT, 8);
        loot.put(Material.GOLDEN_APPLE, 4);
        return loot;
    }

    @EventHandler
    public void explode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            if (e.getEntity().getCustomName() != null) {
                if (e.getEntity().getCustomName().contains("diamond_loot")) {
                    e.setCancelled(true);

                    eventManager.triggerEvent(Bukkit.getPlayer(UUID.fromString(e.getEntity().getCustomName().split("#")[1])), this, Main.getPlugin());

                    HashMap<Material, Integer> loot = getLootItems();
                    List<Block> blocks = e.blockList();

                    e.getEntity().getWorld().playSound(LocationHelper.getCenter3D(e.getEntity().getLocation()), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2, 1);
                    e.getEntity().getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, e.getEntity().getLocation(), 150, e.getYield() * 2, e.getYield() * 2, e.getYield() * 2, .075);


                    for (Block b : blocks) {
                        if (b.getLocation().getBlockY() >= (e.getEntity().getLocation().getBlockY() + 1)) {
                            b.breakNaturally();
                        }
                    }

                    Location loc = e.getLocation();


                    for (int i = 0; i < RandomNumber.rndinteger(4, 8); i++) {
                        List<Material> materials = new ArrayList<Material>(loot.keySet());
                        Material mat = materials.get(RandomNumber.rndinteger(0, materials.size() - 1));
                        int maxAmount = loot.get(mat);

                        loc.getWorld().dropItem(LocationHelper.getCenter3D(loc), new ItemStack(mat, RandomNumber.rndinteger(1, maxAmount)));
                    }
                }
            }
        }
    }

}
