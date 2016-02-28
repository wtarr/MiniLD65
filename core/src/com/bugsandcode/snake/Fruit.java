package com.bugsandcode.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by William on 28/02/2016.
 */
public class Fruit {

    private Rectangle boundingRect;

    public Rectangle getBoundingRect()
    {
        return boundingRect;
    }

    public Fruit(float posX, float posY)
    {
        boundingRect = new Rectangle(posX, posY, MainGame.GRID_CELL, MainGame.GRID_CELL);
    }

    public float getX()
    {
        return boundingRect.x;
    }

    public float getY()
    {
        return boundingRect.y;
    }

    public void render(Batch batchx)
    {

    }

    public void renderDebug(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(boundingRect.getX(), boundingRect.getY(), MainGame.GRID_CELL, MainGame.GRID_CELL);
    }
}
