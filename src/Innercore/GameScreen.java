package Innercore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.VertexArray;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import static helper.Constants.PPM;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private static HexGrid hexGrid;
//    private Button button;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Atom atom,atom_2,atom_3,atom_4,atom_5,atom_6;
    private Circle_of_influence CIF,CIF_2,CIF_3,CIF_4,CIF_5,CIF_6;
    private Atom[] atoms=new Atom[6];
    private Circle[] CIFs=new Circle[6];
    private int[] Random_Coordinate = new int[6];
    private int[] Random_Rows = new int[6];
//    private SpriteBatch batch;
    private Texture img;
    private Stage stage;
    private  boolean render_atoms;
    private  double[][][] coordinates = new double[9][9][2];
    private double[][] edges = new double[54][2];
    private ray lineRenderer;
    private TextureAnalyzer textureAnalyzer;
    private Stage stage2;
    private Skin skin;
    private ArrayList<Integer> Dialog_Input= new ArrayList<>();
    private ShapeRenderer shapeRenderer;
    private int[][] straightline_pairs= new int[27][2];
    private Direction[] pair=new Direction[27];
    static ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
    private boolean deflecting;
    private Vector2[] linePt=new Vector2[2];
    private static final int HEX_SIZE = 55;

    private Vector2 deflectionPoint;
    private Circle circle;
   public Texture arrow;
   public TextureRegion arrowRegion;
   public boolean ShowScore;
   public int score=0;
    private BitmapFont font;
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
        Texture buttonTexture2= new Texture("button2.png");
        Texture buttonTexture3= new Texture("button3.png");
        arrow = new Texture("arrow.png");
        arrowRegion=new TextureRegion(arrow);
        TextureRegion buttonRegion = new TextureRegion(buttonTexture);
        TextureRegion buttonRegion2 = new TextureRegion(buttonTexture2);
        TextureRegion buttonRegion3 = new TextureRegion(buttonTexture3);
        Drawable buttonDrawable = new TextureRegionDrawable(buttonRegion);
        ImageButton button = new ImageButton(buttonDrawable);
        Drawable buttonDrawable2 = new TextureRegionDrawable(buttonRegion2);
        ImageButton button2 = new ImageButton(buttonDrawable2);
        Drawable buttonDrawable3 = new TextureRegionDrawable(buttonRegion3);
        ImageButton button3 = new ImageButton(buttonDrawable3);



        // Set the position of the button
        button.setPosition(800,0);
        button2.setPosition(0,0);
        button3.setPosition(800,870);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // This function will be executed when the button is clicked
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


        });
        // Add a click listener to the button

        button3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // This function will be executed when the button is clicked
                int choice = JOptionPane.YES_OPTION;
                JTextField textField = new JTextField();
                Object[] options = {"Input"};
                for(int i=0;i<6;i++) {
                    // Display the dialog box with input text field and custom buttons
                    choice = JOptionPane.showOptionDialog(
                            null,
                            textField,
                            "Guess for atom:"+i+1,
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    // Process user's choice

                    if (choice == JOptionPane.YES_OPTION) {
                        String inputText = textField.getText();

                        // Split the string at the comma
                        String[] substrings = inputText.split(",");
                        if (substrings.length == 2) {
                            int row = Integer.parseInt(substrings[0]);
                            int col = Integer.parseInt(substrings[1]);
                            for(int j=0;j<6;j++){
                                if(atoms[j].getPosition().x==hexGrid.calculateHexagonPosition(coordinates[row][col][0],coordinates[row][col][1]).x && atoms[j].getPosition().y==hexGrid.calculateHexagonPosition(coordinates[row][col][0],coordinates[row][col][1]).y){
                                    score++;
                                    Gdx.app.log("Dialog Box", "User clicked Input. Input: " + inputText);
                                }
                            }
                        }


                    } else {
                        Gdx.app.log("Dialog Box", "User closed the dialog without clicking Input or Stop.");
                    }


                }
                ShowScore=!ShowScore;

            }



        });


        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // This function will be executed when the button is clicked
                button2.setDisabled(false);
                render_atoms = !render_atoms;
                stage.act();
                stage.draw();
                button.setDisabled(false);
            }


        });



        // Add the button to the stage
        stage.addActor(button);
        stage.addActor(button2);
        stage.addActor(button3);
        //atoms initiation
        atoms[0]=atom;
        atoms[1]=atom_2;
        atoms[2]=atom_3;
        atoms[3]=atom_4;
        atoms[4]=atom_5;
        atoms[5]=atom_6;

//circle of influence initiation
//        CIFs[0]=CIF;
//        CIFs[1]=CIF_2;
//        CIFs[2]=CIF_3;
//        CIFs[3]=CIF_4;
//        CIFs[4]=CIF_5;
//        CIFs[5]=CIF_6;

        // Initialize the hex grid
        hexGrid = new HexGrid();
        for(int i = 0;i<6;i++) {
            atoms[i] = new Atom(new Texture("atom.png"), 100); // Adjust size and texture as needed
//            CIFs[i]= new Circle_of_influence(new Texture("pngwing.com.png"));

        }



        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;



