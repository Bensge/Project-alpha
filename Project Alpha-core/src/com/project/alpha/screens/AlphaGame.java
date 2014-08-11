package com.project.alpha.screens;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.project.alpha.Main;
import com.project.alpha.entities.Enemy;
import com.project.alpha.entities.Player;
import com.project.alpha.input.Joystick;

public class AlphaGame implements Screen {
	
	OrthogonalTiledMapRenderer renderer;
	TiledMap map;
	MapProperties properties;
	TiledMapTileLayer layer;
	OrthographicCamera camera;
	
	ArrayList<Enemy> enemys;
	
	public float mapWidth;
	public float mapHeight;
	public float tileWidth;
	public float tileHeight;
	public float spawnTime = 3000;
	private long timeSinceSpawn;
	Player player;
	Joystick joystick;
	
	public static AlphaGame instance = null;
	long l;
	public AlphaGame(){	}
	
	@Override
	public void show() {
		
		enemys = new ArrayList<Enemy>();
		
		camera = new OrthographicCamera();
		//camera.setToOrtho(true);
		map = new TmxMapLoader().load("maps/map.tmx");
		
		layer = (TiledMapTileLayer) map.getLayers().get(0);
		renderer = new OrthogonalTiledMapRenderer(map);
		
		if (Joystick.joystickSupported())
			joystick = new Joystick();
		
		properties = map.getProperties();
		tileWidth = (Integer) properties.get("tilewidth");
		tileHeight = (Integer) properties.get("tileheight");
		
		mapWidth = tileWidth * (Integer) properties.get("width");
		mapHeight = tileHeight * (Integer) properties.get("height");
		
		instance = this;
		
		player = new Player(10, 160, map);
		
		timeSinceSpawn = System.currentTimeMillis();
		l = timeSinceSpawn;
	}
	
	@Override
	public void render(float delta) {
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			System.out.println("escaped");
			hide();
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(player.getX(), player.getY(), 0);
		update(delta);
		
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
		for(Enemy e : enemys)
			e.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		
		if (Joystick.joystickSupported())
			joystick.render(null);
	}

	private void update(float delta) {
		player.update(delta);
		
		for(Enemy e : enemys){
			e.update(player.getX(), player.getY(), delta);
		}
		
		if(System.currentTimeMillis() - timeSinceSpawn >= spawnTime && enemys.size() <= 1){
			enemys.add(new Enemy());
			timeSinceSpawn = System.currentTimeMillis();
		}
		
		cameraBounds();
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(800, 800 * (width / height), 0);
		camera.viewportWidth = width / 5;
		camera.viewportHeight = width / 5 * (width / height);
		camera.update();
	}

	private void cameraBounds() {
		
		float cameraHalfWidth = camera.viewportWidth * .5f;
		float cameraHalfHeight = camera.viewportHeight * .5f;

		float cameraLeft = camera.position.x - cameraHalfWidth;
		float cameraRight = camera.position.x + cameraHalfWidth;
		float cameraBottom = camera.position.y - cameraHalfHeight;
		float cameraTop = camera.position.y + cameraHalfHeight;

		if(cameraLeft <= 0)
		{
		    camera.position.x = 0 + cameraHalfWidth;
		}
		else if(cameraRight >= mapWidth)
		{
			camera.position.x = mapWidth - cameraHalfWidth;
		}

		if(cameraBottom <= 0)
		{
		    camera.position.y = 0 + cameraHalfHeight;
		}
		else if(cameraTop >= mapHeight)
		{
		    camera.position.y = mapHeight - cameraHalfHeight;
		}
	}
	
	public static AlphaGame getInstance(){
		return instance;
	}
	
	
	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {
	
	}

	@Override
	public void dispose() {
		map.dispose();
		Main.sharedInstance().screenWantsDismissal(this);
	}
}
