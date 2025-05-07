package io.github.leralix.extrade.squaremap;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.storage.ExTradeKey;
import io.github.leralix.interfaces.ExTrader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.leralix.lib.position.Vector3D;

import java.util.*;

public class ExtradeBluemapMarkerRegister extends CommonMarkerRegister {

    private BlueMapAPI api;

    private final Map<ExTradeKey, MarkerSet> traderLayerMap;
    private final Map<ExTradeKey, MarkerSet> traderPotentialPositionLayerMap;

    public ExtradeBluemapMarkerRegister() {
        super();
        BlueMapAPI.onEnable(bluemapApi -> this.api = bluemapApi);
        this.traderLayerMap = new HashMap<>();
        this.traderPotentialPositionLayerMap = new HashMap<>();
    }

    @Override
    protected void setupTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        setupLayer(id, name, chunkLayerPriority, hideByDefault, worldsName, traderLayerMap);
    }

    @Override
    protected void setupPotentialTraderLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        setupLayer(id, name, chunkLayerPriority, hideByDefault, worldsName, traderPotentialPositionLayerMap);
    }

    private void setupLayer(String id, String name, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName, Map<ExTradeKey, MarkerSet> layerMap) {
        List<World> worlds = new ArrayList<>();
        if(worldsName.contains("all") || worldsName.isEmpty()) {
            worlds.addAll(Bukkit.getWorlds());
        }
        else {
            for (String worldName : worldsName) {
                World world = Bukkit.getWorld(worldName);
                if (world != null)
                    worlds.add(world);
            }
        }
        for(World bukkitWorld : worlds) {
            MarkerSet markerSet = MarkerSet.builder()
                    .label(name)
                    .sorting(chunkLayerPriority)
                    .defaultHidden(hideByDefault)
                    .build();

            layerMap.put(new ExTradeKey(bukkitWorld), markerSet);

            api.getWorld(bukkitWorld).ifPresent(world -> {
                for (BlueMapMap map : world.getMaps()) {
                    map.getMarkerSets().put(id, markerSet);
                }
            });
        }

    }


    @Override
    public boolean isWorking() {
        return api != null;
    }

    @Override
    public void addTraderMarker(ExTrader trader) {
        Location location = trader.getCurrentPosition().getLocation();
        World world = location.getWorld();
        POIMarker marker = POIMarker.builder()
                .label(trader.getName())
                .detail(generateDescription(trader))
                .position(location.getX(), location.getY(), location.getZ())
                .maxDistance(2000)
                .build();

        this.traderLayerMap.get(new ExTradeKey(world)).getMarkers().put(trader.getID(),marker);
    }

    @Override
    public void addPotentialPositionMarker(ExTrader trader) {

        int i = 1;
        for(Vector3D vector3D : trader.getPotentialPosition()){
            Location location = vector3D.getLocation();
            World world = location.getWorld();
            POIMarker marker = POIMarker.builder()
                    .label(trader.getName())
                    .detail(generateDescription(trader))
                    .position(location.getX(), location.getY(), location.getZ())
                    .maxDistance(2000)
                    .build();

            this.traderPotentialPositionLayerMap.get(new ExTradeKey(world)).getMarkers().put(trader.getID() + "_" + i,marker);
            i++;
        }
    }


    @Override
    public void deleteAllMarkers() {

    }
}
