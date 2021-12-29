package de.mrvinrsk.challengeeins.events.challengeEvents.negative;

import de.chatvergehen.spigotapi.util.locations.LocationHelper;
import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CE_LargerPortals implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private Plugin plugin = Main.getPlugin();
    private Gameplay gameplay = Gameplay.getInstance();

    private int x = 5;
    private int y = 8;

    @Override
    public String getEventName() {
        return "Größere Portale";
    }

    @Override
    public String getConfigName() {
        return "larger_portals";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "Nether Portale funktionieren erst ab einer",
                "Größe von " + x + "x" + y + " Blöcken."
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.ENDER_PEARL);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.NEGATIVE;
    }


    private HashMap<Player, Location> portals = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void placeBlock(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.FIRE) {
            portals.put(e.getPlayer(), e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onCreate(PortalCreateEvent e) {
        Player p = null;
        List<Location> bb = new ArrayList<>();
        for (BlockState bs : e.getBlocks()) {
            bb.add(bs.getLocation());
        }
        for (Map.Entry<Player, Location> entry : portals.entrySet()) {
            if (bb.contains(entry.getValue())) {
                p = entry.getKey();
                portals.remove(p);
            }
        }

        if (p != null) {
            int minBlocks = (x * y) - 4;

            if (e.getBlocks().size() < minBlocks) {
                e.setCancelled(true);

                List<BlockState> blocks = e.getBlocks();
                blocks.removeIf(b -> b.getType() != Material.OBSIDIAN); // Remove if not obsidian

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (blocks.size() >= 1) {
                            Block b = blocks.get(0).getBlock();
                            Location loc = LocationHelper.getCenter3D(b.getLocation());

                            b.breakNaturally();
                            b.getWorld().spawnParticle(Particle.ASH, loc, 50, .5, .5, .5, .05);

                            b.getWorld().playSound(loc, Sound.BLOCK_STONE_BREAK, 2, 1);
                            blocks.remove(0);
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 2);

                eventManager.triggerEvent(p, this);

                gameplay.sendMessage(p, GameplayMessageType.SYSTEM, "Das Portal ist zu klein.");
            }
        }
    }

}
