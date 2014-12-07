package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameWorld extends GameState {
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	//private Player player;

	public GameWorld(GameStateManager manager) {
		super(manager);
		
		camera = new OrthographicCamera();
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		map = new TmxMapLoader().load("maps/map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);
		System.out.println("camera: " + camera + " map: " + map + " renderer: " + renderer);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch b) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(20, 20, 0);
		//camera.setToOrtho(true);
		//camera.position.set(player.getX(), player.getY(), 0);
		
		camera.update();
		renderer.setView(camera);
		
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		
		//player.draw(renderer.getSpriteBatch());
		
		renderer.getSpriteBatch().end();
		
	}

}
