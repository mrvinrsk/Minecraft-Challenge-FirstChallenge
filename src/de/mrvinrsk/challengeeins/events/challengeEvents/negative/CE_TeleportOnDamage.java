package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CE_TeleportOnDamage implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();

    private int radius = 85;
    private double threshold = 7.;
    private double heal = 5.;

    @Override
    public String getEventName() {
        return "Schnell weg hier";
    }

    @Override
    public String getConfigName() {
        return "go_away_fast";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Jedes mal, wenn du Schaden nimmst und weniger als " + (threshold / 2) + " " + ((threshold / 2) == 1 ? "Herz" : "Herzen"),
                "hast, wirst du an eine zufällige Stelle in der Welt,",
                "in einem Radius von" + radius + " " + (radius == 1 ? "Block" : "Blöcken") + " teleportiert,",
                "und um " + (heal / 2) + " " + ((heal / 2) == 1 ? "Herz" : "Herzen") + " geheilt."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.CHAINMAIL_BOOTS);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (!(e.getDamage() >= p.getHealth())) {
                if (!p.isDead()) {
                    if (p.getHealth() < threshold) {
                        p.setVelocity(p.getVelocity().multiply(3).setY(.75));
                        gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "");
                    }
                }
            }
        }
    }

}
