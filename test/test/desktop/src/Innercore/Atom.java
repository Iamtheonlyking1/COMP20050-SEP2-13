package Innercore;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Atom {
    private Vector2 position;
    private Texture texture;
    private float size;

    public Atom(Texture texture, float size) {
        this.texture = texture;
        this.size = size;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSize() {
        return size;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - size / 2, position.y - size / 2, size, size);
    }
}
