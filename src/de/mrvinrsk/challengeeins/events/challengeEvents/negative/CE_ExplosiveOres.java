package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CE_ExplosiveOres implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();
    private Plugin plugin = Main.getPlugin();

    private double percentage = 15.;

    @Override
    public String getEventName() {
        return "Explosive Erze";
    }

    @Override
    public String getConfigName() {
        return "explosive_ores";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Seltene Erze haben beim Abbauen eine " + percentage + "%",
                "Wahrscheinlichkeit zu explodieren."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new Item(Material.TNT).getItemStack();
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    private List<Material> ores = Arrays.asList(
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
            Material.LAPIS_ORE,
            Material.DEEPSLATE_LAPIS_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.ANCIENT_DEBRIS
    );

    private List<Block> fused = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        if (!fused.contains(b)) {
            if (ores.contains(b.getType())) {
                if (RandomNumber.rnddouble(1., 100.) <= percentage) {
                    e.setCancelled(true);
                    
                    fused.add(b);
                    b.getWorld().playSound(b.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 2, 1);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            b.getWorld().playSound(b.getLocation(), Sound.ENTITY_TNT_PRIMED, 2, 2);

                            new BukkitRunnable() {

                                int run = 30;

                                @Override
                                public void run() {
                                    b.getWorld().spawnParticle(Particle.ASH, LocationHelper.getCenter3D(b.getLocation()), 75, .75, .75, .75, .075);
                                    if(run % 4 == 0) {
                                        b.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, LocationHelper.getCenter3D(b.getLocation()), 5, 1.5, 1.5, 1.5, .125);
                                    }
                                    b.getWorld().playSound(b.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 2, 2);

                                    if(--run == 0) {
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                b.getWorld().createExplosion(b.getLocation(), 6.75F, true, true);
                                            }
                                        }.runTaskLater(plugin, 15);

                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0, 2);
                        }
                    }.runTaskLater(plugin, 5);

                    eventManager.triggerEvent(p, this, Main.getPlugin());
                }
            }
        } else {
            e.setCancelled(true);
            gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "Dieser Block ist geladen, du solltest dich ein wenig davon entfernen...");
        }
    }

}
