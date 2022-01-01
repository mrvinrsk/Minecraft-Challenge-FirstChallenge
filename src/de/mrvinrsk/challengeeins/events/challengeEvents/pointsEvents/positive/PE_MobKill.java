package de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive;

import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengebase.util.PointEarningEvent;
import de.mrvinrsk.challengebase.util.PointEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PE_MobKill implements PointEarningEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public int getPoints() {
        return 2;
    }

    @Override
    public PointEventType getPointEventType() {
        return PointEventType.ADD;
    }

    @Override
    public String getEventName() {
        return "Kampfmeister:in";
    }

    @Override
    public String getConfigName() {
        return "mob_killer";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Beim Besiegen jedes Monsters erhälst",
                "du jeweils " + getPoints() + " " + (getPoints() == 1 ? "Punkt" : "Punkte") + "."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POINTS;
    }

    @EventHandler
    public void kill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Monster) {
            if (e.getEntity().getKiller() != null) {
                eventManager.triggerEvent(e.getEntity().getKiller(), this, Main.getPlugin());
            }
        }
    }

}
