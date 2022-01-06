package de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive;

import de.mrvinrsk.challengebase.util.*;
import de.mrvinrsk.challengeeins.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PE_Crafting implements PointEarningEvent {

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
        return "Baumeister";
    }

    @Override
    public String getConfigName() {
        return "crafting";
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList(
                "Du erhälst beim Herstellen von",
                "Items* jeweils " + getPoints() + " " + (getPoints() == 1 ? "Punkt" : "Punkte") + ".",
                "(* Einige Items ggf. ausgenommen)"
        );
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(Material.CRAFTING_TABLE);
    }

    @Override
    public ChallengeEventType getType() {
        return ChallengeEventType.POINTS;
    }

    private int getCraftedAmount(CraftItemEvent e) {
        ItemStack craftedItem = e.getInventory().getResult(); //Get result of recipe
        Inventory Inventory = e.getInventory(); //Get crafting inventory
        ClickType clickType = e.getClick();
        int realAmount = craftedItem.getAmount();
        if (clickType.isShiftClick()) {
            int lowerAmount = craftedItem.getMaxStackSize() + 1000; //Set lower at recipe result max stack size + 1000 (or just highter max stacksize of reciped item)
            for (ItemStack actualItem : Inventory.getContents()) //For each item in crafting inventory
            {
                if (!actualItem.getType().isAir() && lowerAmount > actualItem.getAmount() && !actualItem.getType().equals(craftedItem.getType())) //if slot is not air && lowerAmount is highter than this slot amount && it's not the recipe amount
                    lowerAmount = actualItem.getAmount(); //Set new lower amount
            }
            //Calculate the final amount : lowerAmount * craftedItem.getAmount
            realAmount = lowerAmount * craftedItem.getAmount();
        }
        return realAmount;
    }

    private List<Material> noPoints = Arrays.asList(
            Material.ACACIA_BUTTON,
            Material.BIRCH_BUTTON,
            Material.CRIMSON_BUTTON,
            Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON,
            Material.OAK_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON,
            Material.SPRUCE_BUTTON,
            Material.STONE_BUTTON,
            Material.WARPED_BUTTON,
            Material.STICK,
            Material.LEVER,
            Material.COAL_BLOCK,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.LAPIS_BLOCK,
            Material.LAPIS_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.NETHERITE_BLOCK,
            Material.STONE_PRESSURE_PLATE,
            Material.ACACIA_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
            Material.WARPED_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.TORCH,
            Material.REDSTONE_TORCH,
            Material.SOUL_TORCH
    );

    @EventHandler
    public void craft(CraftItemEvent e) {
        if(e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            int crafted = getCraftedAmount(e);


            if(!noPoints.contains(e.getRecipe().getResult().getType())) {
                PointManager pm = PointManager.getInstance(p.getUniqueId(), Main.getPlugin());

                pm.addPoints(getPoints() * crafted);
                eventManager.triggerEvent(p, this, Main.getPlugin());
            }
        }
    }

}
