package Innercore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private HexGrid hexGrid;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Atom atom,atom_2,atom_3,atom_4,atom_5,atom_6;
    private Circle_of_influence CIF,CIF_2,CIF_3,CIF_4,CIF_5,CIF_6;
    private Atom[] atoms=new Atom[6];
    private Circle_of_influence[] CIFs=new Circle_of_influence[6];
    private int[] Random_Coordinate = new int[6];


    public GameScreen(OrthographicCamera cam) {
        this.camera = cam;
        this.batch = new SpriteBatch();
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0, 0), false);

        atoms[0]=atom;
        atoms[1]=atom_2;
        atoms[2]=atom_3;
        atoms[3]=atom_4;
        atoms[4]=atom_5;
        atoms[5]=atom_6;


        CIFs[0]=CIF;
        CIFs[1]=CIF_2;
        CIFs[2]=CIF_3;
        CIFs[3]=CIF_4;
        CIFs[4]=CIF_5;
        CIFs[5]=CIF_6;

        // Initialize the hex grid
        hexGrid = new HexGrid();
        for(int i = 0;i<6;i++) {
            atoms[i] = new Atom(new Texture("atom.png"), 100); // Adjust size and texture as needed
            CIFs[i]= new Circle_of_influence(new Texture("pngwing.com.png"));

        }

//        atom.setPosition(randomHexPosition);


        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // Set the position of the atom to the center position
