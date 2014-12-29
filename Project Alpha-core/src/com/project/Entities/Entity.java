package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
	protected int life;
	
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
	
	public Entity(Texture texture) {
		super(texture);
	}
	public Entity(Texture texture, int width, int height)
	{
		super(texture,width,height);
	}

	private void init(){
		life = 100;
		velocity = new Vector2();
		
		canJump = false;
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
		mapWidth = collisionLayer.getWidth() * collisionLayer.getTileWidth();
		mapHeight = collisionLayer.getHeight() * collisionLayer.getTileHeight();
	}
	
	public void render(Batch b){
		super.draw(b);
	}
	
	public boolean isOutOfBoundsX(float x, float width) {
		return (x < 0 || x + width > mapWidth);
	}

	public boolean isOutOfBoundsY(float y, float height) {
		return (y < 0 || y + height > mapHeight);
	}
	
	public boolean isOutOfBoundsYDown(float y, float height){
		return y < 0;
	}
	
	public abstract void update(float delta);
	
	public boolean collisionXLeft(float x, float y, float height) {
		/////left tiles
		if(isBlocked(x, y + 2) ||
			isBlocked(x, y + height - 2) ||
			(x < 0)
			)
		{
				return true;
		}
		return false;
	}
		
	public boolean collisionXRight(float x, float y, float width, float height){
		/////right tiles
		if(isBlocked(x + width, y + 2) ||
			isBlocked(x + width, y + height - 2) ||
			(x + width > mapWidth))
			{
					return true;
			}
		return false;
	}
	
	public boolean collisionYDown(float x, float y, float width){
		/////bottom tiles
		if(isBlocked(x + 0.1f, y) ||
			isBlocked(x - 0.1f + width, y) ||
			y < 0)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean collisionYUp(float x, float y, float width, float height){
		/////up tiles
		if(isBlocked(x , y + height) ||
			//isBlocked(x + getWidth() / 2, y + getHeight()) ||
			isBlocked(x + width, y + height))
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
