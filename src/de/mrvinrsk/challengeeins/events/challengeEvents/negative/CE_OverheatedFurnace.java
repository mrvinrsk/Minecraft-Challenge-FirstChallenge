package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CE_OverheatedFurnace implements PercentageChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();

    @Override
    public String getEventName() {
        return "Überhitzte Öfen";
    }

    @Override
    public String getConfigName() {
        return "overheated_furnace";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Es besteht eine Change, dass ein Ofen überhitzt und",
                "dadurch länger zum Braten/Schmelzen braucht."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.FURNACE);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    @Override
    public double getBasePercentage() {
        return 15.;
    }

    private Player getNearestPlayer(Location loc) {
        Player nearest = null;
        double dist = 2000.;

        for (Entity ent : loc.getWorld().getNearbyEntities(loc, 50., 50., 50.)) {
            if (ent instanceof Player) {
                if (ent.getLocation().distance(loc) < dist) {
                    dist = ent.getLocation().distance(loc);
                    nearest = (Player) ent;
                }
            }
        }

        return nearest;
    }

    private List<Block> overheated = new ArrayList<>();

    @EventHandler
    public void smelt(FurnaceStartSmeltEvent e) {
        if (!overheated.contains(e.getBlock())) {
            Player p = getNearestPlayer(e.getBlock().getLocation());

            if (p != null) {
                double percentage = eventManager.getPercentage(p, this);

                if (RandomNumber.rnddouble(1., 100.) <= percentage) {
                    e.setTotalCookTime((e.getTotalCookTime() * 2));

                    Location loc = e.getBlock().getLocation();
                    overheated.add(e.getBlock());
                    gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "Der Ofen §a" + loc.getBlockX() + "§r/§a" + loc.getBlockY() + "§r/§a" + loc.getBlockZ() + " §rist §cüberhitzt §rund arbeitet jetzt langsamer.");
                    eventManager.triggerEvent(p, this, Main.getPlugin());
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (overheated.contains(e.getBlock())) {
            overheated.remove(e.getBlock());
        }
    }

}
