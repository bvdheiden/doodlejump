package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Player;

@FunctionalInterface
public interface PlayerReadyListener {
    void onPlayerReady(Player player);
}
