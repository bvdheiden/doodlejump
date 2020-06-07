package doodlejump.client.game;

import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.CircleCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.game.drawing.Rectangle;
import doodlejump.core.networking.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class PlayerController {
    private Player playerData;

    private Vector2 pos;
    private Vector2 lastPos;
    private Vector2 velocity;

    private final double maxMovSpeed = 600;
    private final double accelaration = 150;
    private final double maxSpeed = 2000;
    private double velocitySpeed;
    private final double jumpPower = 1400;

    private double width;
    private double halfWidth;
    private double height;
    private double halfHeight;

    private final double gravityVal = 9.8;
    private final double weightVal = 170;
    private double friction = 1000;

    private boolean grounded = true;
    private boolean isCollidingWithGround = false;
    private final boolean canJump = true;
    private final boolean canDoubleJump = false;
    private double windDuration = 3;
    private double windCounter = 0;

    private BoxCollider collider;
    private BoxCollider ground;
    private Rectangle rectangle;

    public PlayerController(Player getPlayerData) {
        this.playerData = getPlayerData;

        //movement stuff
        this.pos = new Vector2(playerData.getX() + 60, playerData.getY() + 520);
        this.lastPos = new Vector2();
        this.velocity = new Vector2();
        this.width = Player.WIDTH;
        this.halfWidth = width * 0.5;
        this.height = Player.HEIGHT;
        this.halfHeight = height * 0.5;

        //collision stuff
        this.collider = new BoxCollider(new Vector2(pos), width, height);
        this.collider.collisionCallback = this::OnCollision;
        this.collider.preCollisionCallback = this::PreCollision;//
        this.collider.setColliderTag(ColliderTag.PLAYER_UNIT);
        this.collider.setOwnerObject(this);

        this.ground = new BoxCollider(new Vector2(0, -100), 40000, 100);
        this.ground.setColliderTag(ColliderTag.GROUND);

        this.rectangle = new Rectangle((int) (pos.x - halfWidth), (int) (pos.y - halfHeight), (int) width, (int) height);
        this.rectangle.setRectangleColor(Color.rgb(251, 207, 207));
        //rectangle.setRectangleColor(Color.blue);
    }

    private void PreCollision() {
        isCollidingWithGround = false;
    }

    private void OnCollision(Collider2D other) {
        if (other.getColliderTag() == ColliderTag.PLATFORM) {
            isCollidingWithGround = true;
            if (this.pos.y - halfHeight < other.getPos().y + 10 && this.lastPos.y - halfHeight > other.getPos().y + 10) {
                this.pos.y = other.getPos().y + halfHeight + 10;
                velocity.y = 0;
                velocity.y += jumpPower;
                grounded = true;
            }
        } else if (other.getColliderTag() == ColliderTag.GROUND) {
            isCollidingWithGround = true;
            this.pos.y = other.getPos().y + halfHeight + 50;
            velocity.y = 0;
            velocity.y += jumpPower;
            grounded = true;
        } else if (other.getColliderTag() == ColliderTag.JUMP_PLATFORM) {
            isCollidingWithGround = true;
            if (this.pos.y - halfHeight < other.getPos().y + 10 && this.lastPos.y - halfHeight > other.getPos().y + 10) {
                this.pos.y = other.getPos().y + halfHeight + 10;
                velocity.y = 0;
                velocity.y += jumpPower * 15;
                grounded = true;
            }
        } else if (other.getColliderTag() == ColliderTag.BOMB) {
            Vector2 blastDirection = new Vector2();
            blastDirection = this.pos.subtract(other.getPos());
            blastDirection.NormalizeThis();
            blastDirection.MultiplyThisByDouble(jumpPower);
            velocity.AddToThis(blastDirection);
        }
    }

    //for testing
    Collider2D bombCol;
    boolean bombPressed = false;

    //
    public void OnKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
            if (velocity.x < maxMovSpeed) {
                velocity.x += accelaration;
            }
        } else if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
            if (velocity.x > -maxMovSpeed) {
                velocity.x -= accelaration;
            }
        } else if (e.getCode() == KeyCode.SPACE) {
            playerData.setCurrentlyBlownByWind(true);
            if (grounded) {
                //velocity.y += jumpPower;
            }
        } else if (e.getCode() == KeyCode.B) {
            if (!bombPressed) {
                bombCol = new CircleCollider(new Vector2(pos.x, pos.y + 550), 100);
                bombCol.setColliderTag(ColliderTag.BOMB);
                bombPressed = true;
            } else {
                bombCol.OnDestroy();
                bombPressed = false;
            }
        }
    }


    public void update(double deltaTime) {

        lastPos.x = pos.x;
        lastPos.y = pos.y;

        if (playerData.isCurrentlyBlownByWind()) {
            velocity.x -= accelaration / 10;
            velocity.y -= jumpPower / 100;
            windCounter += deltaTime;
            if (windCounter > windDuration) {
                playerData.setCurrentlyBlownByWind(false);
                windCounter = 0;
            }
        }

        if (!grounded) {
            velocity.y -= gravityVal * weightVal * deltaTime;
        }

        velocitySpeed = velocity.GetMagnitude();

        if (velocitySpeed > maxSpeed) {
            velocitySpeed = maxSpeed;
        }

        this.pos.x += velocity.x * deltaTime;
        this.pos.y += velocity.y * deltaTime;

        if (pos.x < -10) {
            pos.x = 410;
        } else if (pos.x > 410) {
            pos.x = -10;
        }
        collider.setPos(new Vector2(pos));
        rectangle.ChangePos((int) (pos.x - halfWidth), (int) (pos.y - halfHeight));
        playerData.setPosition(pos.x, pos.y);

        if (!velocity.IsZero()) {
            velocity = velocity.Normalize().MultiplyByDouble(velocitySpeed - (friction * deltaTime));
        }
        if (!isCollidingWithGround) {
            grounded = false;
        }
    }

    public void Draw(GraphicsContext graphicsContext) {
        //System.out.println("hello");
        rectangle.FilledDraw(graphicsContext);
    }
}
