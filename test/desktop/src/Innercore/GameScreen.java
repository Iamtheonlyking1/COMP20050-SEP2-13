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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private HexGrid hexGrid;
//    private Button button;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Atom atom,atom_2,atom_3,atom_4,atom_5,atom_6;
    private Circle_of_influence CIF,CIF_2,CIF_3,CIF_4,CIF_5,CIF_6;
    private Atom[] atoms=new Atom[6];
    private Circle_of_influence[] CIFs=new Circle_of_influence[6];
    private int[] Random_Coordinate = new int[6];
//    private SpriteBatch batch;
    private Texture img;
    private Stage stage;
    private  boolean render_atoms;
    private  double[][] coordinates = new double[61][2];
    private double[][] edges = new double[54][2];
    private ray lineRenderer;
    private TextureAnalyzer textureAnalyzer;
    private Stage stage2;
    private Skin skin;
    private ArrayList<Integer> Dialog_Input= new ArrayList<>();
    private ShapeRenderer shapeRenderer;
    private int[][] straightline_pairs= new int[27][2];
    private boolean deflecting;

    private Vector2 deflectionPoint;

    public GameScreen(OrthographicCamera cam) {
        this.camera = cam;
        this.batch = new SpriteBatch();
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0, 0), false);
        lineRenderer = new ray();



        batch = new SpriteBatch();
//        img = new Texture("button.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create an image button
        Texture buttonTexture = new Texture("button.png");
        TextureRegion buttonRegion = new TextureRegion(buttonTexture);
        Drawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        ImageButton button = new ImageButton(buttonDrawable);

        // Set the position of the button
        button.setPosition(0,0);

        // Add a click listener to the button
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // This function will be executed when the button is clicked
                render_atoms = !render_atoms;
            }


        });

        // Add the button to the stage
        stage.addActor(button);
        //atoms initiation
        atoms[0]=atom;
        atoms[1]=atom_2;
        atoms[2]=atom_3;
        atoms[3]=atom_4;
        atoms[4]=atom_5;
        atoms[5]=atom_6;

//circle of influence initiation
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



        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;



//coordinates for hexagons
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


