package com.project.alpha.screens;

import quicktime.qd3d.camera.CameraRange;

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

public class AlphaGame implements Screen, InputProcessor {
	
	OrthogonalTiledMapRenderer renderer;
	TiledMap map;
	MapProperties properties;
	TiledMapTileLayer layer;
	OrthographicCamera camera;
	
	float[] joystickKnobDelta = new float[2];
	float[] joystickLastTouchPosition;
	Sprite joystickBackgroundSprite;
	Sprite joystickKnobSprite;
	SpriteBatch joystickSpriteBatch;
	
	
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
		
		if (Gdx.graphics.getType() == GraphicsType.iOSGL){
			//Joystick
			updateJoystickKnobPosition();
			joystickSpriteBatch.begin();
			joystickBackgroundSprite.draw(joystickSpriteBatch);
			joystickKnobSprite.draw(joystickSpriteBatch);
			joystickSpriteBatch.end();
		}
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
		Gdx.input.setInputProcessor(this);
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
		
		
		if (Gdx.graphics.getType() == GraphicsType.iOSGL){
			//Joystick links unten, 20 % Höhe und 20 % Breite
			
			//float screenHeight = Gdx.graphics.getHeight();
			float screenWidth = Gdx.graphics.getWidth();
			
			Texture joystickBackgroundTexture = new Texture(Gdx.files.internal("Joystick/Joystick_BG.png"));
			Texture joystickKnobTexture = new Texture(Gdx.files.internal("Joystick/Joystick_knob.png"));
			joystickBackgroundSprite = new Sprite(joystickBackgroundTexture);
			joystickKnobSprite = new Sprite(joystickKnobTexture);
			joystickBackgroundSprite.setBounds(screenWidth * 0.1f,screenWidth * 0.1f,screenWidth * 0.2f,screenWidth * 0.2f);
			updateJoystickKnobPosition();
			joystickSpriteBatch = new SpriteBatch();
		}
	}
	
	private void updateJoystickKnobPosition() {
		float knobSize = Gdx.graphics.getWidth() * 0.05f;
		joystickKnobSprite.setBounds(joystickBackgroundSprite.getX() + joystickBackgroundSprite.getWidth() / 2 - knobSize / 2 + joystickKnobDelta[0], joystickBackgroundSprite.getY() + joystickBackgroundSprite.getHeight() / 2 - knobSize / 2  + joystickKnobDelta[1], knobSize, knobSize);
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		joystickKnobDelta[0] = 0;
		joystickKnobDelta[1] = 0;
		joystickLastTouchPosition = new float[2];
		joystickLastTouchPosition[0] = screenX;
		joystickLastTouchPosition[1] = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		joystickKnobDelta[0] = 0;
		joystickKnobDelta[1] = 0;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		joystickKnobDelta[0] = screenX - joystickLastTouchPosition[0];
		if (joystickKnobDelta[0] > 30)
			joystickKnobDelta[0] = 30;
		else if (joystickKnobDelta[0] < -30)
			joystickKnobDelta[0] = -30;
		
		joystickKnobDelta[1] = joystickLastTouchPosition[1] - screenY;
		if (joystickKnobDelta[1] > 30)
			joystickKnobDelta[1] = 30;
		else if (joystickKnobDelta[1] < -30)
			joystickKnobDelta[1] = -30;
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
