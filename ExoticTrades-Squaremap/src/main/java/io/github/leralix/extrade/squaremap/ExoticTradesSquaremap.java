package io.github.leralix.extrade.squaremap;


import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import io.github.leralix.extrade.map.markers.IconType;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExoticTradesSquaremap extends ExoticTradeMapCommon {


    @Override
    protected String getSubMapName() {
        return "Squaremap";
    }

    @Override
    protected int getBStatID() {
        return 0;
    }

    @Override
    protected CommonMarkerRegister createMarkerRegister() {
        return new ExtradeSquaremapMarkerRegister();
    }
}
