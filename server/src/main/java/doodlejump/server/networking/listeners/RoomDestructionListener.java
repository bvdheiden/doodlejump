package doodlejump.server.networking.listeners;

import doodlejump.core.networking.Room;

@FunctionalInterface
public interface RoomDestructionListener {
    void onRoomDestruction(Room room);
}