//coordinates for hexagons
        coordinates[0][0][0] = 32.75;
        coordinates[0][0][1] = 16.25;
        coordinates[0][1][0] = 32.75;
        coordinates[0][1][1] = 20.25;
        coordinates[0][2][0] = 32.75;
        coordinates[0][2][1] = 24.25;
        coordinates[0][3][0] = 32.75;
        coordinates[0][3][1] = 28.25;
        coordinates[0][4][0] = 32.75;
        coordinates[0][4][1] = 32.25;
        coordinates[1][0][0] = 29.5;
        coordinates[1][0][1] = 14.25;
        coordinates[1][1][0] = 29.5;
        coordinates[1][1][1] = 18.25;
        coordinates[1][2][0] = 29.5;
        coordinates[1][2][1] = 22.25;
        coordinates[1][3][0] = 29.5;
        coordinates[1][3][1] = 26.25;
        coordinates[1][4][0] = 29.5;
        coordinates[1][4][1] = 30.25;
        coordinates[1][5][0] = 29.5;
        coordinates[1][5][1] = 34.25;
        coordinates[2][0][0] = 26.5;
        coordinates[2][0][1] = 12.25;
        coordinates[2][1][0] = 26.5;
        coordinates[2][1][1] = 16.25;
        coordinates[2][2][0] = 26.5;
        coordinates[2][2][1] = 20.25;
        coordinates[2][3][0] = 26.5;
        coordinates[2][3][1] = 24.25;
        coordinates[2][4][0] = 26.5;
        coordinates[2][4][1] = 28.25;
        coordinates[2][5][0] = 26.5;
        coordinates[2][5][1] = 32.25;
        coordinates[2][6][0] = 26.5;
        coordinates[2][6][1] = 36.25;
        coordinates[3][0][0] = 23.25;
        coordinates[3][0][1] = 10.25;
        coordinates[3][1][0] = 23.25;
        coordinates[3][1][1] = 14.25;
        coordinates[3][2][0] = 23.25;
        coordinates[3][2][1] = 18.25;
        coordinates[3][3][0] = 23.25;
        coordinates[3][3][1] = 22.25;
        coordinates[3][4][0] = 23.25;
        coordinates[3][4][1] = 26.25;
        coordinates[3][5][0] = 23.25;
        coordinates[3][5][1] = 30.25;
        coordinates[3][6][0] = 23.25;
        coordinates[3][6][1] = 34.25;
        coordinates[3][7][0] = 23.25;
        coordinates[3][7][1] = 38.25;
        coordinates[4][0][0] = 20.25;
        coordinates[4][0][1] = 8.25;
        coordinates[4][1][0] = 20.25;
        coordinates[4][1][1] = 12.25;
        coordinates[4][2][0] = 20.25;
        coordinates[4][2][1] = 16.25;
        coordinates[4][3][0] = 20.25;
        coordinates[4][3][1] = 20.25;
        coordinates[4][4][0] = 20.25;
        coordinates[4][4][1] = 24.25;
        coordinates[4][5][0] = 20.25;
        coordinates[4][5][1] = 28.25;
        coordinates[4][6][0] = 20.25;
        coordinates[4][6][1] = 32.25;
        coordinates[4][7][0] = 20.25;
        coordinates[4][7][1] = 36.25;
        coordinates[4][8][0] = 20.25;
        coordinates[4][8][1] = 40.25;
        coordinates[5][0][0] = 17;
        coordinates[5][0][1] = 10.25;
        coordinates[5][1][0] = 17;
        coordinates[5][1][1] = 14.25;
        coordinates[5][2][0] = 17;
        coordinates[5][2][1] = 18.25;
        coordinates[5][3][0] = 17;
        coordinates[5][3][1] = 22.25;
        coordinates[5][4][0] = 17;
        coordinates[5][4][1] = 26.25;
        coordinates[5][5][0] = 17;
        coordinates[5][5][1] = 30.25;
        coordinates[5][6][0] = 17;
        coordinates[5][6][1] = 34.25;
        coordinates[5][7][0] = 17;
        coordinates[5][7][1] = 38.25;
        coordinates[6][0][0] = 13.75;
        coordinates[6][0][1] = 12.25;
        coordinates[6][1][0] = 13.75;
        coordinates[6][1][1] = 16.25;
        coordinates[6][2][0] = 13.75;
        coordinates[6][2][1] = 20.25;
        coordinates[6][3][0] = 13.75;
        coordinates[6][3][1] = 24.25;
        coordinates[6][4][0] = 13.75;
        coordinates[6][4][1] = 28.25;
        coordinates[6][5][0] = 13.75;
        coordinates[6][5][1] = 32.25;
        coordinates[6][6][0] = 13.75;
        coordinates[6][6][1] = 36.25;
        coordinates[7][0][0] = 10.5;
        coordinates[7][0][1] = 14.25;
        coordinates[7][1][0] = 10.5;
        coordinates[7][1][1] = 18.25;
        coordinates[7][2][0] = 10.5;
        coordinates[7][2][1] = 22.25;
        coordinates[7][3][0] = 10.5;
        coordinates[7][3][1] = 26.25;
        coordinates[7][4][0] = 10.5;
        coordinates[7][4][1] = 30.25;
        coordinates[7][5][0] = 10.5;
        coordinates[7][5][1] = 34.25;
        coordinates[8][0][0] = 7.5;
        coordinates[8][0][1] = 16.25;
        coordinates[8][1][0] = 7.5;
        coordinates[8][1][1] = 20.25;
        coordinates[8][2][0] = 7.5;
        coordinates[8][2][1] = 24.25;
        coordinates[8][3][0] = 7.5;
        coordinates[8][3][1] = 28.25;
        coordinates[8][4][0] = 7.5;
        coordinates[8][4][1] = 32.25;


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


        pair[0]=Direction.SOUTHEAST;
        pair[1]=Direction.SOUTHEAST;
        pair[2]=Direction.SOUTHEAST;
        pair[3]=Direction.SOUTHEAST;
        pair[4]=Direction.SOUTHEAST;
        pair[5]=Direction.SOUTHEAST;
        pair[6]=Direction.SOUTHEAST;
        pair[7]=Direction.SOUTHEAST;
        pair[8]=Direction.SOUTHEAST;
        pair[9]=Direction.SOUTHWEST;
        pair[10]=Direction.SOUTHWEST;
        pair[11]=Direction.SOUTHWEST;
        pair[12]=Direction.SOUTHWEST;
        pair[13]=Direction.SOUTHWEST;
        pair[14]=Direction.SOUTHWEST;
        pair[15]=Direction.SOUTHWEST;
        pair[16]=Direction.SOUTHWEST;
        pair[17]=Direction.SOUTHWEST;
        pair[18]=Direction.EAST;
        pair[19]=Direction.EAST;
        pair[20]=Direction.EAST;
        pair[21]=Direction.EAST;
        pair[22]=Direction.EAST;
        pair[23]=Direction.EAST;
        pair[24]=Direction.EAST;
        pair[25]=Direction.EAST;
        pair[26]=Direction.EAST;


        // Add rows to the 2D ArrayList
        for (int i = 0; i < 9; i++) {
            rows.add(new ArrayList<>());

        }

        rows.get(0).add(0);
        rows.get(0).add(1);
        rows.get(0).add(2);
        rows.get(0).add(3);
        rows.get(0).add(4);
        rows.get(1).add(0);
        rows.get(1).add(1);
        rows.get(1).add(2);
        rows.get(1).add(3);
        rows.get(1).add(4);
        rows.get(1).add(5);
        rows.get(2).add(0);
        rows.get(2).add(1);
        rows.get(2).add(2);
        rows.get(2).add(3);
        rows.get(2).add(4);
        rows.get(2).add(5);
        rows.get(2).add(6);
        rows.get(3).add(0);
        rows.get(3).add(1);
        rows.get(3).add(2);
        rows.get(3).add(3);
        rows.get(3).add(4);
        rows.get(3).add(5);
        rows.get(3).add(6);
        rows.get(3).add(7);
        rows.get(4).add(0);
        rows.get(4).add(1);
        rows.get(4).add(2);
        rows.get(4).add(3);
        rows.get(4).add(4);
        rows.get(4).add(5);
        rows.get(4).add(6);
        rows.get(4).add(7);
        rows.get(4).add(8);
        rows.get(5).add(0);
        rows.get(5).add(1);
        rows.get(5).add(2);
        rows.get(5).add(3);
        rows.get(5).add(4);
        rows.get(5).add(5);
        rows.get(5).add(6);
        rows.get(5).add(7);
        rows.get(6).add(0);
        rows.get(6).add(1);
        rows.get(6).add(2);
        rows.get(6).add(3);
        rows.get(6).add(4);
        rows.get(6).add(5);
        rows.get(6).add(6);
        rows.get(7).add(0);
        rows.get(7).add(1);
        rows.get(7).add(2);
        rows.get(7).add(3);
        rows.get(7).add(4);
        rows.get(7).add(5);
        rows.get(8).add(0);
        rows.get(8).add(1);
        rows.get(8).add(2);
        rows.get(8).add(3);
        rows.get(8).add(4);









        // Add elements to the 2D ArrayList


        Random rand = new Random();
