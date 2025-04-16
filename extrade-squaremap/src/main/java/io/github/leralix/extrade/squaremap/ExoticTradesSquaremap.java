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
    protected void registerIcon(IconType iconType) {

        for(IconType type : IconType.values()){
            ExoticTradesSquaremap.getPlugin().saveResource("icons/" + type.getFileName(), true);
        }

        try {
            File file = new File(getPlugin().getDataFolder(), "icons/" + iconType.getFileName());
            BufferedImage image = ImageIO.read(file);
            if(SquaremapProvider.get().iconRegistry().hasEntry(Key.of(iconType.getFileName())))
                SquaremapProvider.get().iconRegistry().unregister(Key.of(iconType.getFileName()));
            SquaremapProvider.get().iconRegistry().register(Key.of(iconType.getFileName()),image);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de landmark.png", e);
        }
    }

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
