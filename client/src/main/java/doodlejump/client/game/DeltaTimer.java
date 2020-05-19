package doodlejump.client.game;

/**
 * DeltaTimer is used for timing purposes using a delta time notation.
 * Is is especially created for FPS independent timing purposes.
 */
public class DeltaTimer implements Updateable {
    private double wait;
    private boolean loop;
    private double time;
    private int timeout;

    /**
     * Creates a new single use DeltaTimer object.
     *
     * @param wait the time to wait until a timeout occurred
     */
    public DeltaTimer(double wait) {
        this(wait, false, false);
    }

    /**
     * Creates a new single use DeltaTimer object.
     *
     * @param wait the time to wait until a timeout occurred
     * @param autoStart whether the timer must auto start
     */
    public DeltaTimer(double wait, boolean autoStart) {
        this(wait, autoStart, false);
    }

    /**
     * Creates a new DeltaTimer object.
     *
     * @param wait the time to wait until a timeout occurred
     * @param autoStart whether the timer must auto start
     * @param loop whether this timer must loop
     */
    public DeltaTimer(double wait, boolean autoStart, boolean loop) {
        this.wait = wait;
        this.loop = loop;
        this.time = 0.0;
        this.timeout = autoStart ? 0 : -1;
    }

    public void start() {
        this.timeout = 0;
    }

    public void stop() {
        this.timeout = -1;
    }

    /**
     * Get whether the timer has timed out.
     * Timeouts will stack and will not be reset when this method has been called,
     * instead the timeout will decrease by one.
     *
     * @return timeout occurred
     */
    public boolean timeout() {
        if (this.timeout == -1) {
            // timeout has occurred and is not looped
            return false;
        }

        if (this.timeout - 1 < 0) {
            // timeout has not occurred
            this.timeout = 0;
            return false;
        }

        if (this.loop) {
            // when looped, decrease by one
            this.timeout -= 1;
        } else {
            // else abort the timer
            this.timeout = -1;
        }

        return true;
    }

    /**
     * sets the time until the timeout.
     * @param wait the new timeout time
     */
    public void setWait(double wait) {
        this.wait = wait;
    }


    public double getWait() {
        return this.wait;
    }

    @Override
    public void update(double deltaTime) {
        if (this.timeout != -1) {
            // Only update when timer is not aborted
            this.time += deltaTime;
            if (this.time >= this.wait) {
                // timeout occurred
                this.time -= this.wait;
                this.timeout++;
            }
        }
    }
}
