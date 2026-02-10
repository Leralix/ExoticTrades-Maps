package io.github.leralix.extrade.dynmap;


import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;

public class ExoticTradesDynmap extends ExoticTradeMapCommon {

    @Override
    protected String getSubMapName() {
        return "Squaremap";
    }

    @Override
    protected int getBStatID() {
        return 29461;
    }

    @Override
    protected CommonMarkerRegister createMarkerRegister() {
        return new ExtradeDynmapMarkerRegister();
    }
}
