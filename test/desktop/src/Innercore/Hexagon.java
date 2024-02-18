package Innercore;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Hexagon {
    private Vector2 position;
    private Texture texture;

    // Constructor
    public Hexagon(Vector2 position, Texture texture) {
        this.position = position;
        this.texture = texture;
    }

    // Render method
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

}
