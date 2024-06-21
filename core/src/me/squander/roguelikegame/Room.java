package me.squander.roguelikegame;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.squander.roguelikegame.level.tiles.Tile;

public class Room {
    private final RoomType roomType;
    private Tile[] tiles;
    private int width;
    private int height;

    public Room(RoomType roomType){
        this.roomType = roomType;
        this.loadRoom();
    }

    public Tile getTile(int x, int y){
        if(x < 0 || x >= this.width || y < 0 || y >= this.height) return new Tile();
        return tiles[x + y * this.width];
    }

    public void setTile(Tile tile, int x, int y){
        if(x < 0 || x >= this.width || y < 0 || y >= this.height) return;
        tiles[x + y * this.width] = tile;
    }

    public RoomType getRoomType(){
        return this.roomType;
    }

    public void renderPixel(SpriteBatch batch, ShapeRenderer shapeRenderer, int xRoom, int yRoom) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                tiles[x + y * width].render(batch, this, x + xRoom * 16, y + yRoom * 16);
            }
        }
    }

    private void loadRoom(){
        Texture roomTexture = this.roomType.getRoomTexture();
        roomTexture.getTextureData().prepare();
        this.width = roomTexture.getWidth();
        this.height = roomTexture.getHeight();
        this.tiles = new Tile[this.width * this.height];

        Pixmap pixmap = roomTexture.getTextureData().consumePixmap();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int yInverted = this.height - y - 1;
                int pixel = pixmap.getPixel(x, yInverted);

                int color = (pixel >> 8) & 0xffffff;
                int alpha = pixel & 0xff;

                Tile tile = Tile.getTileByColor(color);
                tile.dataID = alpha;
                tiles[x + y * this.width] = tile;
            }
        }

        pixmap.dispose();
    }
}
