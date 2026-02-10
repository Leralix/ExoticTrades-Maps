package io.github.leralix.extrade.bluemap;


import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;

public class ExoticTradesBluemap extends ExoticTradeMapCommon {


    @Override
    protected String getSubMapName() {
        return "Bluemap";
    }

    @Override
    protected int getBStatID() {
        return 29449;
    }

    @Override
    protected CommonMarkerRegister createMarkerRegister() {
        return new ExtradeBluemapMarkerRegister();
    }
}
