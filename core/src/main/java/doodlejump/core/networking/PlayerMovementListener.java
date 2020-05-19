package doodlejump.core.networking;

@FunctionalInterface
public interface PlayerMovementListener {
    void onPlayerMovement(double newX, double newY);
}
