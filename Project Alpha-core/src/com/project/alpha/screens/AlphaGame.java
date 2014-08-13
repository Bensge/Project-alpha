package com.project.alpha.screens;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.project.alpha.Main;
import com.project.alpha.entities.Bullet;
import com.project.alpha.entities.Enemy;
import com.project.alpha.entities.Entity;
import com.project.alpha.entities.Player;
import com.project.alpha.entities.Zombie;
import com.project.alpha.input.InputManager;
import com.project.alpha.input.Joystick;

public class AlphaGame implements Screen {
	
	OrthogonalTiledMapRenderer renderer;
	TiledMap map;
	MapProperties properties;
	TiledMapTileLayer layer;
	OrthographicCamera camera;
	
	ArrayList<Enemy> enemies;
	ArrayList<Bullet> bullets;
	
	public float mapWidth;
	public float mapHeight;
	public float tileWidth;
	public float tileHeight;
	public float spawnTime = 3000;
	private long timeSinceSpawn;
	Player player;
	Joystick joystick;
	
	public static AlphaGame instance = null;
	
	public AlphaGame(){	}
	
	@Override
	public void show() {
		
		//initialize entities
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		
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
		
		//draw enenmies
		for(Enemy e : enemies)
			e.draw(renderer.getSpriteBatch());
		
		//draw bullets
		for(Bullet b : bullets){
			b.draw(renderer.getSpriteBatch());
		}
		
		renderer.getSpriteBatch().end();
		
		//draw enenmies life
		for(Enemy e : enemies)
			e.renderStuff();
		
		if (Joystick.joystickSupported())
			joystick.render(null);
	}

	private void update(float delta) {
		player.update(delta);
		
		//update bullets
		Iterator<Bullet> it = bullets.iterator();
		while(it.hasNext()){
			Bullet b = it.next();
			b.update(delta);
			if(player.collisionX(b.getX(), b.getY()) || player.collisionY(b.getX(), b.getY()) || player.isOutOfBoundsX(b.getX(), b.getWidth()) || player.isOutOfBoundsY(b.getY(), b.getHeight())){
				it.remove();
			}		
		}
		
		if (InputManager.sharedInstance().getShouldShoot()){
			bullets.add(new Bullet(player.getX(), player.getY(), InputManager.sharedInstance().getShootDirection()));
		}
		
		//update enemies
		for(Enemy e : enemies){
			e.update(player.getX(), player.getY(), delta);
		}
		
		//update bullet & enemie collision
		
		if(!enemies.isEmpty() && !bullets.isEmpty()){
			for(int i = 0; i < bullets.size(); i++){
				for(int j = 0; j < enemies.size(); j++){
					if(collidesWith(bullets.get(i), enemies.get(j))){
						bullets.remove(i);
						//enemies.get(j).gotDamaged();
						enemies.remove(j);
						System.out.println("enemy removed");
						break;
					}
				}
			}
		}
		
		
		if(System.currentTimeMillis() - timeSinceSpawn >= spawnTime){
			addEnemy(new Zombie());
			timeSinceSpawn = System.currentTimeMillis();
		}		
		
		cameraBounds();
	}

	@Override
	public void resize(int width, int height) {
		camera.position.set(800, 600, 0);
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
	
	public void add(Entity e){
		if(e instanceof Bullet){
		//	bullets.add(new Bullet());
			}
		else if(e instanceof Player){
			
		}
		//else if(e instanceof )	add more stuff here
	}
	
	public void addEnemy(Enemy e){
		if(e instanceof Zombie){
			enemies.add(e);
		}
	}
	
	public boolean collidesWith(Entity a, Entity b){
		// collision between two entities
		
		float wa = a.getWidth(), wb = b.getWidth();
		float ha = a.getWidth(), hb = b.getWidth();
		float xa = a.getX(), xb = b.getX();
		float ya = a.getY(), yb = b.getY();
		
		Rectangle rectOne = new Rectangle(xa, ya, wa, ha);
		Rectangle rectTwo = new Rectangle(xb, yb, wb, hb);
		
		return Intersector.overlaps(rectOne, rectTwo);
	
	}
	
	public static AlphaGame getInstance(){	return instance;	}
	
	@Override
	public void hide() {	}

	@Override
	public void pause() {	}

	@Override
	public void resume() {	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		Main.sharedInstance().screenWantsDismissal(this);
		player.getTexture().dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
}
