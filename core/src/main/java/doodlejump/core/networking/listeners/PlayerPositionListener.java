package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Player;

@FunctionalInterface
public interface PlayerPositionListener {
    void onNewPlayerPosition(Player player);
}
