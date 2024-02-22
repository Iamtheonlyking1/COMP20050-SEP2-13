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
            atoms[i] = new Atom(new Texture("C:\\Users\\harmi\\Desktop\\softy\\COMP20050-SEP2-13\\test\\desktop\\src\\Innercore\\atom.png"), 100); // Adjust size and texture as needed
            CIFs[i]= new Circle_of_influence(new Texture("C:\\Users\\harmi\\Desktop\\softy\\COMP20050-SEP2-13\\test\\desktop\\src\\Innercore\\pngwing.com.png"));

        }

//        atom.setPosition(randomHexPosition);


        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // Set the position of the atom to the center position

//        System.out.println(randomHexPosition);

        double[][] coordinates = new double[91][2];
        coordinates[0][0] = 36;
        coordinates[0][1] = 14.75;
        coordinates[1][0] = 36;
        coordinates[1][1] = 18.75;
        coordinates[2][0] = 36;
        coordinates[2][1] = 22.75;
        coordinates[3][0] = 36;
        coordinates[3][1] = 26.75;
        coordinates[4][0] = 36;
        coordinates[4][1] = 30.75;
        coordinates[5][0] = 36;
        coordinates[5][1] = 34.75;
        coordinates[6][0] = 32.75;
        coordinates[6][1] = 12.25;
        coordinates[7][0] = 32.75;
        coordinates[7][1] = 16.25;
        coordinates[8][0] = 32.75;
        coordinates[8][1] = 20.25;
        coordinates[9][0] = 32.75;
        coordinates[9][1] = 24.25;
        coordinates[10][0] = 32.75;
        coordinates[10][1] = 28.25;
        coordinates[11][0] = 32.75;
        coordinates[11][1] = 32.25;
        coordinates[12][0] = 32.75;
        coordinates[12][1] = 36.25;
        coordinates[13][0] = 29.5;
        coordinates[13][1] = 10.25;
        coordinates[14][0] = 29.5;
        coordinates[14][1] = 14.25;
        coordinates[15][0] = 29.5;
        coordinates[15][1] = 18.25;
        coordinates[16][0] = 29.5;
        coordinates[16][1] = 22.25;
        coordinates[17][0] = 29.5;
        coordinates[17][1] = 26.25;
        coordinates[18][0] = 29.5;
        coordinates[18][1] = 30.25;
        coordinates[19][0] = 29.5;
        coordinates[19][1] = 34.25;
        coordinates[20][0] = 29.5;
        coordinates[20][1] = 38.25;
        coordinates[21][0] = 26.5;
        coordinates[21][1] = 8.25;
        coordinates[22][0] = 26.5;
        coordinates[22][1] = 12.25;
        coordinates[23][0] = 26.5;
        coordinates[23][1] = 16.25;
        coordinates[24][0] = 26.5;
        coordinates[24][1] = 20.25;
        coordinates[25][0] = 26.5;
        coordinates[25][1] = 24.25;
        coordinates[26][0] = 26.5;
        coordinates[26][1] = 28.25;
        coordinates[27][0] = 26.5;
        coordinates[27][1] = 32.25;
        coordinates[28][0] = 26.5;
        coordinates[28][1] = 36.25;
        coordinates[29][0] = 26.5;
        coordinates[29][1] = 40.25;
        coordinates[30][0] = 23.25;
        coordinates[30][1] = 6.25;
        coordinates[31][0] = 23.25;
        coordinates[31][1] = 10.25;
        coordinates[32][0] = 23.25;
        coordinates[32][1] = 14.25;
        coordinates[33][0] = 23.25;
        coordinates[33][1] = 18.25;
        coordinates[34][0] = 23.25;
        coordinates[34][1] = 22.25;
        coordinates[35][0] = 23.25;
        coordinates[35][1] = 26.25;
        coordinates[36][0] = 23.25;
        coordinates[36][1] = 30.25;
        coordinates[37][0] = 23.25;
        coordinates[37][1] = 34.25;
        coordinates[38][0] = 23.25;
        coordinates[38][1] = 38.25;
        coordinates[39][0] = 23.25;
        coordinates[39][1] = 42.25;
        coordinates[40][0] = 20.25;
        coordinates[40][1] = 4.25;
        coordinates[41][0] = 20.25;
        coordinates[41][1] = 8.25;
        coordinates[42][0] = 20.25;
        coordinates[42][1] = 12.25;
        coordinates[43][0] = 20.25;
        coordinates[43][1] = 16.25;
        coordinates[44][0] = 20.25;
        coordinates[44][1] = 20.25;
        coordinates[45][0] = 20.25;
        coordinates[45][1] = 24.25;
        coordinates[46][0] = 20.25;
        coordinates[46][1] = 28.25;
        coordinates[47][0] = 20.25;
        coordinates[47][1] = 32.25;
        coordinates[48][0] = 20.25;
        coordinates[48][1] = 36.25;
        coordinates[49][0] = 20.25;
        coordinates[49][1] = 40.25;
        coordinates[50][0] = 20.25;
        coordinates[50][1] = 44.25;
        coordinates[51][0] = 17;
        coordinates[51][1] = 6.25;
        coordinates[52][0] = 17;
        coordinates[52][1] = 10.25;
        coordinates[53][0] = 17;
        coordinates[53][1] = 14.25;
        coordinates[54][0] = 17;
        coordinates[54][1] = 18.25;
        coordinates[55][0] = 17;
        coordinates[55][1] = 22.25;
        coordinates[56][0] = 17;
        coordinates[56][1] = 26.25;
        coordinates[57][0] = 17;
        coordinates[57][1] = 30.25;
        coordinates[58][0] = 17;
        coordinates[58][1] = 34.25;
        coordinates[59][0] = 17;
        coordinates[59][1] = 38.25;
        coordinates[60][0] = 17;
        coordinates[60][1] = 42.25;
        coordinates[61][0] = 13.75;
        coordinates[61][1] = 8.25;
        coordinates[62][0] = 13.75;
        coordinates[62][1] = 12.25;
        coordinates[63][0] = 13.75;
        coordinates[63][1] = 16.25;
        coordinates[64][0] = 13.75;
        coordinates[64][1] = 20.25;
        coordinates[65][0] = 13.75;
        coordinates[65][1] = 24.25;
        coordinates[66][0] = 13.75;
        coordinates[66][1] = 28.25;
        coordinates[67][0] = 13.75;
        coordinates[67][1] = 32.25;
        coordinates[68][0] = 13.75;
        coordinates[68][1] = 36.25;
        coordinates[69][0] = 13.75;
        coordinates[69][1] = 40.25;
        coordinates[70][0] = 10.5;
        coordinates[70][1] = 10.25;
        coordinates[71][0] = 10.5;
        coordinates[71][1] = 14.25;
        coordinates[72][0] = 10.5;
        coordinates[72][1] = 18.25;
        coordinates[73][0] = 10.5;
        coordinates[73][1] = 22.25;
        coordinates[74][0] = 10.5;
        coordinates[74][1] = 26.25;
        coordinates[75][0] = 10.5;
        coordinates[75][1] = 30.25;
        coordinates[76][0] = 10.5;
        coordinates[76][1] = 34.25;
        coordinates[77][0] = 10.5;
        coordinates[77][1] = 38.25;
        coordinates[78][0] = 7.5;
        coordinates[78][1] = 12.25;
        coordinates[79][0] = 7.5;
        coordinates[79][1] = 16.25;
        coordinates[80][0] = 7.5;
        coordinates[80][1] = 20.25;
        coordinates[81][0] = 7.5;
        coordinates[81][1] = 24.25;
        coordinates[82][0] = 7.5;
        coordinates[82][1] = 28.25;
        coordinates[83][0] = 7.5;
        coordinates[83][1] = 32.25;
        coordinates[84][0] = 7.5;
        coordinates[84][1] = 36.25;
        coordinates[85][0] = 4.25;
        coordinates[85][1] = 14.25;
        coordinates[86][0] = 4.25;
        coordinates[86][1] = 18.25;
        coordinates[87][0] = 4.25;
        coordinates[87][1] = 22.25;
        coordinates[88][0] = 4.25;
        coordinates[88][1] = 26.25;
        coordinates[89][0] = 4.25;
        coordinates[89][1] = 30.25;
        coordinates[90][0] = 4.25;
        coordinates[90][1] = 34.25;


        Random rand = new Random();

        for(int i=0;i<6;i++){
            Random_Coordinate[i]=rand.nextInt(91);
            for(int j = 0;j<i;j++) {
                if (Random_Coordinate[i] == Random_Coordinate[j]) {
                    Random_Coordinate[i] = rand.nextInt(91);
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

//        batch.begin();
        // Render the hex grid

        hexGrid.render(batch);

       for(int i=0;i<6;i++){
           atoms[i].render(batch);
           CIFs[i].render(batch);
       }

        // Render the atom


        batch.end();

//        batch.end();

        // Render the Box2D debug renderer
        box2DDebugRenderer.render(world, camera.combined);
    }



}

