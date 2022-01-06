package de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive;

import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PE_Smelting implements PointEarningEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    @Override
    public String getEventName() {
        return "Schmelzmeister";
    }

    @Override
    public String getConfigName() {
        return "smelting";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Du erhälst pro geschmolzenen Item " + getPoints() + " " + (getPoints() == 1 ? "Punkt":"Punkte") + "."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FURNACE);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POINTS;
    }

    @Override
    public int getPoints() {
        return 1;
    }

    @Override
    public PointEventType getPointEventType() {
        return PointEventType.ADD;
    }

    @EventHandler
    public void smelt(FurnaceExtractEvent e) {
        PointManager pm = PointManager.getInstance(e.getPlayer().getUniqueId(), Main.getPlugin());

        pm.addPoints(getPoints() * e.getItemAmount());
        eventManager.triggerEvent(e.getPlayer(), this, Main.getPlugin());
    }

}
