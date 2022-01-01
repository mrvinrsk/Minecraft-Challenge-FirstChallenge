package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class CE_HeavyInventory implements ChallengeEvent {

    private static int eachSlot = 12;

    @Override
    public String getEventName() {
        return "Schwere Tasche";
    }

    @Override
    public String getConfigName() {
        return "heavy_inventory";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Pro " + eachSlot + " belegte Slots erhälst du eine stärkere",
                "Stufe vom Langsamkeits Effekt."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ANVIL);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    private static int getFullSlots(Player player) {
        int slots = 0;

        for (ItemStack is : player.getInventory().getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                slots++;
            }
        }

        return slots;
    }

    public static void start() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            int full = getFullSlots(all);
            int strength = (full / eachSlot);

            if (strength >= 1) {
                if (all.hasPotionEffect(PotionEffectType.SLOW)) {
                    all.removePotionEffect(PotionEffectType.SLOW);
                }
                all.addPotionEffect(PotionEffectType.SLOW.createEffect(99999, strength - 1));
            } else {
                if (all.hasPotionEffect(PotionEffectType.SLOW)) {
                    all.removePotionEffect(PotionEffectType.SLOW);
                }
            }
        }
    }

}