//coordinates for every side of hexagons
        edges[0][0] = 34.25;
        edges[0][1] = 15.25;
        edges[1][0] = 32.75;
        edges[1][1] = 14.25;
        edges[2][0] = 31;
        edges[2][1] = 13.25;
        edges[3][0] = 29.75;
        edges[3][1] = 12.25;
        edges[4][0] = 27.75;
        edges[4][1] = 11.25;
        edges[5][0] = 26.5;
        edges[5][1] = 10.25;
        edges[6][0] = 24.75;
        edges[6][1] = 9.25;
        edges[7][0] = 23.25;
        edges[7][1] = 8.25;
        edges[8][0] = 21.75;
        edges[8][1] = 7.25;
        edges[9][0] = 20.25;
        edges[9][1] = 6.25;
        edges[10][0] = 18.5;
        edges[10][1] = 7.25;
        edges[11][0] = 17;
        edges[11][1] = 8.25;
        edges[12][0] = 15.25;
        edges[12][1] = 9.25;
        edges[13][0] = 13.75;
        edges[13][1] = 10.25;
        edges[14][0] = 12.25;
        edges[14][1] = 11.25;
        edges[15][0] = 10.5;
        edges[15][1] = 12.25;
        edges[16][0] = 9;
        edges[16][1] = 13.25;
        edges[17][0] = 7.5;
        edges[17][1] = 14.25;
        edges[18][0] = 5.75;
        edges[18][1] = 15.25;
        edges[19][0] = 5.75;
        edges[19][1] = 17.25;
        edges[20][0] = 5.75;
        edges[20][1] = 19.25;
        edges[21][0] = 5.75;
        edges[21][1] = 21.25;
        edges[22][0] = 5.75;
        edges[22][1] = 23.25;
        edges[23][0] = 5.75;
        edges[23][1] = 25.25;
        edges[24][0] = 5.75;
        edges[24][1] = 27.25;
        edges[25][0] = 5.75;
        edges[25][1] = 29.25;
        edges[26][0] = 5.75;
        edges[26][1] = 31.25;
        edges[27][0] = 5.75;
        edges[27][1] = 33.25;
        edges[28][0] = 7.5;
        edges[28][1] = 34.25;
        edges[29][0] = 9;
        edges[29][1] = 35.25;
        edges[30][0] = 10.5;
        edges[30][1] = 36.25;
        edges[31][0] = 12.25;
        edges[31][1] = 37.25;
        edges[32][0] = 13.75;
        edges[32][1] = 38.25;
        edges[33][0] = 15.25;
        edges[33][1] = 39.25;
        edges[34][0] = 17;
        edges[34][1] = 40.25;
        edges[35][0] = 18.5;
        edges[35][1] = 41.25;
        edges[36][0] = 20.25;
        edges[36][1] = 42.25;
        edges[37][0] = 21.75;
        edges[37][1] = 41.25;
        edges[38][0] = 23.25;
        edges[38][1] = 40.25;
        edges[39][0] = 24.75;
        edges[39][1] = 39.25;
        edges[40][0] = 26.5;
        edges[40][1] = 38.25;
        edges[41][0] = 27.75;
        edges[41][1] = 37.25;
        edges[42][0] = 29.5;
        edges[42][1] = 36.25;
        edges[43][0] = 31;
        edges[43][1] = 35.25;
        edges[44][0] = 32.75;
        edges[44][1] = 34.25;
        edges[45][0] = 34.25;
        edges[45][1] = 33.25;
        edges[46][0] = 34.25;
        edges[46][1] = 31.25;
        edges[47][0] = 34.25;
        edges[47][1] = 29.25;
        edges[48][0] = 34.25;
        edges[48][1] = 27.25;
        edges[49][0] = 34.25;
        edges[49][1] = 25.25;
        edges[50][0] = 34.25;
        edges[50][1] = 23.25;
        edges[51][0] = 34.25;
        edges[51][1] = 21.25;
        edges[52][0] = 34.25;
        edges[52][1] = 19.25;
        edges[53][0] = 34.25;
        edges[53][1] = 17.25;


        straightline_pairs[0][0]=1;
        straightline_pairs[0][1]=28;
        straightline_pairs[1][0]=3;
        straightline_pairs[1][1]=26;
        straightline_pairs[2][0]=5;
        straightline_pairs[2][1]=24;
        straightline_pairs[3][0]=7;
        straightline_pairs[3][1]=22;
        straightline_pairs[4][0]=9;
        straightline_pairs[4][1]=20;
        straightline_pairs[5][0]=53;
        straightline_pairs[5][1]=30;
        straightline_pairs[6][0]=51;
        straightline_pairs[6][1]=32;
        straightline_pairs[7][0]=49;
        straightline_pairs[7][1]=34;
        straightline_pairs[8][0]=47;
        straightline_pairs[8][1]=36;
        straightline_pairs[9][0]=54;
        straightline_pairs[9][1]=11;
        straightline_pairs[10][0]=52;
        straightline_pairs[10][1]=13;
        straightline_pairs[11][0]=50;
        straightline_pairs[11][1]=15;
        straightline_pairs[12][0]=48;
        straightline_pairs[12][1]=17;
        straightline_pairs[13][0]=46;
        straightline_pairs[13][1]=19;
        straightline_pairs[14][0]=44;
        straightline_pairs[14][1]=21;
        straightline_pairs[15][0]=42;
        straightline_pairs[15][1]=23;
        straightline_pairs[16][0]=40;
        straightline_pairs[16][1]=25;
        straightline_pairs[17][0]=38;
        straightline_pairs[17][1]=27;
        straightline_pairs[18][0]=2;
        straightline_pairs[18][1]=45;
        straightline_pairs[19][0]=4;
        straightline_pairs[19][1]=43;
        straightline_pairs[20][0]=6;
        straightline_pairs[20][1]=41;
        straightline_pairs[21][0]=8;
        straightline_pairs[21][1]=39;
        straightline_pairs[22][0]=10;
        straightline_pairs[22][1]=37;
        straightline_pairs[23][0]=12;
        straightline_pairs[23][1]=35;
        straightline_pairs[24][0]=14;
        straightline_pairs[24][1]=33;
        straightline_pairs[25][0]=16;
        straightline_pairs[25][1]=31;
        straightline_pairs[26][0]=18;
        straightline_pairs[26][1]=29;

        Random rand = new Random();