//        System.out.println(randomHexPosition);

        double[][] coordinates = new double[61][2];

        coordinates[0][0] = 32.75;
        coordinates[0][1] = 16.25;
        coordinates[1][0] = 32.75;
        coordinates[1][1] = 20.25;
        coordinates[2][0] = 32.75;
        coordinates[2][1] = 24.25;
        coordinates[3][0] = 32.75;
        coordinates[3][1] = 28.25;
        coordinates[4][0] = 32.75;
        coordinates[4][1] = 32.25;
        coordinates[5][0] = 29.5;
        coordinates[5][1] = 14.25;
        coordinates[6][0] = 29.5;
        coordinates[6][1] = 18.25;
        coordinates[7][0] = 29.5;
        coordinates[7][1] = 22.25;
        coordinates[8][0] = 29.5;
        coordinates[8][1] = 26.25;
        coordinates[9][0] = 29.5;
        coordinates[9][1] = 30.25;
        coordinates[10][0] = 29.5;
        coordinates[10][1] = 34.25;
        coordinates[11][0] = 26.5;
        coordinates[11][1] = 12.25;
        coordinates[12][0] = 26.5;
        coordinates[12][1] = 16.25;
        coordinates[13][0] = 26.5;
        coordinates[13][1] = 20.25;
        coordinates[14][0] = 26.5;
        coordinates[14][1] = 24.25;
        coordinates[15][0] = 26.5;
        coordinates[15][1] = 28.25;
        coordinates[16][0] = 26.5;
        coordinates[16][1] = 32.25;
        coordinates[17][0] = 26.5;
        coordinates[17][1] = 36.25;
        coordinates[18][0] = 23.25;
        coordinates[18][1] = 10.25;
        coordinates[19][0] = 23.25;
        coordinates[19][1] = 14.25;
        coordinates[20][0] = 23.25;
        coordinates[20][1] = 18.25;
        coordinates[21][0] = 23.25;
        coordinates[21][1] = 22.25;
        coordinates[22][0] = 23.25;
        coordinates[22][1] = 26.25;
        coordinates[23][0] = 23.25;
        coordinates[23][1] = 30.25;
        coordinates[24][0] = 23.25;
        coordinates[24][1] = 34.25;
        coordinates[25][0] = 23.25;
        coordinates[25][1] = 38.25;
        coordinates[26][0] = 20.25;
        coordinates[26][1] = 8.25;
        coordinates[27][0] = 20.25;
        coordinates[27][1] = 12.25;
        coordinates[28][0] = 20.25;
        coordinates[28][1] = 16.25;
        coordinates[29][0] = 20.25;
        coordinates[29][1] = 20.25;
        coordinates[30][0] = 20.25;
        coordinates[30][1] = 24.25;
        coordinates[31][0] = 20.25;
        coordinates[31][1] = 28.25;
        coordinates[32][0] = 20.25;
        coordinates[32][1] = 32.25;
        coordinates[33][0] = 20.25;
        coordinates[33][1] = 36.25;
        coordinates[34][0] = 20.25;
        coordinates[34][1] = 40.25;
        coordinates[35][0] = 17;
        coordinates[35][1] = 10.25;
        coordinates[36][0] = 17;
        coordinates[36][1] = 14.25;
        coordinates[37][0] = 17;
        coordinates[37][1] = 18.25;
        coordinates[38][0] = 17;
        coordinates[38][1] = 22.25;
        coordinates[39][0] = 17;
        coordinates[39][1] = 26.25;
        coordinates[40][0] = 17;
        coordinates[40][1] = 30.25;
        coordinates[41][0] = 17;
        coordinates[41][1] = 34.25;
        coordinates[42][0] = 17;
        coordinates[42][1] = 38.25;
        coordinates[43][0] = 13.75;
        coordinates[43][1] = 12.25;
        coordinates[44][0] = 13.75;
        coordinates[44][1] = 16.25;
        coordinates[45][0] = 13.75;
        coordinates[45][1] = 20.25;
        coordinates[46][0] = 13.75;
        coordinates[46][1] = 24.25;
        coordinates[47][0] = 13.75;
        coordinates[47][1] = 28.25;
        coordinates[48][0] = 13.75;
        coordinates[48][1] = 32.25;
        coordinates[49][0] = 13.75;
        coordinates[49][1] = 36.25;
        coordinates[50][0] = 10.5;
        coordinates[50][1] = 14.25;
        coordinates[51][0] = 10.5;
        coordinates[51][1] = 18.25;
        coordinates[52][0] = 10.5;
        coordinates[52][1] = 22.25;
        coordinates[53][0] = 10.5;
        coordinates[53][1] = 26.25;
        coordinates[54][0] = 10.5;
        coordinates[54][1] = 30.25;
        coordinates[55][0] = 10.5;
        coordinates[55][1] = 34.25;
        coordinates[56][0] = 7.5;
        coordinates[56][1] = 16.25;
        coordinates[57][0] = 7.5;
        coordinates[57][1] = 20.25;
        coordinates[58][0] = 7.5;
        coordinates[58][1] = 24.25;
        coordinates[59][0] = 7.5;
        coordinates[59][1] = 28.25;
        coordinates[60][0] = 7.5;
        coordinates[60][1] = 32.25;

        Random rand = new Random();

        for(int i=0;i<6;i++){
            Random_Coordinate[i]=rand.nextInt(61);
            for(int j = 0;j<i;j++) {
                if (Random_Coordinate[i] == Random_Coordinate[j]) {
                    Random_Coordinate[i] = rand.nextInt(61);
                }
            }
                    Vector2 randomHexPosition = hexGrid.calculateHexagonPosition(coordinates[Random_Coordinate[i]][0], coordinates[Random_Coordinate[i]][1]);
                    atoms[i].setPosition(randomHexPosition);
                    CIFs[i].setPosition(atoms[i]);
            }
        }



    private void update() {
        world.step(1 / 60f, 6, 2);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        this.update();
//        camera.zoom=100.0f;
//        camera.zoom=camera.far;


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Render the hex grid
        hexGrid.render(batch);

       for(int i=0;i<6;i++){
           atoms[i].render(batch);
           CIFs[i].render(batch);
       }

        // Render the atom
        batch.end();

        // Render the Box2D debug renderer
        box2DDebugRenderer.render(world, camera.combined);
    }
}

