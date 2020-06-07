package doodlejump.client.game;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.CircleCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.game.drawing.Rectangle;
import doodlejump.core.networking.Player;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class PlayerController {
    private static final double MAX_MOVE_SPEED = 600;
    private static final double ACCELERATION = 150;
    private static final double MAX_SPEED = 2000;
    private static final double JUMP_POWER = 1400;
    private static final double GRAVITY = 9.8;
    private static final double WEIGHT = 170;
    private static final double FRICTION = 1000;
    private static final double WIND_DURATION = 3;
    private static final double WINDOW_PADDING = 10;
    private static final double WIND_DEBUF_X_AXIS = ACCELERATION/10;
    private static final double WIND_DEBUF_Y_AXIS = JUMP_POWER/100;

    private final Player playerData;
    private final Vector2 pos;
    private final Vector2 lastPos;
    private final BoxCollider collider;
    private final BoxCollider groundCollider;
    private final Rectangle rectangle;
    //for testing
    Collider2D bombCol;
    boolean bombPressed = false;
    private Vector2 velocity;
    private double velocitySpeed;
    private boolean grounded = true;
    private boolean isCollidingWithGround = false;
    private double windCounter = 0;

    public PlayerController(Player getPlayerData) {
        this.playerData = getPlayerData;

        //movement stuff
        this.pos = new Vector2(playerData.getX() + 60, playerData.getY() + GameView.WINDOW_HEIGHT / 2.0 - Player.HEIGHT);
        this.lastPos = new Vector2();
        this.velocity = new Vector2();

        //collision stuff
        this.collider = new BoxCollider(new Vector2(pos), Player.WIDTH, Player.HEIGHT);
        this.collider.collisionCallback = this::onCollision;
        this.collider.preCollisionCallback = this::preCollision;//
        this.collider.setColliderTag(ColliderTag.PLAYER_UNIT);
        this.collider.setOwnerObject(this);

        this.groundCollider = new BoxCollider(new Vector2(0, -100), 40000, 100);
        this.groundCollider.setColliderTag(ColliderTag.GROUND);

        this.rectangle = new Rectangle((int) (pos.x - Player.HALF_WIDTH), (int) (pos.y - Player.HALF_HEIGHT), (int) Player.WIDTH, (int) Player.HEIGHT);
        this.rectangle.setRectangleColor(Color.rgb(251, 207, 207));
        //rectangle.setRectangleColor(Color.blue);
    }

    private void preCollision() {
        isCollidingWithGround = false;
    }

    private void onCollision(Collider2D other) {
        if (other.getColliderTag() == ColliderTag.PLATFORM) {
            isCollidingWithGround = true;
            if (this.pos.y - Player.HALF_HEIGHT < other.getPos().y + 10 && this.lastPos.y - Player.HALF_HEIGHT > other.getPos().y + 10) { // @todo remove magic number
                this.pos.y = other.getPos().y + Player.HALF_HEIGHT + 10; // @todo remove magic number
                velocity.y = 0;
                velocity.y += JUMP_POWER;
                grounded = true;
            }
        } else if (other.getColliderTag() == ColliderTag.GROUND) {
            isCollidingWithGround = true;
            this.pos.y = other.getPos().y + Player.HALF_HEIGHT + 50; // @todo remove magic number
            velocity.y = 0;
            velocity.y += JUMP_POWER;
            grounded = true;
        } else if (other.getColliderTag() == ColliderTag.JUMP_PLATFORM) {
            isCollidingWithGround = true;
            if (this.pos.y - Player.HALF_HEIGHT < other.getPos().y + 10 && this.lastPos.y - Player.HALF_HEIGHT > other.getPos().y + 10) { // @todo remove magic number
                this.pos.y = other.getPos().y + Player.HALF_HEIGHT + 10; // @todo remove magic number
                velocity.y = 0;
                velocity.y += JUMP_POWER * 15; // @todo remove magic number
                grounded = true;
            }
        } else if (other.getColliderTag() == ColliderTag.BOMB) {
            Vector2 blastDirection = this.pos.subtract(other.getPos());
            blastDirection.normalizeThis();
            blastDirection.multiplyThisByDouble(JUMP_POWER);
            velocity.addToThis(blastDirection);
        }
    }

    //
    public void onKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT)
        {
            if (velocity.x < MAX_MOVE_SPEED) {
                if (velocity.x < 0) {
                    velocity.x += ACCELERATION * 2;
                } else {
                    velocity.x += ACCELERATION;
                }
            }
        }
        else if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT)
        {
            if (velocity.x > -MAX_MOVE_SPEED) {
                if (velocity.x > 0) {
                    velocity.x -= ACCELERATION * 2;
                } else {
                    velocity.x -= ACCELERATION;
                }
            }
        }
        else if (e.getCode() == KeyCode.SPACE)
        {
            playerData.setCurrentlyBlownByWind(true);
            if (grounded) {
                //velocity.y += jumpPower;
            }
        }
        else if (e.getCode() == KeyCode.B)
        {
            if (!bombPressed) {
                bombCol = new CircleCollider(new Vector2(pos.x, pos.y + 550), 100); // @todo remove magic number
                bombCol.setColliderTag(ColliderTag.BOMB);
                bombPressed = true;
            } else {
                bombCol.onDestroy();
                bombPressed = false;
            }
        }
    }

    public void update(double deltaTime) {

        lastPos.x = pos.x;
        lastPos.y = pos.y;

        if (playerData.isCurrentlyBlownByWind()) {
            velocity.x -= WIND_DEBUF_X_AXIS;
            velocity.y -= WIND_DEBUF_Y_AXIS;
            windCounter += deltaTime;
            if (windCounter > WIND_DURATION) {
                playerData.setCurrentlyBlownByWind(false);
                windCounter = 0;
            }
        }

        if (!grounded) {
            velocity.y -= GRAVITY * WEIGHT * deltaTime;
        }

        velocitySpeed = velocity.getMagnitude();

        if (velocitySpeed > MAX_SPEED) {
            velocitySpeed = MAX_SPEED;
        }

        this.pos.x += velocity.x * deltaTime;
        this.pos.y += velocity.y * deltaTime;

        if (pos.x < -WINDOW_PADDING) {
            pos.x = GameView.WINDOW_WIDTH + WINDOW_PADDING;
        } else if (pos.x > GameView.WINDOW_WIDTH + WINDOW_PADDING) {
            pos.x = -WINDOW_PADDING;
        }
        collider.setPos(new Vector2(pos));
        rectangle.changePos((int) (pos.x - Player.HALF_HEIGHT), (int) (pos.y - Player.HALF_HEIGHT));
        playerData.setPosition(pos.x, pos.y);

        if (!velocity.isZero()) {
            velocity = velocity.normalize().multiplyByDouble(velocitySpeed - (FRICTION * deltaTime));
        }
        if (!isCollidingWithGround) {
            grounded = false;
        }
    }
}
