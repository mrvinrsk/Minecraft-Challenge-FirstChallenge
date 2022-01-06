package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class CE_DropItems implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Plugin plugin = Main.getPlugin();

    @Override
    public String getEventName() {
        return "Sharing Isn't Caring";
    }

    @Override
    public String getConfigName() {
        return "item_sharing";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Du kannst keine Items droppen; du musst wohl oder übel",
                "andere Wege finden Items mit deinen Mitspielern zu teilen..."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new de.chatvergehen.spigotapi.util.instances.Item(Material.DIRT).getItemStack();
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();

        final Item item = e.getItemDrop();
        item.setPickupDelay(99999);
        eventManager.triggerEvent(p, this, Main.getPlugin());

        Bukkit.getScheduler().runTaskLater(plugin, bukkitTask -> launch(item), 20L);
    }

    public void launch(Item item) {
        item.setGravity(false);

        item.setVelocity(item.getVelocity().setX(0).setY(.4).setZ(0));
        item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 2, 2);

        new BukkitRunnable() {

            int runs = 15;

            @Override
            public void run() {
                if (runs > 0) {
                    item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 10, .075, .075, .075, .04);
                    runs--;
                } else {
                    item.remove();

                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2, 1);
                    item.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, item.getLocation(), 50, .4, .4, .4, .075);

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 2, 2);
    }

}