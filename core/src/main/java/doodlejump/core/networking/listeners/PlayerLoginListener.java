package doodlejump.core.networking.listeners;

import doodlejump.core.networking.Player;

public interface PlayerLoginListener {
    void onPlayerLogin(Player player);
    void onPlayerLoginNameInUse();
}