//randomly placing the atoms and circle of influnces arount the board
        for(int i=0;i<6;i++){


            Random_Rows[i]=rand.nextInt(9);
            Random_Coordinate[i]=rand.nextInt(rows.get(Random_Rows[i]).size());
            for(int j = 0;j<i;j++) {
                if (Random_Coordinate[i] == Random_Coordinate[j]) {
                    Random_Coordinate[i] = rand.nextInt(rows.get(Random_Rows[i]).size());
                }
            }
                    Vector2 randomHexPosition = hexGrid.calculateHexagonPosition(coordinates[Random_Rows[i]][Random_Coordinate[i]][0], coordinates[Random_Rows[i]][Random_Coordinate[i]][1]);
                    atoms[i].setPosition(randomHexPosition);
                    CIFs[i]=new Circle(atoms[i].getPosition().x,atoms[i].getPosition().y,100);



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

        font = new BitmapFont();
        for(int i=0;i<9;i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                Vector2 midpoint=hexGrid.calculateHexagonPosition(coordinates[i][j][0],coordinates[i][j][1]);
                font.draw(batch, "{"+i+","+j+"}", midpoint.x, midpoint.y);
            }
        }
//
        if(ShowScore) {
            font.setColor(Color.RED);
            font.getData().setScale(3);
            font.draw(batch, "SCORE:\n" + score, 0, 900);
        }

        if (render_atoms) {
            renderGameBoard();


        }

        batch.end();
        stage.act();
        stage.draw();
//
        shapeRenderer = new ShapeRenderer();
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1); // Set color to red with 50% opacity
        if (render_atoms) {
            for (int i = 0; i < 6; i++) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.circle(CIFs[i].x, CIFs[i].y, 100); // Draw a circle with center at (100, 100) and radius 50
                shapeRenderer.end();
            }

        }
//        no_atom_encounter_absorbtion();
        newDeflect();
        newDeflectWithoutRender();
