package io.github.leralix.extrade.map;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.extrade.map.commands.PlayerCommandManager;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.update.UpdateTraders;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.leralix.lib.data.PluginVersion;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class ExoticTradeMapCommon extends JavaPlugin {

    private static ExoticTradeMapCommon plugin;
    private final Logger logger = this.getLogger();
    private CommonMarkerRegister markerRegister;
    private long updatePeriod;
    private final PluginVersion pluginVersion = new PluginVersion(0,1 ,0);
    private UpdateTraders updateTraders;

    private final String subMapName = "[ExTrade - " + getSubMapName() + "] - ";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Plugin startup logic
        plugin = this;

        logger.info(subMapName + "Loading Plugin");


        PluginManager pm = getServer().getPluginManager();
        //Get T&N
        Plugin tanPlugin = pm.getPlugin("ExoticTrades");
        if (tanPlugin == null || !tanPlugin.isEnabled()) {
            logger.severe(subMapName + "Cannot find Exotic trades, check your logs to see if it enabled properly?!");
            setEnabled(false);
            return;
        }

        ExtradeAPI api = ExtradeAPI.getInstance();

        PluginVersion minTanVersion = api.getMinimumSupportingMapPlugin();
        if(pluginVersion.isOlderThan(minTanVersion)){
            logger.log(Level.SEVERE,subMapName + "Extrade is not compatible with this version of tanmap (minimum version: {0})", minTanVersion);
            setEnabled(false);
            return;
        }
        Objects.requireNonNull(getCommand("trademap")).setExecutor(new PlayerCommandManager());

        checkConfigVersion();
        initialise();

        logger.info(subMapName + "Plugin is running");
    }

    private void checkConfigVersion() {
        String configFileName = "config.yml";

        InputStream internalConfigStream = plugin.getResource(configFileName);
        if (internalConfigStream == null) {
            return;
        }
        FileConfiguration internalConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(internalConfigStream));

        int configVersion = plugin.getConfig().getInt("config-version", 0);
        int internalConfigVersion = internalConfig.getInt("config-version", 999);

        if(internalConfigVersion != configVersion){
            plugin.getLogger().info(subMapName + "Updating config from version " + configVersion + " to version version " + internalConfigVersion);
            plugin.saveResource(configFileName, true);
            getConfig().set("config-version", internalConfigVersion);
        }

    }

    private void initialise() {
        markerRegister = createMarkerRegister();

        if(!markerRegister.isWorking()){
            logger.severe(subMapName +  "Cannot find marker API, retrying in 5 seconds");
            new BukkitRunnable() {
                @Override
                public void run() {
                    initialise();
                }
            }.runTaskLater(this, 100);
            return;
        }
        logger.info(subMapName +  "Marker API found");



        int per = getConfig().getInt("update.period", 300);
        if(per < 15) per = 15;
        updatePeriod = per * 20L;

        markerRegister.setup();
        startTasks();
    }

    private void startTasks() {

        updateTraders = new UpdateTraders(markerRegister, updatePeriod);
        Runnable deleteAllRunnable = () -> markerRegister.deleteAllMarkers();

        getServer().getScheduler().scheduleSyncDelayedTask(this, deleteAllRunnable, 40);
        getServer().getScheduler().scheduleSyncDelayedTask(this, updateTraders, 40);
    }

    @Override
    public void onDisable() {

    }

    public static ExoticTradeMapCommon getPlugin(){
        return plugin;
    }

    public void updateDynmap() {
        markerRegister.deleteAllMarkers();
        updateTraders.update();
    }

    protected abstract String getSubMapName();

    protected abstract int getBStatID();

    protected abstract CommonMarkerRegister createMarkerRegister();

}


