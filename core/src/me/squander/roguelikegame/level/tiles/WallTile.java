package me.squander.roguelikegame.level.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.squander.roguelikegame.Assets;
import me.squander.roguelikegame.Room;

public class WallTile extends Tile {

    @Override
    public void render(SpriteBatch batch, Room room, int x, int y) {
        batch.setColor(1, 1, 1, 1);
        batch.begin();
        batch.draw(Assets.tiles, x * 16, y * 16, 32, 0, 16, 16);
        batch.end();
    }
}
