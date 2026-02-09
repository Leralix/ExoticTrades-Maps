package io.github.leralix.extrade.squaremap;

import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.markers.IconType;
import io.github.leralix.interfaces.ExTrader;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;
import org.leralix.lib.position.Vector3D;

import java.util.List;

public class ExtradeDynmapMarkerRegister extends CommonMarkerRegister {

    private final MarkerAPI api;
    private MarkerSet traderMarketSet;
    private MarkerSet traderPotentialMarkerSet;


    public ExtradeDynmapMarkerRegister() {
        Plugin plugin = ExoticTradesDynmap.getPlugin().getServer().getPluginManager().getPlugin("dynmap");
        if(plugin instanceof DynmapAPI dynmapAPI){
            this.api = dynmapAPI.getMarkerAPI();
        }else{
            this.api = null;
        }
    }

    @Override
    protected void setupTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        traderMarketSet = api.createMarkerSet("Traders", name, null, false);
    }

    @Override
    protected void setupPotentialTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        traderPotentialMarkerSet = api.createMarkerSet("Traders - Potential", name, null, false);
    }

    @Override
    public boolean isWorking() {
        return api != null;
    }

    @Override
    public void addTraderMarker(ExTrader trader) {

        Marker marker = traderMarketSet.findMarker(trader.getID());
        if (marker != null) {
            marker.deleteMarker();
        }

        Vector3D location = trader.getCurrentPosition();
        marker = traderMarketSet.createMarker(
                trader.getID(),
                trader.getName(),
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                api.getMarkerIcon(IconType.TRADER.getIconName()),
                true);
        marker.setDescription(generateDescription(trader));
    }

    @Override
    public void addPotentialPositionMarker(ExTrader trader) {

        for(Vector3D potentialPosition : trader.getPotentialPosition()) {

            Marker marker = traderMarketSet.findMarker(trader.getID());
            if (marker != null) {
                marker.deleteMarker();
            }

            Vector3D location = trader.getCurrentPosition();
            marker = traderMarketSet.createMarker(
                    trader.getID() +  "_" + potentialPosition.getX() + "_" + potentialPosition.getY() + "_" + potentialPosition.getZ() + "_" + potentialPosition.getWorld().getName(),
                    trader.getName(),
                    location.getWorld().getName(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    api.getMarkerIcon(IconType.TRADER_POTENTIAL.getIconName()),
                    true);
            marker.setDescription(generateDescription(trader));
        }

    }


    @Override
    public void deleteAllMarkers() {
        for(Marker marker : traderMarketSet.getMarkers()) {
            marker.deleteMarker();
        }
        if(traderPotentialMarkerSet != null) {
            for (Marker marker : traderPotentialMarkerSet.getMarkers()) {
                marker.deleteMarker();
            }
        }
    }

    @Override
    public void registerIcon(IconType iconType) {
        api.createMarkerIcon(iconType.getFileName(), iconType.getFileName(),
                ExoticTradeMapCommon.getPlugin().getResource("icons/" + iconType.getFileName()));

    }
}
