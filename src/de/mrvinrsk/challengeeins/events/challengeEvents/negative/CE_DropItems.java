package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
    public List<String> getDescription() {
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
        eventManager.triggerEvent(p, this);

        Bukkit.getScheduler().runTaskLater(plugin, bukkitTask -> launch(item), 20L);
    }

    public void launch(Item item) {
        item.setGravity(false);
        item.setVelocity(item.getVelocity().setX(0).setY(.35).setZ(0));
        item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);

        new BukkitRunnable() {
            int iteration = 0;

            @Override
            public void run() {
                item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 5, .25, .25, .25, .05);

                if (++iteration == 20) {
                    item.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, item.getLocation(), 100, .6D, .6D, .6D, .15);
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 1);
                    item.remove();

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

}
