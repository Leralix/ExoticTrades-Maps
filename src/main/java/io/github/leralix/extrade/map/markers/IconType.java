package io.github.leralix.extrade.map.markers;

public enum IconType {

    TRADER("Trader.png"),
    TRADER_POTENTIAL("TraderPotential.png");

    final String fileName;

    IconType(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
