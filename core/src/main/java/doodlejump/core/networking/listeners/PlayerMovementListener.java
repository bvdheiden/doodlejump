package doodlejump.core.networking.listeners;

@FunctionalInterface
public interface PlayerMovementListener {
    void onPlayerMovement(double newX, double newY);
}
