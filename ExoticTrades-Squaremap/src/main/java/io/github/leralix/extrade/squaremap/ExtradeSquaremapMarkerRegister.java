package io.github.leralix.extrade.squaremap;

import io.github.leralix.interfaces.ExTrader;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.markers.IconType;
import io.github.leralix.extrade.map.storage.ExTradeKey;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.leralix.lib.position.Vector3D;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.util.*;

public class ExtradeSquaremapMarkerRegister extends CommonMarkerRegister {

    private final Squaremap api;
    private final Map<ExTradeKey, SimpleLayerProvider> traderLayerMap;
    private final Map<ExTradeKey, SimpleLayerProvider> traderPotentialPositionLayerMap;


    public ExtradeSquaremapMarkerRegister() {
        this.api = SquaremapProvider.get();
        this.traderLayerMap = new HashMap<>();
        this.traderPotentialPositionLayerMap = new HashMap<>();
    }

    @Override
    protected void setupLandmarkLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        setupLayer(id, name, chunkLayerPriority, hideByDefault, worldsName, traderLayerMap);
    }

    @Override
    protected void setupChunkLayer(String id, String name, int minZoom, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName) {
        setupLayer(id, name, chunkLayerPriority, hideByDefault, worldsName, traderPotentialPositionLayerMap);
    }

    private void setupLayer(String id, String name, int chunkLayerPriority, boolean hideByDefault, List<String> worldsName, Map<ExTradeKey, SimpleLayerProvider> landmarkLayerMap) {
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
        for(World world : worlds) {
            ExTradeKey key = new ExTradeKey(world);
            SimpleLayerProvider layerProvider = SimpleLayerProvider.builder(name).layerPriority(chunkLayerPriority).defaultHidden(hideByDefault).build();
            landmarkLayerMap.put(key,layerProvider);


            Optional<MapWorld> optionalWorld = api.getWorldIfEnabled(BukkitAdapter.worldIdentifier(world));

            if(optionalWorld.isPresent()){
                MapWorld mapWorld = optionalWorld.get();
                mapWorld.layerRegistry().register(Key.of(id), layerProvider);
            }

        }
    }

    @Override
    public boolean isWorking() {
        return api != null;
    }

    @Override
    public void addTraderMarker(ExTrader trader) {

        Point point = Point.of(trader.getCurrentPosition().getX(), trader.getCurrentPosition().getZ());

        MarkerOptions markerOptions = MarkerOptions.builder().
                hoverTooltip(generateDescription(trader)).
                build();

        String imageKey = IconType.TRADER.getFileName();

        Marker marker = Marker.icon(point, Key.of(imageKey),16).markerOptions(markerOptions);

        ExTradeKey key = new ExTradeKey(trader.getCurrentPosition().getWorld());
        traderLayerMap.get(key).addMarker(Key.of(trader.getID()), marker);
    }

    @Override
    public void addPotentialPositionMarker(ExTrader trader) {

        for(Vector3D potentialPosition : trader.getPotentialPosition()) {
            Point point = Point.of(potentialPosition.getX(), potentialPosition.getZ());

            MarkerOptions markerOptions = MarkerOptions.builder().
                    hoverTooltip(generateDescription(trader)).
                    build();

            String imageKey = IconType.TRADER_POTENTIAL.getFileName();

            Marker marker = Marker.icon(point, Key.of(imageKey),16).markerOptions(markerOptions);

            ExTradeKey key = new ExTradeKey(potentialPosition.getWorld());
            Key traderKey = Key.of(trader.getID() + "_" + potentialPosition.getX() + "_" + potentialPosition.getY() + "_" + potentialPosition.getZ() + "_" + potentialPosition.getWorld().getName());
            traderPotentialPositionLayerMap.get(key).addMarker(traderKey, marker);
        }

    }


    @Override
    public void deleteAllMarkers() {

    }
}
