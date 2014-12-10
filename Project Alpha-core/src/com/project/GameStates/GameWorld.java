package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.project.Entities.Player;

public class GameWorld extends GameState {
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private Player player;
	private BitmapFont fpsFont;
	private SpriteBatch fpsBatch;
	private final float fpsPadding = 10;
	private String fpsString;

	public GameWorld(GameStateManager manager) {
		super(manager);
		
		//Camera
		camera = new OrthographicCamera();
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		
		//Map
		map = new TmxMapLoader().load("maps/map.tmx");
		
		//Renderer
		renderer = new OrthogonalTiledMapRenderer(map);
		
		//Player
		player = new Player(10, 10, map);

		//Logging
		System.out.println("camera: " + camera + " map: " + map + " renderer: " + renderer);
		
		fpsFont = new BitmapFont();
		fpsFont.setColor(Color.RED);
		fpsFont.setScale(2);
		
		fpsBatch = new SpriteBatch();
	}

	@Override
	public void update(float delta) {
		player.update(delta);
		
		fpsString = (int)(1.f / delta) + "";
	}

	@Override
	public void render(SpriteBatch b) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Get player position
		float pX = player.getX();
		float pY = player.getY();
		//Limit camera position (left, bottom)
		float cX = Math.max(pX, camera.viewportWidth / 2);
		float cY = Math.max(pY, camera.viewportHeight / 2);
		//Limit camera position (right, top)
		TiledMapTileLayer layer = ((TiledMapTileLayer)map.getLayers().get(0));
		cX = Math.min(cX, layer.getWidth() * layer.getTileWidth() - camera.viewportWidth / 2);
		cY = Math.min(cY, layer.getHeight() * layer.getTileHeight() - camera.viewportHeight / 2);
		
		camera.position.set(cX, cY, 0);
		
		camera.update();
		renderer.setView(camera);
		
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		
		player.draw(renderer.getSpriteBatch());
		
		renderer.getSpriteBatch().end();
		
		//FPS counter
		fpsBatch.begin();
		String str = fpsString != null ? fpsString : (Gdx.graphics.getFramesPerSecond() + "");
		TextBounds bounds = fpsFont.getBounds(str);
		fpsFont.draw(fpsBatch,str,Gdx.graphics.getWidth() - bounds.width - fpsPadding, Gdx.graphics.getHeight() - fpsPadding);
		fpsBatch.end();
		
		
	}

}
