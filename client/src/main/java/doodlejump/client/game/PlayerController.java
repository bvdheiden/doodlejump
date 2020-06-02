package doodlejump.client.game;

import doodlejump.client.game.Collision2D.Colliders.BoxCollider;
import doodlejump.client.game.Collision2D.Colliders.Collider2D;
import doodlejump.client.game.Collision2D.CollisionSystem;
import doodlejump.client.game.Collision2D.Enums.ColliderTag;
import doodlejump.client.game.Collision2D.Vector2;
import doodlejump.client.networking.GameClient;
import doodlejump.core.networking.Player;

public class PlayerController implements Updateable
{
    private Player playerData;
    private final GameClient gameClient;
    private final CollisionSystem collisionSystem;

    private Vector2 pos;
    private Vector2 velocity;
    private double width;
    private double height;

    private double gravityVal = 9.8;
    private double weightVal = 10;
    private double friction = 1;

    private boolean grounded = true;
    private boolean canJump = true;
    private boolean canDoubleJump = false;

    private BoxCollider collider;
    private Drawable playerArt;

    public PlayerController(Vector2 pos, double width, double height)
    {
        this.gameClient = GameClient.INSTANCE;
        this.playerData = new Player("testPlayer");
        this.collisionSystem = CollisionSystem.INSTANCE;

        this.pos = pos;
        this.velocity = new Vector2();
        this.width = width;
        this.height = height;


        //this is all the collision code you need to set it up//
        this.collider = new BoxCollider(pos, width,height);   //
        this.collider.collisionCallback = this::OnCollision;  //
        this.collider.setColliderTag(ColliderTag.PLAYER_UNIT);//
        this.collider.setOwnerObject(this);                   //
        ////////////////////////////////////////////////////////
    }

    private void OnCollision(Collider2D other)
    {
        if(other.getColliderTag() == ColliderTag.PLATFORM)
        {
            if(this.pos.y < other.getPos().y - height/2)
            {
                if(velocity.y < 20)
                {
                    velocity.y = 20;
                }
            }
        }
    }

    @Override
    public void update(double deltaTime)
    {

        velocity.y += gravityVal*deltaTime;
    }
}
