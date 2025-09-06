package com.eclectusstudio.sknbs;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkNBS extends JavaPlugin {

    private static SkriptAddon addon;

    @Override
    public void onEnable() {
        // Plugin startup logic
        boolean NoteBlockAPI = true;
        boolean isSkriptEnabled = true;
        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")){
            getLogger().severe("*** NoteBlockAPI is not installed or not enabled. ***");
            NoteBlockAPI = false;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("Skript")){
            getLogger().severe("*** Skript is not installed or not enabled. ***");
            isSkriptEnabled = false;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        addon = Skript.registerAddon(this);
        try {
            // This will load classes from your elements package
            addon.loadClasses("com.eclectusstudio.sknbs", "elements");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Register Types
        Types.register();

        getLogger().info("SkNBS enabled! Skript <-> NoteBlockAPI bridge active.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
