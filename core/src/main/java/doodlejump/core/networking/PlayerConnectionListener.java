package doodlejump.core.networking;

import doodlejump.core.Player;

@FunctionalInterface
public interface PlayerConnectionListener {
    void onPlayerConnection(Player player);
}
