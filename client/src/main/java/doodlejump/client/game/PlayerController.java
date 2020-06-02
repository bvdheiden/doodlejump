package doodlejump.client.game;

import doodlejump.client.game.collision.colliders.BoxCollider;
import doodlejump.client.game.collision.colliders.Collider2D;
import doodlejump.client.game.collision.CollisionSystem;
import doodlejump.client.game.collision.enums.ColliderTag;
import doodlejump.client.game.collision.Vector2;
import doodlejump.client.game.drawing.Rectangle;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class PlayerController implements Updateable
{
    private Player playerData;
    private final GameClient gameClient;
    private final CollisionSystem collisionSystem;

    private Vector2 pos;
    private Vector2 lastPos;
    private Vector2 velocity;
    private double width;
    private double halfWidth;
    private double height;
    private double halfHeight;

    private double gravityVal = 9.8;
    private double weightVal = 80;
    private double friction = 1;

    private boolean grounded = true;
    private boolean canJump = true;
    private boolean canDoubleJump = false;

    private BoxCollider collider;
    private Rectangle rectangle;

    public PlayerController(Vector2 pos, double width, double height)
    {
        this.gameClient = GameClient.INSTANCE;
        this.playerData = new Player("testPlayer");
        this.collisionSystem = CollisionSystem.INSTANCE;

        //movement stuff
        this.pos = pos;
        this.lastPos = new Vector2();
        this.velocity = new Vector2();
        this.width = width;
        this.halfWidth = width*0.5;
        this.height = height;
        this.halfHeight = height*0.5;

        //collision stuff
        this.collider = new BoxCollider(pos, width,height);
        this.collider.collisionCallback = this::OnCollision;
        this.collider.setColliderTag(ColliderTag.PLAYER_UNIT);
        this.collider.setOwnerObject(this);

        this.rectangle = new Rectangle((int)pos.x, (int)pos.y, (int)width,(int)height,0);
        rectangle.getSquare2D().setPosition(new Point2D.Double(pos.x,pos.y));
        rectangle.setRectangleColor(Color.blue);
    }

    private void OnCollision(Collider2D other)
    {
        if(other.getColliderTag() == ColliderTag.PLATFORM)
        {
            if(this.pos.y <other.getPos().y-(50+halfHeight))
            {
                velocity.y = 0;
                this.pos.y = other.getPos().y-(50+(halfHeight));
                grounded = true;
            }
        }
    }

    @Override
    public void update(double deltaTime)
    {
        lastPos = pos;

        this.pos.x += velocity.x*deltaTime;
        this.pos.y += velocity.y*weightVal*deltaTime;
        collider.setPos(pos);

        if(!grounded)
        {
            velocity.y += gravityVal*deltaTime;
        }
        grounded = false;
    }
}
