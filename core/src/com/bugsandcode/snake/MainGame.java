package com.bugsandcode.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ScreenAdapter {

    public static Difficulty GameDifficulty = Difficulty.Level1;

    SnakeGame snakeGame;

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Texture img;

    private final float SCREEN_WIDTH = 640;
    private final float SCREEN_HEIGHT = 480;

    private final float GAME_HEIGHT = 432;
    private final float GAME_WIDTH = 640;

    private Camera camera;
    private Viewport viewport;

    public static final float GRID_CELL = 16;

    private float minNextMoveAllowed = 0.1f;
    private float current = 0;

    private SnakePart head;

    private int score = 0;
    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont bitmapFont;


    private boolean fruitInPlace = false;
    private Fruit fruit;
    private float fruitExpire = 10;
    private float currentFruitTime = 0;
    private float minNextFruitPlacement = 2;
    private float currentFruitPlacementTime = 0;
    private final float FRUITPOINTS = 20;

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private enum GameState {
        Playing,
        GameOver
    }

    private GameState currentState = GameState.Playing;

    private Array<SnakePart> snake = new Array<SnakePart>();
    private Array<Obstacle> obstacles = new Array<Obstacle>(); // boundarys, random

    private Direction CurrentDirection = Direction.RIGHT;
    private Direction NextDirection = Direction.RIGHT;

    public MainGame(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // snake starts with 3 parts (1 head + 2 body) travelling in the direction RIGHT)
        head = new SnakePart(SnakePartType.Head, 48, 16);

        snake.add(new SnakePart(SnakePartType.Body, 16, 16)); // 0
        snake.add(new SnakePart(SnakePartType.Body, 32, 16)); // 1

        bitmapFont = new BitmapFont();

        if (GameDifficulty == Difficulty.Level2 || GameDifficulty == Difficulty.Level3)
        {
            placeBoundary();
        }

    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    public void update(float delta) {
        pollForInput();

        if (currentState == GameState.Playing) {

            if (!fruitInPlace && currentFruitPlacementTime > minNextFruitPlacement) {
                // place fruit
                placeFruit();

                // reset timer
                currentFruitPlacementTime = 0;
            } else {
                currentFruitPlacementTime += delta;
            }

            if (current >= minNextMoveAllowed) {
                // move snake
                moveSnake();

                checkForObstacleCollision();

                checkForFruitCollision();

                checkForSelfCollision();

                checkForOffScreen();
                // reset
                current = 0;
            } else {
                current += delta;
            }

            if (fruitInPlace) {
                checkForFruitExpiration(delta);
            }
        } else {
            // todo render text "game over"
            Gdx.app.log("MAIN", "GAME OVER");
        }
    }

    private void checkForObstacleCollision() {
        // todo
        for (Obstacle obs : obstacles)
        {
            if (head.getBoundingRect().overlaps(obs.getBoundingRect()))
            {
                currentState = GameState.GameOver;
            }
        }
    }


    private int nextSpeedIncrease = 100;

    private void checkForFruitCollision() {
        if (!fruitInPlace) return;

        if (head.getBoundingRect().overlaps(fruit.getBoundingRect())) {
            SnakePart tail = snake.get(0);

            SnakePart newTail = new SnakePart(SnakePartType.Body, tail.getPosx(), tail.getPosy());

            snake.insert(0, newTail);

            resetFruitForReuse();

            score += FRUITPOINTS;

            if (score >= nextSpeedIncrease && minNextMoveAllowed > 0.01f) {
                nextSpeedIncrease += 100;
                minNextMoveAllowed -= 0.0005f;
                Gdx.app.log("MAIN", "Speed increase! " + Float.toString(minNextMoveAllowed) + " Next increase " + Integer.toString(nextSpeedIncrease));
            }
        }
    }

    // if off screen we place at opposite side
    private void checkForOffScreen() {
        if (head.getPosx() < 0) {
            head.updatePos(GAME_WIDTH - GRID_CELL, head.getPosy());
        }

        if (head.getPosx() > GAME_WIDTH) {
            head.updatePos(0, head.getPosy());
        }

        // y
        if (head.getPosy() > GAME_HEIGHT - GRID_CELL) {
            head.updatePos(head.getPosx(), 0);
        }

        if (head.getPosy() < 0) {
            head.updatePos(head.getPosx(), GAME_HEIGHT - GRID_CELL);
        }
    }

    public void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();

        // todo - if using textures
        String scoreText = String.format("%1$s", score);
        layout.setText(bitmapFont, scoreText);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.draw(batch, scoreText, SCREEN_WIDTH / 2 - layout.width, SCREEN_HEIGHT - ((GRID_CELL * 3)/2) );

        batch.end();

    }

    // for difficulty levels 2 and 3
    public void placeBoundary()
    {
        for (int y = 0; y < GAME_HEIGHT; y += GRID_CELL) {

            if (y == 0 || y == GAME_HEIGHT - GRID_CELL)
            {
                for (int x = 0; x < GAME_WIDTH + 1; x += GRID_CELL)
                {
                    obstacles.add(new Obstacle(x, y));
                }
            } else
            {
                // place y at either side
                obstacles.add(new Obstacle(0, y));
                obstacles.add(new Obstacle(GAME_WIDTH - GRID_CELL, y));
            }
        }
    }

    private void pollForInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);

        //boolean plusPressed = Gdx.input.isKeyPressed(Input.Keys.PLUS);
        //boolean minusPressed = Gdx.input.isKeyPressed(Input.Keys.MINUS);

        if (lPressed && CurrentDirection != Direction.RIGHT) NextDirection = Direction.LEFT;
        if (rPressed && CurrentDirection != Direction.LEFT) NextDirection = Direction.RIGHT;
        if (uPressed && CurrentDirection != Direction.DOWN) NextDirection = Direction.UP;
        if (dPressed && CurrentDirection != Direction.UP) NextDirection = Direction.DOWN;

        //if (plusPressed) {
        //    minNextMoveAllowed -= 0.0005f;
        //   Gdx.app.log("MAIN", Float.toString(minNextMoveAllowed));
       // }

    }

    public void moveSnake() {
        if (CurrentDirection != NextDirection) {
            CurrentDirection = NextDirection;
        }

        float oldX = head.getPosx();
        float oldY = head.getPosy();

        if (CurrentDirection == Direction.RIGHT) {
            head.updatePos(head.getPosx() + GRID_CELL, head.getPosy());
        }

        if (CurrentDirection == Direction.LEFT) {
            head.updatePos(head.getPosx() - GRID_CELL, head.getPosy());
        }

        if (CurrentDirection == Direction.UP) {
            head.updatePos(head.getPosx(), head.getPosy() + GRID_CELL);
        }

        if (CurrentDirection == Direction.DOWN) {
            head.updatePos(head.getPosx(), head.getPosy() - GRID_CELL);
        }

        SnakePart end = snake.removeIndex(0); // tail

        end.updatePos(oldX, oldY);

        snake.add(end);


    }

    public void checkForSelfCollision() {
        for (SnakePart part : snake) {
            if (head.getBoundingRect().overlaps(part.getBoundingRect())) {
                currentState = GameState.GameOver;
            }
        }
    }

    public void placeFruit() {
        if (!fruitInPlace) {

            Vector2 vector2 = yieldAPossiblePlacement();
            fruit = new Fruit(vector2.x, vector2.y);

            fruitInPlace = true;
        }

    }

    /// We want avoid placing the fruit directly on the snake
    /// or on top o
    private Vector2 yieldAPossiblePlacement() {

        boolean isvalid  = false;

        Vector2 testVector = new Vector2(0, 0);

        float placeX = 0, placeY = 0;

        while (true) {

            isvalid = true;

            float x = (int) (MathUtils.random(0, GAME_WIDTH) / GRID_CELL);
            placeX = x * GRID_CELL;

            float y = (int) (MathUtils.random(0, GAME_HEIGHT) / GRID_CELL);
            placeY = y * GRID_CELL;

            testVector = new Vector2(placeX, placeY);

            for (int i = 0; i < snake.size; i++) {

                SnakePart part = snake.get(i);

                if (floatsAreEqual(testVector.x, part.getPosx()) && floatsAreEqual(testVector.y, part.getPosy()))
                {
                    isvalid = false;
                }
            }

            if (floatsAreEqual(testVector.x, head.getPosx()) && floatsAreEqual(testVector.y, head.getPosy()))
            {
                isvalid = false;
            }

            if (obstacles.size > 0) {
                for (int b = 0; b < obstacles.size; b++) {

                    Obstacle obs = obstacles.get(b);

                    if ( floatsAreEqual(testVector.x, obs.getX()) && floatsAreEqual(testVector.y, obs.getY())) {
                        isvalid = false;
                    }

                }
            }


            if (CurrentDirection == Direction.LEFT || CurrentDirection == Direction.RIGHT)
            {
                if (testVector.x == head.getPosx()) {

                    isvalid = false;
                }
            }

            if (CurrentDirection == Direction.UP || CurrentDirection == Direction.DOWN)
            {
                if (testVector.y == head.getPosy()) {
                    isvalid = false;
                }
            }

            // We dont want to place an obstacle on top of a fruit (not nice!)
            if (fruitInPlace)
            {
                if ( floatsAreEqual(testVector.x, fruit.getX()) && floatsAreEqual(testVector.y, fruit.getY())) {
                    isvalid = false;
                }
            }

            if (isvalid) {
                Gdx.app.log("MAIN", "Placement is deemed valid");
                break;
            }


        }

        Gdx.app.log("MAIN", "X; " + testVector.x + " Y: " + testVector.y);
        return testVector;
    }

    public void checkForFruitExpiration(float delta) {
        currentFruitTime += delta;

        if (currentFruitTime >= fruitExpire)
            resetFruitForReuse();

    }

    private void resetFruitForReuse() {
        fruitInPlace = false;
        currentFruitPlacementTime = 0;
        currentFruitTime = 0;

    }

    public void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawGrid();

        if (obstacles.size > 0)
        {
            for (Obstacle obs : obstacles)
            {
                obs.renderDebug(shapeRenderer);
            }
        }

        if (fruitInPlace)
            fruit.renderDebug(shapeRenderer);

        head.renderDebug(shapeRenderer);
        for (SnakePart sp : snake) {
            sp.renderDebug(shapeRenderer);
        }

        shapeRenderer.end();
    }

    public void drawGrid() {
        for (int x = 0; x < GAME_WIDTH + 1; x += GRID_CELL) {
            shapeRenderer.line(x, 0, x, GAME_HEIGHT);
        }

        for (int y = 0; y < GAME_HEIGHT + 1; y += GRID_CELL) {
            shapeRenderer.line(0, y, viewport.getWorldWidth(), y);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private boolean floatsAreEqual(float a, float b)
    {
        if (Math.abs(a - b) < 0.1) return true;

        return false;
    }
}
