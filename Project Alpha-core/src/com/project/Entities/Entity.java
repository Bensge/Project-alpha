package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;


public abstract class Entity extends Sprite {
	
	protected TiledMapTileLayer collisionLayer;
	protected TiledMap		  map;
	
	protected final String blockKey = "blocked";
	
	protected float gravity, XSpeed, jump, maxSpeed;
	protected boolean canJump, collisionX, collisionY;
	protected float mapWidth, mapHeight;
	protected Vector2 velocity;
	protected int damage;
	
	public Entity(TiledMap map) {
		this.map = map;
		
		init();
	}
	
	public Entity(String s, TiledMap map){
		super(new Sprite(new Texture(Gdx.files.internal(s))));
		
		this.map = map;
		setSize(getTexture().getWidth(), getTexture().getHeight());
		
		init();
	}
	
	private void init(){
		damage = 0;
		velocity = new Vector2();
		
		canJump = false;
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
		mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
		mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();
	}
	
	public void render(SpriteBatch b){
		super.draw(b);
	}
	
	public boolean isOutOfBoundsX(float x, float width) {
		return (x < 0 || x + width > mapWidth);
	}

	public boolean isOutOfBoundsY(float y, float height) {
		return (y < 0 || y + height > mapHeight);
	}
	
	public abstract void update(float delta);
	
	public boolean collisionX(float x, float y) {
		
		/////left tiles
		if(isBlocked(x, y) ||
			isBlocked(x, y + getHeight() / 2) ||
			isBlocked(x, y + getHeight()))
		{
				return true;
		}
		
		/////right tiles
		else if(isBlocked(x + getWidth(), y) ||
				isBlocked(x + getWidth(), y + getHeight() / 2) ||
				isBlocked(x + getWidth(), y + getHeight()))
			{
					return true;
			}
		return false;
	}
	
	public boolean collisionY(float x, float y){
		/////up tiles
		if(isBlocked(x , y + getHeight()) ||
			isBlocked(x + getWidth() / 2, y + getHeight()) ||
			isBlocked(x + getWidth(), y + getHeight()))
		{
			return true;
		}
		
		/////bottom tiles
		else if(isBlocked(x, y) ||
				isBlocked(x + getWidth() / 2, y) ||
				isBlocked(x + getWidth(), y))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean isBlocked(float x, float y){
		Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile().getProperties().containsKey(blockKey);
	}
}
