package me.dwaa.plugins;

import me.dwaa.plugins.Listeners.TradingListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class EconomicInflation extends JavaPlugin {

    private int inflationRate;


    private void loadConfig() {
        FileConfiguration config = getConfig();
        inflationRate = config.getInt("inflation_rate");
    }

    public int getInflationRate() {
        return inflationRate;
    }
    Logger logger = getLogger();
    @Override
    public void onEnable() {
        // Plugin startup logic
        logger.info("Economic Inflation has been enabled");
        getServer().getPluginManager().registerEvents(new TradingListener(), this);
        saveDefaultConfig();
        loadConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
