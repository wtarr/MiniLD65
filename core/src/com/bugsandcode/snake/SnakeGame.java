package com.bugsandcode.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by William on 27/02/2016.
 */
public class SnakeGame extends Game {

    private final AssetManager assetManager = new AssetManager();

    @Override
    public void create(){
        //assetManager.setLoader(TiledMap.class, new T);
        setScreen(new MenuScreen(this));
    }

    public AssetManager getAssetManager() { return assetManager; }
}
