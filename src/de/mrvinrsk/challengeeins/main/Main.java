package de.mrvinrsk.challengeeins.main;

import de.mrvinrsk.challengebase.util.ChallengeEventManager;
import de.mrvinrsk.challengeeins.events.challengeEvents.CE_DoubleDamage;
import de.mrvinrsk.challengeeins.events.challengeEvents.CE_DropItems;
import de.mrvinrsk.challengeeins.events.challengeEvents.CE_LaggyChests;
import de.mrvinrsk.challengeeins.events.challengeEvents.CE_WeakArms;
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
        eventManager.registerEvent(new CE_DoubleDamage(), this);
        eventManager.registerEvent(new CE_DropItems(), this);
        eventManager.registerEvent(new CE_LaggyChests(), this);
        eventManager.registerEvent(new CE_WeakArms(), this);
    }

}
