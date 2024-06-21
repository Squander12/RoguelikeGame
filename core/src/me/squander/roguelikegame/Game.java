package me.squander.roguelikegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.squander.roguelikegame.level.Level;

public class Game extends ApplicationAdapter {
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private Viewport gameViewport;
	private Level level;

	@Override
	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float aspectRatio = (height / width);
		float view = 380;
		camera = new OrthographicCamera();
		gameViewport = new ExtendViewport(view, view * aspectRatio, camera);
		gameViewport.apply();
		camera.zoom = 16;

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();

		level = new Level();
	}

	public void update(){
		if(Gdx.input.isButtonJustPressed(0)){
			Vector3 v = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			v.x = MathUtils.floor(v.x / 16 / 16);
			v.y = MathUtils.floor(v.y / 16 / 16);
			System.out.println(v.x + " " + v.y);
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
			for (int i = 0; i < 25; i++) {
				System.out.println();
			}

			level = new Level();
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W)) camera.position.y += 1 * camera.zoom;
		if(Gdx.input.isKeyPressed(Input.Keys.S)) camera.position.y -= 1 * camera.zoom;
		if(Gdx.input.isKeyPressed(Input.Keys.A)) camera.position.x -= 1 * camera.zoom;
		if(Gdx.input.isKeyPressed(Input.Keys.D)) camera.position.x += 1 * camera.zoom;

		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
			camera.zoom--;
			if(camera.zoom < 1) camera.zoom = 1;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
			camera.zoom++;
			if(camera.zoom > 16) camera.zoom = 16;
		}

		camera.position.x = MathUtils.round(camera.position.x);
		camera.position.y = MathUtils.round(camera.position.y);
		camera.update();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		this.update();

		shapeRenderer.setProjectionMatrix(gameViewport.getCamera().combined);
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		level.render(batch, shapeRenderer);
	}

	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height);
		gameViewport.apply();
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		batch.dispose();
	}
}
