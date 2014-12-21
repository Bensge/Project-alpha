package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.project.CharacterControllers.CharacterController;
import com.project.CharacterControllers.ComputerDemoController;
import com.project.CharacterControllers.UserDesktopController;
import com.project.CharacterControllers.UserMobileController;
import com.project.Entities.Player;
import com.project.constants.Constants;

public class GameWorld extends GameState {
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private Player player;
	
	//FPS counter
	private BitmapFont fpsFont;
	private SpriteBatch fpsBatch;
	private final float fpsPadding = 7;
	private String fpsString;
	private long fpsTimestamp = 0;
	private final int FPS_UPDATES_PER_SECOND = 10;
	
	private float SCALING_FACTOR;
	
	//Parallax effect
	private SpriteBatch backgroundBatch;
	private Sprite backgroundSprite;

	public GameWorld(GameStateManager manager) {
		super(manager);
		
		//new MultiplayerController("10.32.116.6", 80);
		
		SCALING_FACTOR = Constants.SCREEN_SCALING_FACTOR;
		System.out.println("Screen scaling factor: " + SCALING_FACTOR);
		
		//Camera
		camera = new OrthographicCamera();
		camera.viewportWidth = Gdx.graphics.getWidth() / SCALING_FACTOR;
		camera.viewportHeight = Gdx.graphics.getHeight() / SCALING_FACTOR;
		
		//Map
		map = new TmxMapLoader().load("maps/map.tmx");
		
		//Renderer
		renderer = new OrthogonalTiledMapRenderer(map);
		
		//Player
		player = new Player(24 * 16, 39 * 16, map, camera);
		
		//FPS
		fpsFont = Constants.menlo32Font;
		fpsFont.setColor(Color.RED);
		
		fpsBatch = new SpriteBatch();
		
		//Parallax
		backgroundBatch = new SpriteBatch();
		backgroundSprite = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("img/parallax_background.jpg")),1260,800,900,700));
		if (!(SCALING_FACTOR > 0.99f && SCALING_FACTOR < 1.01f))
			backgroundSprite.scale(SCALING_FACTOR);
	}
	
	public void setIsInBackground(boolean background)
	{
		if (background != isInBackground)
		{
			//The background state actually changed
			Class <? extends CharacterController> userControllerClass = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer) ? UserMobileController.class : UserDesktopController.class;
			
			Class<? extends CharacterController> controllerClass = background ? ComputerDemoController.class : userControllerClass;
			try {
				player.controller = (CharacterController)controllerClass.getConstructors()[0].newInstance(player);
			} catch (Exception e)
			{
				System.out.println("Ouch, creating CharacterController failed");
				e.printStackTrace();
			}
		}
		
		super.setIsInBackground(background);
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
	public void render(SpriteBatch b)
	{	
		super.render(b);
		
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
		
		camera.position.set((int)cX, (int)cY, 0);
		//System.out.println("camera-x: " + camera.position.x);
		camera.update();
		renderer.setView(camera);
		
		//Draw parallax background
		float xConstant = Gdx.graphics.getWidth();
		float yConstant = Gdx.graphics.getHeight();
		
		float xVariable = backgroundSprite.getWidth() * SCALING_FACTOR - xConstant;
		float yVariable = backgroundSprite.getHeight() * SCALING_FACTOR - yConstant;
		
		float xPercent = (cX - camera.viewportWidth / 2)/(layer.getWidth()*layer.getTileWidth() - camera.viewportWidth);
		float yPercent = (cY - camera.viewportHeight / 2)/(layer.getHeight()*layer.getTileHeight() - camera.viewportHeight);
		
		backgroundBatch.begin();
		backgroundSprite.setPosition(-xVariable * xPercent,-yVariable * yPercent);
		backgroundSprite.draw(backgroundBatch);
		backgroundBatch.end();
		
		//Draw map
		renderer.render();
		
		//Draw player
		Method getBatchMethod = null;
		try
		{
			getBatchMethod = ClassReflection.getMethod(renderer.getClass(), "getBatch", new Class[0]);
		}
		catch (ReflectionException exc)
		{
			try
			{
				getBatchMethod = ClassReflection.getMethod(renderer.getClass(), "getSpriteBatch", new Class[0]);
			}
			catch (ReflectionException e)
			{
				System.out.println("Ugh...");
				e.printStackTrace();
			}
		}
		
		Batch batch = null;
		try
		{
			batch = (Batch) getBatchMethod.invoke(renderer, (Object[])null);
		} catch (ReflectionException e)
		{
			System.out.println("Damn.");
			e.printStackTrace();
		}
		
		batch.begin();
		
		player.render(batch);
		
		batch.end();
		
		//FPS counter
		fpsBatch.begin();
		String str = fpsString != null ? fpsString : (Gdx.graphics.getFramesPerSecond() + "");
		TextBounds bounds = fpsFont.getBounds(str);
		fpsFont.draw(fpsBatch,str,Gdx.graphics.getWidth() - bounds.width - fpsPadding, Gdx.graphics.getHeight() - fpsPadding);
		fpsBatch.end();
	}

	@Override
	public void dispose() {
		//The game world is never disposed, idiot!
	}

}
