package me.squander.roguelikegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public enum RoomType {
    START("start"),
    BASE("base"),
    KEY("key"),
    END("end");

    private final String texturePath;
    RoomType(String texturePath){
        this.texturePath = texturePath;
    }

    public Texture getRoomTexture(){
        return new Texture(Gdx.files.internal(texturePath + ".png"));
    }
}
