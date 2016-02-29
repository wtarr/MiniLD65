package com.bugsandcode.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by William on 28/02/2016.
 */
public class Fruit {

    private Texture texture;

    private Rectangle boundingRect;

    public Rectangle getBoundingRect()
    {
        return boundingRect;
    }

    public Fruit(float posX, float posY, Texture texture)
    {
        boundingRect = new Rectangle(posX, posY, MainGame.GRID_CELL, MainGame.GRID_CELL);
        this.texture = texture;
    }

    public float getX()
    {
        return boundingRect.x;
    }

    public float getY()
    {
        return boundingRect.y;
    }

    public void render(Batch batch)
    {
        batch.draw(texture, boundingRect.getX(), boundingRect.getY());
    }

    public void renderDebug(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(boundingRect.getX(), boundingRect.getY(), MainGame.GRID_CELL, MainGame.GRID_CELL);
    }
}