//        shapeRenderer.setAutoShapeType(true);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(1, 0, 0, 1); // Red line
//        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);



        box2DDebugRenderer.render(world, camera.combined);
    }



    private void renderGameBoard() {//renders the game board

        for(int i=0;i<6;i++){

          //  CIFs[i].render(batch);

            atoms[i].render(batch);

        }

    }

    /**
     * @param array
     * @param target
     * @return an int array with index of the array where the element in the 2d array matches with the target
     */
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


    // Function to calculate the deflected angle

    /**
     * This Function is the main function for most of the ray deflection logic is in the code. rays are also rendered in this function.
     */
    public void newDeflect() {
        if (render_atoms) {
            for (int i = 0; i < Dialog_Input.size(); i++) {
                int[] index = search2DArray(straightline_pairs, Dialog_Input.get(i));
                    Direction D=pair[index[0]];
                Vector2 Hex_left=null;
                Vector2 Hex_right=null;
                Vector2 Hex_next=null;
//                shapeRenderer = new ShapeRenderer();
//                assert index != null;
                if (index[1] == 1) {
                    D = D.reverse(D);
                    Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                    Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                    Vector2 Direction = findDirection(startPoint, endPoint);
                    Vector2 Row_cols = findROWDirection(Direction, startPoint, coordinates);
                    boolean absorbed=false;

                    Vector2 Near_point = findNearestPointInDirection(Direction,startPoint,coordinates);
                    shapeRenderer.setAutoShapeType(true);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(1, 0, 0, 1); // Red line
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rectLine(startPoint, Near_point,5);
                    shapeRenderer.end();
                    if(!Find_atom_start(Near_point)) {
                        startPoint=Near_point;
                        do{

                            if (D == Innercore.Direction.EAST && Row_cols.x == 0) {

                                Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0) {
                                Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8) {

                                Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8) {
                                Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y + 1);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x-1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y+1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y - 1);
                            }


                            if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                 break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHWEST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                                 absorbed=true;
break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                 absorbed=true;
break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                 absorbed=true;
break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                 absorbed=true;
break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.SOUTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                 absorbed=true;
break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                 absorbed=true;
break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.SOUTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                 absorbed=true;
break;
                            } else if (D == Innercore.Direction.SOUTHEAST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_left) && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                     absorbed=true;
break;
                                }
                            }else if (D == Innercore.Direction.SOUTHEAST && Row_cols.y == 0) {
                                    if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                         absorbed=true;
break;
                                    } else if (Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                        D = Innercore.Direction.SOUTHWEST;
                                        break;
                                    } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                        D = Innercore.Direction.WEST;
                                        break;
                                    }
                                }

                                else if (D == Innercore.Direction.SOUTHEAST&& (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                    if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                         absorbed=true;
break;
                                    }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                        D = Innercore.Direction.EAST;
                                        break;

                                    } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                        D = Innercore.Direction.NORTHEAST;
                                        break;
                                    }
                                }


                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                     absorbed=true;
break;
                                }
                            }
                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ){
                                     absorbed=true;
break;
                                }  else if (Find_atom(Hex_left)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            } else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right) ){
                                     absorbed=true;
break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                }
                            }else if (D == Innercore.Direction.NORTHEAST && Row_cols.y != 0 && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                     absorbed=true;
break;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }

                            }else if (D == Innercore.Direction.NORTHEAST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                }

                            }  else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                     absorbed=true;
break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                     absorbed=true;
break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                     absorbed=true;
break;
                                }  else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                }
                            }
                            if (D == Innercore.Direction.EAST) {
//                        Row_cols.x=Row_cols.x;
                                Row_cols.y = Row_cols.y + 1;

                            } else if (D == Innercore.Direction.WEST) {
                                Row_cols.y = Row_cols.y - 1;

                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;


                                } else {

                                    Row_cols.x += 1;
                                    Row_cols.y += 1;
                                }


                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {

                                    Row_cols.x -= 1;

                                } else {

                                    Row_cols.x -= 1;
                                    Row_cols.y -= 1;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Row_cols.x -= 1;
                                    Row_cols.y += 1;
                                } else {
                                    Row_cols.x-=1;

                                }

                            }else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;
                                    Row_cols.y -= 1;
                                } else {

                                    Row_cols.x += 1;

                                }

                            }

                            Near_point = hexGrid.calculateHexagonPosition(coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][0], coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][1]);
                            shapeRenderer.setAutoShapeType(true);
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                            shapeRenderer.setColor(1, 0, 0, 1); // Red line
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.rectLine(startPoint, Near_point, 5);
                            shapeRenderer.end();
                            startPoint = Near_point;

                        }while(Boundry(Hex_next, Dialog_Input.get(i)));
                        if(absorbed==false){

                           Vector2 Direction2=getDir(D);
                            Vector2 Endpoint=null;
                            if(D== Innercore.Direction.EAST||D== Innercore.Direction.WEST){
                                Endpoint=findThirdNearestPoint(Direction2,Near_point,edges);
                            } else if (D== Innercore.Direction.SOUTHEAST) {
                                Endpoint=findSecondNearestPoint(Direction2,Near_point,edges);
                            } else{
                                Endpoint=findNearestPointInDir(Direction2,Near_point,edges);
                            }
                            shapeRenderer.setAutoShapeType(true);
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                            shapeRenderer.setColor(1, 0, 0, 1); // Red line
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.rectLine(Near_point,Endpoint, 5);
                            shapeRenderer.end();
                        }

                    }
                }
                else {
//                    D = D.reverse(D);
                    Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                    Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                    Vector2 Direction = findDirection(startPoint, endPoint);
                    Vector2 Row_cols = findROWDirection(Direction, startPoint, coordinates);
                    Vector2 Near_point = findNearestPointInDirection(Direction, startPoint, coordinates);
                    boolean absorbed=false;
                    shapeRenderer.setAutoShapeType(true);
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    shapeRenderer.setColor(1, 0, 0, 1); // Red line
                    shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.rectLine(startPoint, Near_point, 5);
                    shapeRenderer.end();
                    if (!Find_atom_start(Near_point)) {
                        startPoint = Near_point;
//                        while (Boundry(Row_cols)) {
                        do {

                            if (D == Innercore.Direction.EAST && Row_cols.x == 0) {

                                Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0) {
                                Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8) {

                                Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8) {
                                Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y + 1);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y+1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y - 1);
                            }


                            if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHWEST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.SOUTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.SOUTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.SOUTHEAST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }else if (D == Innercore.Direction.SOUTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }
                            }

                            else if (D == Innercore.Direction.SOUTHEAST&& (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;
                                    break;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                }
                            }


                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            } else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                }
                            }else if (D == Innercore.Direction.NORTHEAST && Row_cols.y != 0 && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }

                            }else if (D == Innercore.Direction.NORTHEAST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                }

                            }  else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                }
                            }
                            if (D == Innercore.Direction.EAST) {
//                        Row_cols.x=Row_cols.x;
                                Row_cols.y = Row_cols.y + 1;

                            } else if (D == Innercore.Direction.WEST) {
                                Row_cols.y = Row_cols.y - 1;

                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;


                                } else {

                                    Row_cols.x += 1;
                                    Row_cols.y += 1;
                                }


                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {

                                    Row_cols.x -= 1;

                                } else {

                                    Row_cols.x -= 1;
                                    Row_cols.y -= 1;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Row_cols.x -= 1;
                                    Row_cols.y += 1;
                                } else {
                                    Row_cols.x -= 1;

                                }

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;
                                    Row_cols.y -= 1;
                                } else {

                                    Row_cols.x += 1;

                                }

                            }
