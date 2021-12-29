package de.mrvinrsk.challengeeins.events.challengeEvents;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;

public class CE_DoubleDamage implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public String getEventName() {
        return "Doppelter Schaden";
    }

    @Override
    public String getConfigName() {
        return "double_damage";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Sobald Schaden genommen wird, wird dieser verdoppelt.",
                "Egal wann und wodurch."
        );
    }

    @Override
    public ItemStack getIcon() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if(meta != null) {
            meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        potion.setItemMeta(meta);

        return potion;
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setDamage(e.getFinalDamage() * 2);

            eventManager.triggerEvent(((Player) e.getEntity()).getPlayer(), this);
        }
    }

}
