package io.github.leralix.extrade.map.update;

import io.github.leralix.ExtradeAPI;
import io.github.leralix.interfaces.ExTrader;
import io.github.leralix.extrade.map.ExoticTradeMapCommon;
import io.github.leralix.extrade.map.markers.CommonMarkerRegister;
import org.bukkit.plugin.Plugin;

public class UpdateTraders implements Runnable {

    private final CommonMarkerRegister traderMarkerRegister;
    private final Long updatePeriod;

    public UpdateTraders(CommonMarkerRegister markerRegister, long updatePeriod) {
        this.traderMarkerRegister = markerRegister;
        this.updatePeriod = updatePeriod;
    }

    public UpdateTraders(UpdateTraders copy) {
        this.traderMarkerRegister = copy.traderMarkerRegister;
        this.updatePeriod = copy.updatePeriod;
    }


    @Override
    public void run() {
        update();
    }

    public void update() {

        ExtradeAPI extradeAPI = ExtradeAPI.getInstance();

        boolean showTraderPotentialPositions = ExoticTradeMapCommon.getPlugin().getConfig().getBoolean("show_potential_trader_spawn");

        for(ExTrader trader : extradeAPI.getPlayerManager().getTraders()){
            traderMarkerRegister.addTraderMarker(trader);
            if(showTraderPotentialPositions){
                traderMarkerRegister.addPotentialPositionMarker(trader);
            }
        }


        Plugin plugin = ExoticTradeMapCommon.getPlugin();
        if(updatePeriod > 0)
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new UpdateTraders(this), updatePeriod);

    }
}
