package io.github.leralix.extrade.map.markers;

public enum IconType {

    TRADER("Emerald"),
    TRADER_POTENTIAL("Emerald_Blank");

    final String name;
    final String extension = ".png";

    IconType(String name){
        this.name = name;
    }

    public String getIconName(){
        return name;
    }

    public String getFileName(){
        return name + extension;
    }
}
