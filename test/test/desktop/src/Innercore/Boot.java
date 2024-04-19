package Innercore;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Boot extends Game{

    public static Boot INSTANCE;
    private int width, height;
    private OrthographicCamera orthographicCamera;

    public Boot(){
        INSTANCE=this;
    }
    @Override
    public void create() {
        this.height= Gdx.graphics.getHeight();
        this.width=Gdx.graphics.getWidth();
        this.orthographicCamera=new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false,width,height);
        setScreen(new GameScreen(orthographicCamera));

    }
}
