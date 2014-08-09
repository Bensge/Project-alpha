package com.project.alpha.screens;


import com.badlogic.gdx.Graphics.GraphicsType;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.project.alpha.Main;
import com.project.alpha.entities.Player;

public class AlphaGame implements Screen {
	
	OrthogonalTiledMapRenderer renderer;
	TiledMap map;
	MapProperties properties;
	TiledMapTileLayer layer;
	OrthographicCamera camera;
	
	
	private float mapWidth, mapHeight, tileWidth, tileHeight;
	Player player;
	
	public AlphaGame(){
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			System.out.println("escaped");
			dispose();
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
		renderer.getSpriteBatch().end();	
	}

	private void update(float delta) {
		player.update(delta);
		cameraBounds();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 3;
		camera.viewportHeight = height / 3;
		camera.update();
	}

	@Override
	public void show() {
		
		camera = new OrthographicCamera();
		map = new TmxMapLoader().load("maps/map.tmx");
		
		layer = (TiledMapTileLayer) map.getLayers().get(0);
		renderer = new OrthogonalTiledMapRenderer(map);
		
		player = new Player(10, 160, layer);
		
		properties = map.getProperties();
		tileWidth = (Integer) properties.get("tilewidth");
		tileHeight = (Integer) properties.get("tileheight");
		
		mapWidth = tileWidth * (Integer) properties.get("width");
		mapHeight = tileHeight * (Integer) properties.get("height");
		
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
