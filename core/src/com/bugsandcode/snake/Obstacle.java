package com.bugsandcode.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by William on 28/02/2016.
 */
public class Obstacle {

    private Rectangle boundingRect;
    private Texture texture;

    public Obstacle(float posx, float posy) {
        boundingRect = new Rectangle(posx, posy, MainGame.GRID_CELL, MainGame.GRID_CELL);
    }

    public Rectangle getBoundingRect()
    {
        return boundingRect;
    }

    public void renderDebug(ShapeRenderer shapeRenderer)
    {
        // todo
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(boundingRect.getX(), boundingRect.getY(), boundingRect.width, boundingRect.getHeight());
    }

    public void render(Batch batch)
    {
        // todo
    }

    public float getX()
    {
        return boundingRect.x;
    }

    public float getY()
    {
        return boundingRect.y;
    }
}
