package me.squander.roguelikegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture tiles = load("tiles");

    private static Texture load(String path){
        return new Texture(Gdx.files.internal(path + ".png"));
    }
}
