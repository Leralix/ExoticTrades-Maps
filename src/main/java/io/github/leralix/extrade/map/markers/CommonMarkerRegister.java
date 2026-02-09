package io.github.leralix.extrade.map.markers;

import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.interfaces.ExRareItem;
import io.github.leralix.interfaces.ExTrader;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public abstract class CommonMarkerRegister {


    public void setup(){
        FileConfiguration cfg = ExoticTradeMapCommon.getPlugin().getConfig();
        cfg.options().copyDefaults(true); //TODO : check if that is really useful
        ExoticTradeMapCommon.getPlugin().saveConfig();

        String id = "extrade.trader";
        String name = cfg.getString("trader_layer.name", "Exotic Trades - Traders");
        int minZoom = Math.max(cfg.getInt("trader_layer.minimum_zoom", 0),0);
        int chunkLayerPriority =  Math.max(cfg.getInt("trader_layer.priority", 10),0);
        boolean hideByDefault = cfg.getBoolean("trader_layer.hide_by_default", false);
        List<String> worldsName = cfg.getStringList("trader_layer.worlds");
        setupTraderLayer(id, name, minZoom, chunkLayerPriority, hideByDefault, worldsName);

        boolean showTraderPotentialPositions = ExoticTradeMapCommon.getPlugin().getConfig().getBoolean("show_potential_trader_spawn");
        if(!showTraderPotentialPositions)
            return;

        String id2 = "extrade.trader_potential_position";
        String name2 = cfg.getString("potential_trader_spawn_layer.name", "Exotic Trades - Potential trader positions");
        int minZoom2 = Math.max(cfg.getInt("potential_trader_spawn_layer.minimum_zoom", 0),0);
        int chunkLayerPriority2 =  Math.max(cfg.getInt("potential_trader_spawn_layer.priority", 10),0);
        boolean hideByDefault2 = cfg.getBoolean("potential_trader_spawn_layer.hide_by_default", false);
        List<String> worldsName2 = cfg.getStringList("potential_trader_spawn_layer.worlds");
        setupPotentialTraderLayer(id2, name2, minZoom2, chunkLayerPriority2, hideByDefault2, worldsName2);
    }

    protected abstract void setupTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName);
    protected abstract void setupPotentialTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName);

    public abstract boolean isWorking();

    public abstract void addTraderMarker(ExTrader trader);

    public abstract void addPotentialPositionMarker(ExTrader trader);

    public abstract void deleteAllMarkers();

    protected String generateDescription(ExTrader trader) {

        String res = ExoticTradeMapCommon.getPlugin().getConfig().getString("trader_infowindow");

        if(res == null)
            return "No description";

        String traderName = trader.getName();
        if(traderName == null)
            traderName = "Unnamed trader";

        StringBuilder itemList = new StringBuilder();
        for(ExRareItem item : trader.getItemsSold()){
            itemList.append(item.getName()).append(" : ").append(item.getPrice()).append("\n").append("<br>");
        }

        res = res.replace("%TRADER_NAME%", traderName)
                        .replace("%TRADER_ID%", trader.getID())
                        .replace("%NB_HOURS_BEFORE_NEXT_POSITION%", Integer.toString(trader.getNbHoursBeforeNextPosition()))
                        .replace("%ALL_TRADES%", itemList);

        return res;

    }

    public abstract void registerIcon(IconType iconType);
}
