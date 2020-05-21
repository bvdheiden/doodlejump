package doodlejump.server.networking.listeners;

import doodlejump.core.networking.Room;

@FunctionalInterface
public interface RoomCreationListener {
    void onRoomCreated(Room room);
}
