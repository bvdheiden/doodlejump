package doodlejump.core.networking.listeners;

@FunctionalInterface
public interface GameFinishListener {
    void onFinish(boolean didWin);
}