//randomly placing the atoms and circle of influnces arount the board
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
        //dialog box
        int choice = JOptionPane.YES_OPTION;
        JTextField textField = new JTextField();
        Object[] options = {"Input", "Stop"};
        while(choice != JOptionPane.NO_OPTION) {
        // Display the dialog box with input text field and custom buttons
        choice = JOptionPane.showOptionDialog(
                null,
                textField,
                "Input Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        // Process user's choice

            if (choice == JOptionPane.YES_OPTION) {
                String inputText = textField.getText();
                Dialog_Input.add(Integer.parseInt(inputText));
                Gdx.app.log("Dialog Box", "User clicked Input. Input: " + inputText);
            } else if (choice == JOptionPane.NO_OPTION) {
                Gdx.app.log("Dialog Box", "User clicked Stop.");
            } else {
                Gdx.app.log("Dialog Box", "User closed the dialog without clicking Input or Stop.");
            }


        }
    }

    private void update() {
        world.step(1 / 60f, 6, 2);
//ESC button for exit app
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        this.update();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        hexGrid.render(batch);

        // Render the hex grid
        if (render_atoms) {

            renderGameBoard();

        }

        // Render the atom
        batch.end();

        stage.act();
        stage.draw();

        no_atom_encounter_absorbtion();

            box2DDebugRenderer.render(world, camera.combined);
        }



    private void renderGameBoard() {
        for(int i=0;i<6;i++){
            atoms[i].render(batch);
            CIFs[i].render(batch);
        }
    }
    public static int[] search2DArray(int[][] array, int target) {//searches the array for pairs
        for (int i = 0; i < array.length; i++) {
            if (array[i][1] == target) {
                return new int[]{i, 1};
            } else if (array[i][0] == target) {
                return new int[]{i, 0};
            }
        }
        return null;
    }

    private Vector2 calculateEndPoint(Vector2 startPoint, float angle, float distance) {//calculates end point after 60 degree deflection
        float radAngle = (float) Math.toRadians(angle);
        float x = startPoint.x + distance * (float) Math.cos(radAngle);
        float y = startPoint.y + distance * (float) Math.sin(radAngle);
        return new Vector2(x, y);
    }
    private void angle_60(){
        Vector2 startPoint =  hexGrid.calculateHexagonPosition(23.5,22.25); // Example starting point
        float angle = -60; // Example angle (in degrees)
        Vector2 endPoint = calculateEndPoint(startPoint, angle, 200); // Calculate the endpoint

        // Deflect the line
        float dx = endPoint.x - startPoint.x;
        float dy = endPoint.y - startPoint.y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float deflectAngle = (float) Math.toRadians(180-angle + 120); // Deflect by 60 degrees
        endPoint.set(startPoint.x + length * (float) Math.cos(deflectAngle), startPoint.y + length * (float) Math.sin(deflectAngle));

        // Draw the deflected lin
    }
    private void deflectLine(Vector2 startPoint, Vector2 endPoint, Vector2 deflectionPoint) {
//        float angle = -60;
        float dx = endPoint.x - startPoint.x;
        float dy = endPoint.y - startPoint.y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.atan2(dy, dx) + (float) Math.toRadians(60); // Deflect by 60 degrees
        endPoint.set(deflectionPoint.x + length * (float) Math.cos(angle), deflectionPoint.y + length * (float) Math.sin(angle));

        shapeRenderer.rectLine(startPoint, endPoint, 5);
        shapeRenderer.end();
    }

    private void no_atom_encounter_absorbtion() {
        deflectionPoint = hexGrid.calculateHexagonPosition(7.5, 16.25);
        if (render_atoms) {
            for (int i = 0; i < Dialog_Input.size(); i++) {
                int[] index = search2DArray(straightline_pairs, Dialog_Input.get(i));
                shapeRenderer = new ShapeRenderer();
                shapeRenderer.setAutoShapeType(true);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.setColor(1, 0, 0, 1); // Red line
                shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                if (index[1] == 1) {
                    if (!deflecting) {
                        hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]).x += 2; // Move the endpoint of the line horizontally
                        if (hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]).x >= deflectionPoint.x) {
                            deflecting = true;
//                            deflectLine(hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0]-1][0], edges[straightline_pairs[index[0]][0]-1][1]),hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1]-1][0], edges[straightline_pairs[index[0]][1]-1][1]));
                        }
                    } else {
                        Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                        Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                        // Deflect the line
                        angle_60();
                    }


                } else {

//                shapeRenderer.rectLine(hexGrid.calculateHexagonPosition(edges[0][0], edges[0][1]), hexGrid.calculateHexagonPosition(edges[27][0], edges[27][1]), 5);
//                shapeRenderer.end();
                    if (!deflecting) {
                        hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]).x += 2; // Move the endpoint of the line horizontally
                        if (hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]).x >= deflectionPoint.x) {
                            deflecting = true;
//                            deflectLine(hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0]-1][0], edges[straightline_pairs[index[0]][0]-1][1]),hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1]-1][0], edges[straightline_pairs[index[0]][1]-1][1]));
                        }
                    } else {
                        Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                        Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                        // Deflect the line
                        angle_60();
                    }

                }

            }
        }
    }



}

