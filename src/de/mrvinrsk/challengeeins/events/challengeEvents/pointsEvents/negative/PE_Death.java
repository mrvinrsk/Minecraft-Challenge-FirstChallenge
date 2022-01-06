package de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.negative;

import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PE_Death implements PointEarningEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public int getPoints() {
        return 200;
    }

    @Override
    public PointEventType getPointEventType() {
        return PointEventType.REMOVE;
    }

    @Override
    public String getEventName() {
        return "Teurer Tot";
    }

    @Override
    public String getConfigName() {
        return "expensive_death";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Du verlierst beim Sterben " + getPoints() + " " + (getPoints() == 1 ? "Punkt" : "Punkte") + "."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.SPIDER_EYE);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POINTS;
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        PointManager pm = PointManager.getInstance(e.getEntity().getUniqueId(), Main.getPlugin());

        pm.removePoints(getPoints());
        eventManager.triggerEvent(e.getEntity(), this, Main.getPlugin());
    }

}
