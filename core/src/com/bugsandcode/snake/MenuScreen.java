package com.bugsandcode.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by William on 27/02/2016.
 */
public class MenuScreen extends ScreenAdapter {

    private final SnakeGame snakeGame;

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer shapeRenderer;


    private Viewport viewport;
    private OrthographicCamera camera;

    private Image gameTitleImage;
    private TextButton btnPlayLvl1, btnPlayLvl2, btnPlayLvl3;
    private Texture gameTitleTexture, buttonUpTexture, buttonDownTexture, buttonOverTexture;
    private BitmapFont bitmapFont;

    private Table table;
    private Stage stage;

    public MenuScreen(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }

    @Override
    public void show()
    {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        bitmapFont = new BitmapFont();

        // Game Title
        gameTitleTexture = new Texture(Gdx.files.internal("title/gameTitle.png"));
        gameTitleImage = new Image(new TextureRegionDrawable(new TextureRegion(gameTitleTexture)));

        // Set button styles
        buttonUpTexture = new Texture(Gdx.files.internal("buttons/btnIdle.png"));
        buttonOverTexture = new Texture(Gdx.files.internal("buttons/btnOver.png"));
        buttonDownTexture = new Texture(Gdx.files.internal("buttons/btnDown.png"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonUpTexture));
        textButtonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonOverTexture));
        textButtonStyle.down = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));

        // define buttons
        btnPlayLvl1 = new TextButton("Level 1", textButtonStyle);
        btnPlayLvl2 = new TextButton("Level 2", textButtonStyle);
        btnPlayLvl3 = new TextButton("Level 3", textButtonStyle);

        // create table
        table = new Table();
        //table.debug();

        // set structure
        table.row();
        table.add(gameTitleImage).padTop(30f).colspan(2).expand();
        table.row();
        // todo add some snake image??
        table.add(btnPlayLvl1).padTop(15f).padBottom(15f).colspan(2);
        table.row();
        table.add(btnPlayLvl2).padTop(15f).padBottom(15f).colspan(2);
        table.row();
        table.add(btnPlayLvl3).padTop(15f).padBottom(15f).colspan(2);
        table.row();

        // Pack table
        table.setFillParent(true);
        table.pack();

        // set tables alpha to 0
        table.getColor().a = 0f;

        // Play button listener
        btnPlayLvl1.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.log(
                        "MENU", "PLAY 1");
                snakeGame.setScreen(new MainGame(snakeGame));
            }
        });

        btnPlayLvl2.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.log(
                        "MENU", "PLAY 2");
                MainGame.GameDifficulty = Difficulty.Level2;
                snakeGame.setScreen(new MainGame(snakeGame));
            }
        });

        btnPlayLvl3.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.log(
                        "MENU", "PLAY 3");
                MainGame.GameDifficulty = Difficulty.Level3;
                snakeGame.setScreen(new MainGame(snakeGame));
            }
        });


        stage.addActor(table);

        table.addAction(Actions.fadeIn(2f));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        clearScreen();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
        draw();
    }

    private void draw() {

    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.TEAL.r, Color.TEAL.g, Color.TEAL.b, Color.TEAL.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta)
    {

    }
}
