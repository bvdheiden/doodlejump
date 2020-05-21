package doodlejump.server.networking.listeners;

import doodlejump.core.networking.Room;

@FunctionalInterface
public interface RoomUpdateListener {
    void onRoomUpdate(Room room);
}
