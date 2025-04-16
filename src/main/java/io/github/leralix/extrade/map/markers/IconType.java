package io.github.leralix.extrade.map.markers;

public enum IconType {

    TRADER("Emerald.png"),
    TRADER_POTENTIAL("Emerald_Blank.png");

    final String fileName;

    IconType(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
