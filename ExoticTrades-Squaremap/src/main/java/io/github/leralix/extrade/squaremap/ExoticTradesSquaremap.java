package io.github.leralix.extrade.squaremap;


import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;

public class ExoticTradesSquaremap extends ExoticTradeMapCommon {


    @Override
    protected String getSubMapName() {
        return "Squaremap";
    }

    @Override
    protected int getBStatID() {
        return 29462;
    }

    @Override
    protected CommonMarkerRegister createMarkerRegister() {
        return new ExtradeSquaremapMarkerRegister();
    }
}
