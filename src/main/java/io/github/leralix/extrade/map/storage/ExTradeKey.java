package io.github.leralix.extrade.map.storage;

import org.bukkit.World;

public class ExTradeKey {
    private final World world;

    public ExTradeKey(World world){
        this.world = world;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ExTradeKey key){
            return key.world.equals(world);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return world.hashCode();
    }

    @Override
    public String toString() {
        return "TanKey{" +
                "world=" + world +
                '}';
    }
}
