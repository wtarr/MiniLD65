package com.bugsandcode.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class SnakePart {

    private float posx;
    private float posy;
    private SnakePartType partType;

    private Rectangle boundingRect;

    private Texture texture;

    public Rectangle getBoundingRect() {
        return boundingRect;
    }

    public float getPosx() {
        return posx;
    }

    public float getPosy() {
        return posy;
    }

    public SnakePartType getPartType() {
        return partType;
    }

    public SnakePart(SnakePartType part, float posx, float posy, Texture texture)
    {
        this.partType = part;
        this.posx = posx;
        this.posy = posy;

        boundingRect = new Rectangle(posx, posy, MainGame.GRID_CELL, MainGame.GRID_CELL);

        this.texture = texture;
    }

    public void updatePos(float x, float y)
    {
        posx = x;
        posy = y;

        boundingRect.setPosition(x, y);
    }

    public void update(float delta)
    {

    }

    public void render(Batch batch)
    {
        batch.draw(texture, boundingRect.getX(), boundingRect.getY());
    }

    public void renderDebug(ShapeRenderer shapeRenderer)
    {
        //shapeRenderer.setColor(Color.CYAN);
        //shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        //shapeRenderer.rect(posx, posy, MainGame.GRID_CELL, MainGame.GRID_CELL);
    }

}
