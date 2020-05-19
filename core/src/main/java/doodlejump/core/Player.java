package doodlejump.core;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private double positionX;
    private double positionY;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
