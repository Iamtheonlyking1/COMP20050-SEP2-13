package Innercore;
import Innercore.Atom;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Circle_of_influence {
    private Vector2 position;
    private Texture texture;

    public Circle_of_influence(Texture texture){
       this.texture=texture;
    }

    public void setPosition(Atom atom) {
        this.position = atom.getPosition();
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-100 , position.y-93);
    }

}
