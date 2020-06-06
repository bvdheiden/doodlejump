package doodlejump.client.game;

import doodlejump.client.game.pickups.Pickup;

import java.util.Collections;
import java.util.List;

public class Chunk {
    private final List<Platform> platformList;
    private final List<Pickup> pickupList;
    private final double startY;
    private final double endY;

    public Chunk(List<Platform> platformList, List<Pickup> pickupList, double startY, double endY) {
        this.platformList = platformList;
        this.pickupList = pickupList;
        this.startY = startY;
        this.endY = endY;
    }

    public List<Platform> getPlatformList() {
        return Collections.unmodifiableList(platformList);
    }

    public List<Pickup> getPickupList() {
        return Collections.unmodifiableList(pickupList);
    }

    public double getStartY() {
        return startY;
    }

    public double getEndY() {
        return endY;
    }
}
