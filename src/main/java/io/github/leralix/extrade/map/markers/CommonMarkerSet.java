package io.github.leralix.extrade.map.markers;


import io.github.leralix.interfaces.ExTrader;
import io.github.leralix.extrade.map.ExoticTradeMapCommon;

public abstract class CommonMarkerSet {

    public abstract void deleteAllMarkers();

    public abstract void createTrader(ExTrader trader, String name, String worldName, int x, int y, int z, boolean b);


    protected String generateDescription(ExTrader trader) {

        String res = ExoticTradeMapCommon.getPlugin().getConfig().getString("landmark_infowindow");
        if(res == null)
            return "No description";


        //res = res.replace("%OWNER%", ownerName);

        return res;
    }


}
