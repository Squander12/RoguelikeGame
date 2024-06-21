package me.squander.roguelikegame.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.squander.roguelikegame.Room;

public class Tile {
    public int dataID;

    public void render(SpriteBatch batch, Room room, int x, int y){
    }

    public static Tile getTileByColor(int color){
        if(color == 0xFFFFFF) return new WallTile();
        return new FloorTile();
    }
}
