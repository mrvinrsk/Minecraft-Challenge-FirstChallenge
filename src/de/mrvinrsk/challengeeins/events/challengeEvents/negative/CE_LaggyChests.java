package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.random.RandomNumber;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CE_LaggyChests implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Gameplay gameplay = Gameplay.getInstance();

    @Override
    public String getEventName() {
        return "Knarrende Kisten";
    }

    @Override
    public String getConfigName() {
        return "luck_chest";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Manche Truhen klemmen ab und zu mal..."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new Item(Material.CHEST).getItemStack();
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }

    @EventHandler
    public void openChest(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                    if (RandomNumber.rndinteger(1, 100) <= 40) {
                        e.setCancelled(true);

                        eventManager.triggerEvent(e.getPlayer(), this, Main.getPlugin());

                        gameplay.sendMessage(e.getPlayer(), GameplayMessageType.SYSTEM, "Diese Kiste scheint grade zu klemmen...");
                        e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.BLOCK_WOOD_STEP, 2, 1);
                    }
                }
            }
        }
    }

}
