package doodlejump.client.game.effects;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.CircleCollider;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.sound.SoundPlayer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BombDrop extends Effect
{
    private static final double RADIUS = 75;;
    private static final double GRAVITY = 9.8;
    private static final double WEIGHT = 170;
    private static final double FRICTION = 1000;
    private static final String EXPLOSION_SOUND_PATH = "Explosion39.wav";
    private double x;
    private double y;

    private double counter = 0;
    private double lifeTime = 2;

    private Vector2 velocity;
    private double velocitySpeed;

    private boolean isActivated = false;

    private CircleCollider collider;

    public BombDrop(double x, double y)
    {
        velocity = new Vector2();
        this.x = x;
        this.y = y;
    }

    @Override
    public void activate()
    {
        super.activate();
        this.collider = new CircleCollider(new Vector2(x, y), RADIUS);
        collider.setColliderTag(ColliderTag.BOMB);
        collider.setOwnerObject(this);
        isActivated = true;
        SoundPlayer.play(EXPLOSION_SOUND_PATH);
    }

    @Override
    public void update(double deltaTime)
    {
        counter += deltaTime;
        if(counter >= lifeTime)
        {
            if(!isActivated)
            {
                activate();
            }
            if(counter >= lifeTime+0.2)
            {
                remove();
            }
        }
        velocity.y -= GRAVITY * WEIGHT * deltaTime;
        velocitySpeed = velocity.getMagnitude();
        if (!velocity.isZero()) {
            velocity = velocity.normalize().multiplyByDouble(velocitySpeed - (FRICTION * deltaTime));
        }
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        if(!isActivated)
        {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillOval(x - RADIUS, y - RADIUS, 10 * 2, 10 * 2);
        }
        else
        {
            graphicsContext.setFill(Color.ORANGERED);
            graphicsContext.fillOval(x - RADIUS, y - RADIUS, RADIUS*2, RADIUS*2);
        }
    }

    @Override
    public void remove() {
        collider.onDestroy();
        shouldBeRemoved = true;
    }
}
