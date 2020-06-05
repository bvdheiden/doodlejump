package doodlejump.client.game;

import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.game.drawing.Rectangle;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class PlayerController {
    private final GameClient gameClient;
    private final CollisionSystem collisionSystem;
    private Player playerData;
    private Vector2 pos;
    private Vector2 lastPos;
    private Vector2 velocity;
    private Vector2 addVelocity;
    private double width;
    private double halfWidth;
    private double height;
    private double halfHeight;
    private double maxSpeed = 70;
    private double maxMovSpeed = 30;

    private double gravityVal = -9.8;
    private double weightVal = 80;
    private double friction = 1;

    private boolean grounded = true;
    private boolean canJump = true;
    private boolean canDoubleJump = false;

    private BoxCollider collider;
    private Rectangle rectangle;


    public PlayerController(Player getPlayerData)
    {
        this.playerData = getPlayerData;
        this.gameClient = GameClient.INSTANCE;
        this.collisionSystem = CollisionSystem.INSTANCE;

        //movement stuff
        this.pos = new Vector2(playerData.getX(),playerData.getY());
        this.lastPos = new Vector2();
        this.velocity = new Vector2();
        this.addVelocity = new Vector2();
        this.width = 100;
        this.halfWidth = width * 0.5;
        this.height = 100;
        this.halfHeight = height * 0.5;

        //collision stuff
        this.collider = new BoxCollider(pos, width, height);
        this.collider.collisionCallback = this::OnCollision;
        this.collider.setColliderTag(ColliderTag.PLAYER_UNIT);
        this.collider.setOwnerObject(this);

        this.rectangle = new Rectangle((int) pos.x, (int) pos.y, (int) width, (int) height);
        this.rectangle.setRectangleColor(Color.BLUE);
        //rectangle.setRectangleColor(Color.blue);
    }

    private void OnCollision(Collider2D other) {
        if (other.getColliderTag() == ColliderTag.PLATFORM) {
            if (this.pos.y < other.getPos().y - (10 + halfHeight) && velocity.y > 0) {
                velocity.y = 800;
                this.pos.y = other.getPos().y - (10 + (halfHeight));
                grounded = true;
            }
        }
    }

    public void OnKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.D) {
            if (velocity.x < maxMovSpeed) {
                addVelocity.x += 10;
            }
        } else if (e.getCode() == KeyCode.A) {
            if (velocity.x > maxMovSpeed) {
                addVelocity.x -= 10;
            }
        }
    }


    public void update(double deltaTime) {
        lastPos = pos;

        if (!grounded) {
            addVelocity.y += velocity.y + gravityVal * weightVal * deltaTime;
            friction = 1;
        } else {
            friction = 5;
        }

        velocity.Add(addVelocity);

        if (velocity.GetMagnitude() > maxSpeed) {
            velocity = velocity.Normalize().MultiplyByDouble(maxSpeed);
        }

        this.pos.x += velocity.x * deltaTime;
        this.pos.y += velocity.y * deltaTime;
        collider.setPos(pos);
        rectangle.ChangePos(pos);

        addVelocity.SetToZero();
        grounded = false;
    }

    public void Draw(GraphicsContext graphicsContext) {
        System.out.println("hello");
        rectangle.FilledDraw(graphicsContext);
    }
}
