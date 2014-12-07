package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.Entities.Player;

public class GameWorld extends GameState {
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private Player player;

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
		player = new Player(10, 10);
		
		//Logging
		System.out.println("camera: " + camera + " map: " + map + " renderer: " + renderer);
	}

	@Override
	public void update(float delta) {
		player.update(delta);
	}

	@Override
	public void render(SpriteBatch b) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(20, 20, 0);
		
		camera.update();
		renderer.setView(camera);
		
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		
		player.draw(renderer.getSpriteBatch());
		//player.draw(renderer.getSpriteBatch());
		
		renderer.getSpriteBatch().end();
		
	}

}
