package io.github.leralix.extrade.squaremap;


import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.markers.IconType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ExoticTradesBluemap extends ExoticTradeMapCommon {


    @Override
    protected String getSubMapName() {
        return "Bluemap";
    }

    @Override
    protected int getBStatID() {
        return 0;
    }

    @Override
    protected CommonMarkerRegister createMarkerRegister() {
        return new ExtradeBluemapMarkerRegister();
    }
}
