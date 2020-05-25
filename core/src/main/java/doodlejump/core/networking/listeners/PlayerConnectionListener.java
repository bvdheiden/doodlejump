package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Player;

@FunctionalInterface
public interface PlayerConnectionListener {
    void onPlayerConnection(Player player);
}
