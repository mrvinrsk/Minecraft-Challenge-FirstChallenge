package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CE_WeakArms implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public String getEventName() {
        return "Schwache Arme";
    }

    @Override
    public String getConfigName() {
        return "weak_arms";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Du teilst bei jedem Schlag nur den",
                "halben Schaden aus."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new Item(Material.IRON_SWORD).getItemStack();
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            e.setDamage(e.getFinalDamage() / 2);
        }
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            eventManager.triggerEvent(killer, this, Main.getPlugin());
        }
    }

}