//                        System.out.println(Find_atom(Hex_next));
//                        System.out.println(Row_cols);

                            Near_point = hexGrid.calculateHexagonPosition(coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][0], coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][1]);

                            shapeRenderer.setAutoShapeType(true);
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                            shapeRenderer.setColor(1, 0, 0, 1); // Red line
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.rectLine(startPoint, Near_point, 5);
                            shapeRenderer.end();
                            startPoint = Near_point;
                            //.out.println(rows.get((int) Hex_next.x).size());
//                        }while(Hex_next.y>=0 && Hex_next.y<=rows.get((int) Hex_next.x).size()-1 && Hex_next.x>0 && Hex_next.x<8);
                        }while(Boundry(Hex_next, Dialog_Input.get(i)));
                        if(absorbed==false){
                            //.out.println(D);
                            Vector2 Direction2=getDir(D);
                            Vector2 Endpoint=null;
                            if(D== Innercore.Direction.EAST||D== Innercore.Direction.WEST){
                                Endpoint=findThirdNearestPoint(Direction2,Near_point,edges);
                            } else if (D== Innercore.Direction.SOUTHEAST) {
                                Endpoint=findSecondNearestPoint(Direction2,Near_point,edges);
                            }else{
                                Endpoint=findNearestPointInDir(Direction2,Near_point,edges);
                            }

                            shapeRenderer.setAutoShapeType(true);
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                            shapeRenderer.setColor(1, 0, 0, 1); // Red line
                            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.rectLine(Near_point,Endpoint, 5);
                            shapeRenderer.end();
                        }


                    }
                }
            }
        }
    }

    /**
     * This fuction is also very important as this function makes the logic behind the deflection but lines are not rendered. This function also helps the arrows to be printed.
     */
    public void newDeflectWithoutRender() {

            for (int i = 0; i < Dialog_Input.size(); i++) {
                int[] index = search2DArray(straightline_pairs, Dialog_Input.get(i));
                Direction D=pair[index[0]];
                Vector2 Hex_left=null;
                Vector2 Hex_right=null;
                Vector2 Hex_next=null;
//                shapeRenderer = new ShapeRenderer();
//                assert index != null;
                if (index[1] == 1) {
                    D = D.reverse(D);
                    Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                    Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                    Vector2 Direction = findDirection(startPoint, endPoint);
                    Vector2 Row_cols = findROWDirection(Direction, startPoint, coordinates);
                    boolean absorbed=false;
                    float angle=Direction.angle();
                    Vector2 Near_point = findNearestPointInDirection(Direction,startPoint,coordinates);
                    drawArrow(batch,startPoint,angle);
                    if(!Find_atom_start(Near_point)) {
                        startPoint=Near_point;
                        do{

                            if (D == Innercore.Direction.EAST && Row_cols.x == 0) {

                                Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0) {
                                Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8) {

                                Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8) {
                                Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y + 1);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x-1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y+1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y - 1);
                            }


                            if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHWEST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.SOUTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.SOUTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.SOUTHEAST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }else if (D == Innercore.Direction.SOUTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }
                            }

                            else if (D == Innercore.Direction.SOUTHEAST&& (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;
                                    break;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                }
                            }


                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            } else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                }
                            }else if (D == Innercore.Direction.NORTHEAST && Row_cols.y != 0 && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }

                            }else if (D == Innercore.Direction.NORTHEAST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                }

                            }  else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                }
                            }
                            if (D == Innercore.Direction.EAST) {
//                        Row_cols.x=Row_cols.x;
                                Row_cols.y = Row_cols.y + 1;

                            } else if (D == Innercore.Direction.WEST) {
                                Row_cols.y = Row_cols.y - 1;

                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;


                                } else {

                                    Row_cols.x += 1;
                                    Row_cols.y += 1;
                                }


                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {

                                    Row_cols.x -= 1;

                                } else {

                                    Row_cols.x -= 1;
                                    Row_cols.y -= 1;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Row_cols.x -= 1;
                                    Row_cols.y += 1;
                                } else {
                                    Row_cols.x-=1;

                                }

                            }else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;
                                    Row_cols.y -= 1;
                                } else {

                                    Row_cols.x += 1;

                                }

                            }

                            Near_point = hexGrid.calculateHexagonPosition(coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][0], coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][1]);

                            startPoint = Near_point;

                        }while(Boundry(Hex_next, Dialog_Input.get(i)));
                        if(absorbed==false){
                            //.out.println(D);
                            Vector2 Direction2=getDir(D);
                            float arrowRotation = Direction2.angle();
                            Vector2 Endpoint=findSecondNearestPoint(Direction2,Near_point,edges);
                            drawArrow(batch,Near_point,arrowRotation);

                        }
                    }
                }
                else {
//                    D = D.reverse(D);
                    Vector2 startPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][0] - 1][0], edges[straightline_pairs[index[0]][0] - 1][1]);
                    Vector2 endPoint = hexGrid.calculateHexagonPosition(edges[straightline_pairs[index[0]][1] - 1][0], edges[straightline_pairs[index[0]][1] - 1][1]);
                    Vector2 Direction = findDirection(startPoint, endPoint);
                    float angle=Direction.angle();
                    Vector2 Row_cols = findROWDirection(Direction, startPoint, coordinates);
                    Vector2 Near_point = findNearestPointInDirection(Direction, startPoint, coordinates);
                    drawArrow(batch,startPoint,angle);
                    boolean absorbed=false;
                    if (!Find_atom_start(Near_point)) {
                        startPoint = Near_point;
//                        while (Boundry(Row_cols)) {
                        do {

                            if (D == Innercore.Direction.EAST && Row_cols.x == 0) {

                                Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0) {
                                Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8) {

                                Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8) {
                                Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0) {
                                if (Row_cols.x < 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else if (Row_cols.x == 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                } else if (Row_cols.x > 4) {
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                }
                                Hex_next = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y + 1);
                                    Hex_right = new Vector2(Row_cols.x + 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y + 1);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                    Hex_right = new Vector2(Row_cols.x - 1, Row_cols.y);
                                }
                                Hex_left = new Vector2(Row_cols.x, Row_cols.y - 1);
                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 3) {
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y + 1);
                                } else {
                                    Hex_next = new Vector2(Row_cols.x - 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x - 1, Row_cols.y - 1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y + 1);

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y - 1);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y);

                                } else {
                                    Hex_next = new Vector2(Row_cols.x + 1, Row_cols.y);
                                    Hex_left = new Vector2(Row_cols.x + 1, Row_cols.y+1);
                                }
                                Hex_right = new Vector2(Row_cols.x, Row_cols.y - 1);
                            }


                            if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHWEST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 0 && Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHEAST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x == 8 && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x == 8 && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                                break;
                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHEAST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.SOUTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.NORTHWEST;

                            } else if (D == Innercore.Direction.EAST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;

                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.SOUTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                D = Innercore.Direction.NORTHWEST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                D = Innercore.Direction.SOUTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                D = Innercore.Direction.NORTHEAST;
                            } else if (D == Innercore.Direction.WEST && Row_cols.x != 8 && Row_cols.x != 0 && Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                absorbed=true;
                                break;
                            } else if (D == Innercore.Direction.SOUTHEAST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }else if (D == Innercore.Direction.SOUTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }
                            }

                            else if (D == Innercore.Direction.SOUTHEAST&& (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.EAST;
                                    break;

                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                }
                            }


                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            } else if (D == Innercore.Direction.NORTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right) ){
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_right)  && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHWEST;
                                    break;
                                }
                            }else if (D == Innercore.Direction.NORTHEAST && Row_cols.y != 0 && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST && Row_cols.y == 0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                }

                            }else if (D == Innercore.Direction.NORTHEAST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) ) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                }

                            }  else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y!=rows.get((int) Row_cols.x).size()-1) && Row_cols.y!=0) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;

                                } else if (Find_atom(Hex_left) && !Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.WEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && !Find_atom(Hex_right)) {
                                    D = Innercore.Direction.NORTHWEST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right) && !Find_atom(Hex_left)) {
                                    D = Innercore.Direction.EAST;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) && Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && Find_atom(Hex_left) && !Find_atom(Hex_next)) {
                                    absorbed=true;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==rows.get((int) Row_cols.x).size()-1)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_right)) {
                                    absorbed=true;
                                    break;
                                } else if (Find_atom(Hex_right) && !Find_atom(Hex_next)) {
                                    D = Innercore.Direction.SOUTHEAST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_right)) {
                                    D = Innercore.Direction.EAST;
                                    break;
                                }
                            }
                            else if (D == Innercore.Direction.SOUTHWEST && (Row_cols.y==0)) {
                                if (Find_atom(Hex_next) && !Find_atom(Hex_left)) {
                                    absorbed=true;
                                    break;
                                }  else if (Find_atom(Hex_left) && !Find_atom(Hex_next) ) {
                                    D = Innercore.Direction.WEST;
                                    break;
                                } else if (Find_atom(Hex_next) && Find_atom(Hex_left) ) {
                                    D = Innercore.Direction.NORTHWEST;
                                    break;
                                }
                            }
                            if (D == Innercore.Direction.EAST) {
//                        Row_cols.x=Row_cols.x;
                                Row_cols.y = Row_cols.y + 1;

                            } else if (D == Innercore.Direction.WEST) {
                                Row_cols.y = Row_cols.y - 1;

                            } else if (D == Innercore.Direction.SOUTHEAST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;


                                } else {

                                    Row_cols.x += 1;
                                    Row_cols.y += 1;
                                }


                            } else if (D == Innercore.Direction.NORTHWEST) {
                                if (Row_cols.x > 4) {

                                    Row_cols.x -= 1;

                                } else {

                                    Row_cols.x -= 1;
                                    Row_cols.y -= 1;
                                }

                            } else if (D == Innercore.Direction.NORTHEAST) {
                                if (Row_cols.x > 4) {
                                    Row_cols.x -= 1;
                                    Row_cols.y += 1;
                                } else {
                                    Row_cols.x -= 1;

                                }

                            } else if (D == Innercore.Direction.SOUTHWEST) {
                                if (Row_cols.x > 3) {
                                    Row_cols.x += 1;
                                    Row_cols.y -= 1;
                                } else {

                                    Row_cols.x += 1;

                                }

                            }
                            Near_point = hexGrid.calculateHexagonPosition(coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][0], coordinates[(int) (Row_cols.x)][(int) (Row_cols.y)][1]);

                            startPoint = Near_point;
                        }while(Boundry(Hex_next, Dialog_Input.get(i)));
                        if(absorbed==false){
                            Vector2 Direction2=getDir(D);
                            float arrowRotation = Direction2.angle();
                            Vector2 Endpoint=findSecondNearestPoint(Direction2,Near_point,edges);
                            drawArrow(batch,Near_point,arrowRotation);

                        }


                    }
                }
            }
        }

    /**
     * @param vector1
     * @param vector2
     * @return a direction vector in a form of Vector2
     */
    public static Vector2 findDirection(Vector2 vector1, Vector2 vector2) {
        // Subtract vector1 from vector2 to get the direction vector
        return vector2.cpy().sub(vector1);
    }

    /**
     * @param D->is the direction enum val
     * @return the Direction in terms of a direction vector.
     */
    public Vector2 getDir(Direction D){
        Vector2 X = null;
        if(D==Direction.EAST){
            X=findDirection(hexGrid.calculateHexagonPosition(edges[10][0],edges[10][1]),hexGrid.calculateHexagonPosition(edges[37][0],edges[37][1]));
        } else if (D==Direction.NORTHEAST) {
            X=findDirection(hexGrid.calculateHexagonPosition(edges[19][0],edges[19][1]),hexGrid.calculateHexagonPosition(edges[46][0],edges[46][1]));
        } else if (D==Direction.WEST) {
            X=findDirection(hexGrid.calculateHexagonPosition(edges[37][0],edges[37][1]),hexGrid.calculateHexagonPosition(edges[10][0],edges[10][1]));

        } else if (D==Direction.SOUTHWEST) {
            X=findDirection(hexGrid.calculateHexagonPosition(edges[46][0],edges[46][1]),hexGrid.calculateHexagonPosition(edges[19][0],edges[19][1]));
        }else if(D==Direction.NORTHWEST){
            X=findDirection(hexGrid.calculateHexagonPosition(edges[28][0],edges[28][1]),hexGrid.calculateHexagonPosition(edges[1][0],edges[1][1]));
        } else if (D==Direction.SOUTHEAST) {
            X=findDirection(hexGrid.calculateHexagonPosition(edges[1][0],edges[1][1]),hexGrid.calculateHexagonPosition(edges[28][0],edges[28][1]));
        }
        return X;
    }

    /**
     * @param direction
     * @param startingPoint
     * @param points
     * @return the coordinates in terms of rows and columns of the nearest point in direction of the staring point.
     */
    public static Vector2 findROWDirection(Vector2 direction, Vector2 startingPoint, double[][][] points) {
        float minDistance = Float.MAX_VALUE;
        Vector2 nearestPoint = null;
        Vector2 row=null;

        // Normalize direction vector
        direction.nor();

        // Iterate through points
        for (int i=0;i<9;i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                // Calculate vector from starting point to current point
                Vector2 vectorToPoint = new Vector2(hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]).x - startingPoint.x, hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]).y - startingPoint.y);

                // Calculate dot product to determine if the point is in the direction
                float dotProduct = vectorToPoint.dot(direction);

                // Check if the dot product is positive, indicating that the point is in the direction
                if (dotProduct > 0) {
                    float dist = startingPoint.dst(hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]));
                    if (dist < minDistance) {
                        minDistance = dist;
                        nearestPoint = hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]);
                        row=new Vector2(i,j);
                    }
                }
            }
        }

        return row;
    }

    /**
     * @param direction
     * @param startingPoint
     * @param points->3D aaray
     * @return the nearest point in the 3D array points in the direction from the starting point.
     */
    public static Vector2 findNearestPointInDirection(Vector2 direction, Vector2 startingPoint, double[][][] points) {
        float minDistance = Float.MAX_VALUE;
        Vector2 nearestPoint = null;

        // Normalize direction vector
        direction.nor();

        // Iterate through points
        for (int i=0;i<9;i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                // Calculate vector from starting point to current point
                Vector2 vectorToPoint = new Vector2(hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]).x - startingPoint.x, hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]).y - startingPoint.y);

                // Calculate dot product to determine if the point is in the direction
                float dotProduct = vectorToPoint.dot(direction);

                // Check if the dot product is positive, indicating that the point is in the direction
                if (dotProduct > 0) {
                    float dist = startingPoint.dst(hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]));
                    if (dist < minDistance) {
                        minDistance = dist;
                        nearestPoint = hexGrid.calculateHexagonPosition(points[i][j][0], points[i][j][1]);
                    }
                }
            }
        }

        return nearestPoint;
    }

    /**
     * @param H
     * @return true if the atom is in H vector, else returns false.
     */
    public boolean Find_atom(Vector2 H) {
        for (int i = 0; i < atoms.length; i++) {
            if (atoms[i].getPosition().x == hexGrid.calculateHexagonPosition(coordinates[(int) H.x][(int) H.y][0], coordinates[(int) H.x][(int) H.y][1]).x && atoms[i].getPosition().y == hexGrid.calculateHexagonPosition(coordinates[(int) H.x][(int) H.y][0], coordinates[(int) H.x][(int) H.y][1]).y ) {
                //.out.println(atoms[i].getPosition()+","+H);
                return true;
            }
        }
        return false;
    }

    /**
     * @param H
     * @return true if the start hexagon have the atom
     */
    public boolean Find_atom_start(Vector2 H) {
        for (int i = 0; i < atoms.length; i++) {
            if (atoms[i].getPosition().x == H.x && atoms[i].getPosition().y ==H.y ) {
                //.out.println(atoms[i].getPosition()+","+H);
                return true;
            }
        }
        return false;
    }

    /**
     * @param H->current hexagon
     * @param G->input from dialog box
     * @return boundry for the hexagon board
     */
    public boolean Boundry(Vector2 H, int G) {
        if(G==54 || G==47){
            return H.y>=0 && H.y<=rows.get((int) H.x).size()-1 && H.x>0 && H.x<4;
        }
        else if(G==11 || G==36 || G==9 ||G==38){
            return H.y>=0 && H.y<=rows.get((int) H.x).size()-1 && H.x>0 && H.x<8;
        } else if (G==20 || G==27) {
            return H.y>=0 && H.y<=rows.get((int) H.x).size()-1 && H.x>4 && H.x<8;
        }else if (G==2 || G==45 || G==18 || G==29) {
            return H.y>0 && H.y<rows.get((int) H.x).size()-1 && H.x>=0 && H.x<=8;
        }
        else{
            return H.y>0 && H.y<rows.get((int) H.x).size()-1 && H.x>0 && H.x<8;
        }

    }

    /**
     * @param direction
     * @param startingPoint
     * @param points
     * @return find the nearest point in a 2D array in the direction
     */
    public static Vector2 findNearestPointInDir(Vector2 direction, Vector2 startingPoint, double[][] points) {
        float minDistance = Float.MAX_VALUE;
        Vector2 nearestPoint = null;

        // Normalize direction vector
        direction.nor();

        // Iterate through points
        for (int i=0;i<54;i++) {

                // Calculate vector from starting point to current point
                Vector2 vectorToPoint = new Vector2(hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).x - startingPoint.x, hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).y - startingPoint.y);

                // Calculate dot product to determine if the point is in the direction
                float dotProduct = vectorToPoint.dot(direction);

                // Check if the dot product is positive, indicating that the point is in the direction
                if (dotProduct > 0) {
                    float dist = startingPoint.dst(hexGrid.calculateHexagonPosition(points[i][0], points[i][1]));
                    if (dist < minDistance) {
                        minDistance = dist;
                        nearestPoint = hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);
                    }
                }
            }


        return nearestPoint;
    }

    /**
     * @param direction
     * @param startingPoint
     * @param points
     * @return second-nearest point in the direction in the 2D array
     */
    public static Vector2 findSecondNearestPoint(Vector2 direction, Vector2 startingPoint, double[][] points) {
        Vector2 nearestPoint = null;
        Vector2 secondNearestPoint = null;
        float nearestDistance = Float.MAX_VALUE;
        float secondNearestDistance = Float.MAX_VALUE;

        for (int i=0;i<54;i++) {

            // Calculate vector from starting point to current point
            Vector2 vectorToPoint = new Vector2(hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).x - startingPoint.x, hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).y - startingPoint.y);

            // Calculate dot product of direction and vector to current point
            float dotProduct = direction.dot(vectorToPoint);

            // Check if the point is in the direction of the given direction
            if (dotProduct > 0) {
                // Calculate distance to current point
                float distance = vectorToPoint.len();

                // Update nearest and second nearest points if applicable
                if (distance < nearestDistance) {
                    secondNearestDistance = nearestDistance;
                    secondNearestPoint = nearestPoint;
                    nearestDistance = distance;
                    nearestPoint = hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);;
                } else if (distance < secondNearestDistance) {
                    secondNearestDistance = distance;
                    secondNearestPoint =hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);;
                }
            }
        }

        return secondNearestPoint;
    }

    /**
     * @param direction
     * @param startingPoint
     * @param points
     * @return the third-nearest point in direction in the 2D array
     */
    public static Vector2 findThirdNearestPoint(Vector2 direction, Vector2 startingPoint, double[][] points) {
        Vector2 nearestPoint = null;
        Vector2 secondNearestPoint = null;
        Vector2 thirdNearestPoint = null;
        float nearestDistance = Float.MAX_VALUE;
        float secondNearestDistance = Float.MAX_VALUE;
        float thirdNearestDistance = Float.MAX_VALUE;

        for (int i=0;i<54;i++) {
            Vector2 vectorToPoint = new Vector2(hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).x - startingPoint.x, hexGrid.calculateHexagonPosition(points[i][0], points[i][1]).y - startingPoint.y);

            // Calculate vector from origin to current point


            // Calculate dot product of direction and vector to current point
            float dotProduct = direction.dot(vectorToPoint);

            // Check if the point is in the direction of the given direction
            if (dotProduct > 0) {
                // Calculate distance to current point
                float distance = vectorToPoint.len();

                // Update nearest, second nearest, and third nearest points if applicable
                if (distance < nearestDistance) {
                    thirdNearestDistance = secondNearestDistance;
                    thirdNearestPoint = secondNearestPoint;
                    secondNearestDistance = nearestDistance;
                    secondNearestPoint = nearestPoint;
                    nearestDistance = distance;
                    nearestPoint = hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);
                } else if (distance < secondNearestDistance) {
                    thirdNearestDistance = secondNearestDistance;
                    thirdNearestPoint = secondNearestPoint;
                    secondNearestDistance = distance;
                    secondNearestPoint =hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);
                } else if (distance < thirdNearestDistance) {
                    thirdNearestDistance = distance;
                    thirdNearestPoint = hexGrid.calculateHexagonPosition(points[i][0], points[i][1]);
                }
            }
        }

        return thirdNearestPoint;
    }

    /**
     * @param batch
     * @param position
     * @param Direction
     */
    public void drawArrow(SpriteBatch batch, Vector2 position,float Direction) {
        batch.begin();
        batch.draw(arrowRegion,position.x-arrowRegion.getRegionHeight(),position.y-arrowRegion.getRegionWidth(),arrowRegion.getRegionWidth()/2f,arrowRegion.getRegionHeight()/2f,arrowRegion.getRegionWidth(),arrowRegion.getRegionHeight(),1,1,Direction);
        batch.end();
    }

    @Override
    public void dispose () {
        shapeRenderer.dispose();
    }





}

