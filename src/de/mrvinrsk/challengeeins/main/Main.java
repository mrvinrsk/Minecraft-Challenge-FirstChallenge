package de.mrvinrsk.challengeeins.main;

import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengeeins.events.challengeEvents.negative.*;
import de.mrvinrsk.challengeeins.events.challengeEvents.positive.CE_QuickMining;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin plugin;
    private ChallengeEventManager eventManager = ChallengeEventManager.getManager();

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;

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
        eventManager.registerEvent(new CE_QuickMining(), this);

        // NEGATIVE
        eventManager.registerEvent(new CE_DoubleDamage(), this);
        eventManager.registerEvent(new CE_DropItems(), this);
        eventManager.registerEvent(new CE_LaggyChests(), this);
        eventManager.registerEvent(new CE_WeakArms(), this);
        eventManager.registerEvent(new CE_ExplosiveOres(), this);
        eventManager.registerEvent(new CE_LargerPortals(), this);
    }

}
