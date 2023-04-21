package de.patboyhd.boarderteleporter;

import de.patboyhd.boarderteleporter.listeners.MovementListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public FileManager config;
    private String config_name = "config.yml";



    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().fine("Plugin activated.");

        this.config = new FileManager(this, config_name);


        setupConfig();

        listenerRegistration();
        commandRegistration();



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().fine("Plugin deactivated.");
    }

    private void commandRegistration() {


    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new MovementListener(this), this);


    }

    private void setupConfig() {



    }

}
