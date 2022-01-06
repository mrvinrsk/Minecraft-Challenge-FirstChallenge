package de.mrvinrsk.challengeeins.recipes;

import de.chatvergehen.spigotapi.util.filemanaging.ConfigEditor;
import de.chatvergehen.spigotapi.util.filemanaging.FileBuilder;
import de.chatvergehen.spigotapi.util.instances.Item;
import de.chatvergehen.spigotapi.util.other.Region;
import de.mrvinrsk.challengebase.main.ChallengeBase;
import de.mrvinrsk.challengebase.util.ChallengeRecipe;
import de.mrvinrsk.challengebase.util.Gameplay;
import de.mrvinrsk.challengebase.util.GameplayMessageType;
import de.mrvinrsk.challengeeins.main.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.org.eclipse.sisu.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class CR_Finder implements ChallengeRecipe {

    @Override
    public ItemStack getResult() {
        return new Item(Material.COMPASS)
                .setName("§eExpeditionshelfer")
                .getItemStack();
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(Main.getPlugin(), "finder"), getResult());

        sr.shape(
                "XCX",
                "CPC",
                "XCX"
        );

        sr.setIngredient('X', Material.AIR);
        sr.setIngredient('C', Material.COMPASS);
        sr.setIngredient('P', new RecipeChoice.ExactChoice(new CR_Powersource().getResult()));

        return sr;
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        if(e.getItem().isSimilar(getResult())) {
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true);

                Inventory inv = Bukkit.createInventory(null, InventoryType.DROPPER, "§aWelches Item suchen?");

                for(int i = 0; i < 9; i++) {
                    inv.setItem(i, new Item(Material.WHITE_STAINED_GLASS_PANE).setName("§6").getItemStack());
                }
                inv.setItem(4, new ItemStack(Material.AIR));

                e.getPlayer().openInventory(inv);
            }
        }
    }

    @EventHandler
    public void click(InventoryClickEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("§aWelches Item suchen?")) {
            if(e.getClickedInventory().getType() == InventoryType.DROPPER) {
                if (e.getSlot() != 4) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("§aWelches Item suchen?")) {
            Material mat = null;

            if(e.getInventory().getItem(4) != null && e.getInventory().getItem(4).getType() != Material.AIR) {
                mat = e.getInventory().getItem(4).getType();
            }

            setSearchMaterial((Player) e.getPlayer(), mat);
            Gameplay.getInstance().sendMessage(((Player) e.getPlayer()).getPlayer(), GameplayMessageType.SYSTEM, "Der §e" + getResult().getItemMeta().getDisplayName() + " §rsucht ab jetzt nach §6" + mat.name() + "§r.");
        }
    }

    private FileBuilder getFile(Player player) {
        return  new FileBuilder(ChallengeBase.getUserFolder(Main.getPlugin(), player.getUniqueId()), "finder.yml");
    }

    private void setSearchMaterial(Player player, @Nullable Material material) {
        FileBuilder fb = getFile(player);

        if(!fb.exists()) {
            fb.create();
        }

        ConfigEditor ce = fb.getConfig();
        if(material != null) {
            ce.set("Material", material.name());
        }else {
            ce.set("Material", null);
        }
    }

    private Material getSearchMaterial(Player player) {
        Material mat = null;
        FileBuilder fb = getFile(player);

        if(fb.exists()) {
            ConfigEditor ce = fb.getConfig();

            if(ce.contains("Material")) {
                return (Material) ce.get("Material");
            }
        }

        return mat;
    }

    private static int radius = 50;
    public static void loop() {
        for(Player all : Bukkit.getOnlinePlayers()) {
            if(all.getInventory().containsAtLeast(new CR_Finder().getResult(), 1)) {
                List<Location> blocks = Region.getBlocks(all.getLocation().subtract(radius, radius, radius), all.getLocation().add(radius, radius, radius), Arrays.asList());
            }
        }
    }

    private void drawLine(Location start, Location end, double space, Particle particle, float oX, float oY, float oZ, float speed, int count) {
        World world = start.getWorld();
        Validate.isTrue(end.getWorld().equals(world), "Lines cannot be in different worlds!");
        double distance = start.distance(end);
        Vector p1 = start.toVector();
        Vector p2 = end.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;

        for (; length < distance; p1.add(vector)) {
            p1.toLocation(start.getWorld()).getWorld().spawnParticle(particle, p1.toLocation(start.getWorld()), count, oX, oY, oZ);
            length += space;
        }
    }

}
