package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Player;

@FunctionalInterface
public interface PlayerDisconnectionListener {
    void onPlayerDisconnection(Player player);
}
