package de.mrvinrsk.challengeeins.main;

import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengebase.util.ChallengeRecipeManager;
import de.mrvinrsk.challengeeins.events.challengeEvents.negative.*;
import de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.negative.PE_Death;
import de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive.PE_Crafting;
import de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive.PE_MobKill;
import de.mrvinrsk.challengeeins.events.challengeEvents.pointsEvents.positive.PE_Smelting;
import de.mrvinrsk.challengeeins.events.challengeEvents.positive.CE_DIAMONDS;
import de.mrvinrsk.challengeeins.events.challengeEvents.positive.CE_LowerPercentages;
import de.mrvinrsk.challengeeins.recipes.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    private static Plugin plugin;
    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();
    private ChallengeRecipeManager recipeManager = ChallengeRecipeManager.getInstance();

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;


        registerRecipes();

        registerCommands();
        registerListeners();

        registerEvents();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void registerCommands() {
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
    }

    private void registerEvents() {
        // POSITIVE
        eventManager.registerEvent(new CE_LowerPercentages(), this);
        eventManager.registerEvent(new CE_DIAMONDS(), this);

        // NEGATIVE
        eventManager.registerEvent(new CE_DropItems(), this);
        eventManager.registerEvent(new CE_WeakArms(), this);
        eventManager.registerEvent(new CE_ExplosiveOres(), this);
        eventManager.registerEvent(new CE_LargerPortals(), this);
        eventManager.registerEvent(new CE_TeleportOnDamage(), this);
        eventManager.registerEvent(new CE_OverheatedFurnace(), this);

        // POINTS - ADD
        eventManager.registerEvent(new PE_MobKill(), this);
        eventManager.registerEvent(new PE_Crafting(), this);
        eventManager.registerEvent(new PE_Smelting(), this);

        // POINTS - REMOVE
        eventManager.registerEvent(new PE_Death(), this);


        // START SCHED
        new BukkitRunnable() {
            @Override
            public void run() {
                CR_Finder.loop();
            }
        }.runTaskTimer(this, 5, 5);
    }

    private void registerRecipes() {
        recipeManager.registerRecipe(new CR_Powersource(), this);
        recipeManager.registerRecipe(new CR_ExtendedPowersource(), this);
        recipeManager.registerRecipe(new CR_CompactIronBlock(), this);
        recipeManager.registerRecipe(new CR_Bohrer_Mk_1(), this);
        recipeManager.registerRecipe(new CR_Bohrer_Mk_2(), this);
        recipeManager.registerRecipe(new CR_Bohrer_Mk_3(), this);
        recipeManager.registerRecipe(new CR_Grenade(), this);
        recipeManager.registerRecipe(new CR_Finder(), this);
    }

}
