package de.mrvinrsk.challengeeins.events.challengeEvents.positive;

import de.chatvergehen.spigotapi.util.instances.Item;
import de.mrvinrsk.challengebase.util.ChallengeEvent;
import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeEventType;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CE_QuickMining implements ChallengeEvent {

    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    private int maxHeight = 45;
    private int radius = 5;

    @Override
    public String getEventName() {
        return "Quick Mining";
    }

    @Override
    public String getConfigName() {
        return "quick_mining";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        description.add("Beim Abbauen / Platzieren eines Blocks im Bereich");
        description.add("0-" + maxHeight + " auf der Y-Achse werden alle Änderungen");

        if (radius == 1) {
            description.add("jeweils in den angrenzenden Chunk übernommen.");
        } else {
            description.add("jeweils in die angrenzenden " + radius + " Chunks übernommen.");
        }

        return description;
    }

    @Override
    public ItemStack getIcon() {
        return new Item(Material.NETHERITE_PICKAXE).getItemStack();
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POSITIVE;
    }

    private List<Chunk> getNearbyChunks(Chunk chunk, int radius, boolean includeCurrent) {
        List<Chunk> chunks = new ArrayList<>();
        int invert = radius * -1;

        for (int x = invert; x <= radius; x++) {
            for (int z = invert; z <= radius; z++) {
                Chunk c = chunk.getWorld().getChunkAt(chunk.getX() + x, chunk.getZ() + z);
                if (includeCurrent || !c.equals(chunk)) {
                    chunks.add(c);
                }
            }
        }

        return chunks;
    }

    private int[] getBlockPositionInChunk(Block block) {
        Chunk chunk = block.getChunk();
        World w = chunk.getWorld();

        int cx = chunk.getX() << 4;
        int cz = chunk.getZ() << 4;

        for (int xx = 0; xx < 16; xx++) {
            for (int zz = 0; zz < 16; zz++) {
                for (int yy = 0; yy < w.getMaxHeight() - 1; yy++) {
                    if (chunk.getBlock(xx, yy, zz).equals(block)) {
                        return new int[]{xx, yy, zz};
                    }
                }
            }
        }
        return null;
    }

    private void cloneBlock(Player p, Block b, Material material, int radius) {
        int[] inChunk = getBlockPositionInChunk(b);
        int x = inChunk[0];
        int y = inChunk[1];
        int z = inChunk[2];

        List<Chunk> nearby = getNearbyChunks(b.getChunk(), radius, false);

        for (Chunk chunk : nearby) {
            Block remove = chunk.getBlock(x, y, z);
            remove.setType(material);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Block b = e.getBlockPlaced();

        if (b.getLocation().getBlockY() <= maxHeight) {
            cloneBlock(e.getPlayer(), b, b.getType(), 2);

            eventManager.triggerEvent(e.getPlayer(), this, Main.getPlugin());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block b = e.getBlock();

        if (b.getLocation().getBlockY() <= maxHeight) {
            cloneBlock(e.getPlayer(), b, Material.AIR, 2);

            eventManager.triggerEvent(e.getPlayer(), this, Main.getPlugin());
        }
    }

}
