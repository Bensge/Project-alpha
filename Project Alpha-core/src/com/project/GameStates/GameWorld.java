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
import com.badlogic.gdx.utils.TimeUtils;
import com.project.Entities.Player;

public class GameWorld extends GameState {
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private Player player;
	private BitmapFont fpsFont;
	private SpriteBatch fpsBatch;
	private final float fpsPadding = 7;
	private String fpsString;
	private long fpsTimestamp = 0;
	private final int FPS_UPDATES_PER_SECOND = 10;

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
		player = new Player(24 * 16, 39 * 16, map);

		//Logging
		System.out.println("camera: " + camera + " map: " + map + " renderer: " + renderer);
		
		fpsFont = new BitmapFont(Gdx.files.internal("fonts/Menlo-32.fnt"),Gdx.files.internal("fonts/Menlo.png"), false);
		fpsFont.setColor(Color.RED);
		
		fpsBatch = new SpriteBatch();
	}

	@Override
	public void update(float delta) {
		player.update(delta);
		
		long time;
		if ((time = TimeUtils.nanoTime()) - fpsTimestamp > 1000*1000*1000 / FPS_UPDATES_PER_SECOND)
		{
			fpsString = (int)(1.f / delta) + "";
			fpsTimestamp = time;
		}
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
