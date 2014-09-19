package com.project.alpha.entities;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.alpha.input.InputManager;
import com.project.alpha.input.InputManager.PlayerDirection;
import com.project.alpha.screens.AlphaGame;

public class Player extends Entity {

	private static final int    COL = 4;     
    private static final int    ROW = 4;     

    TiledMapTileLayer collision;
    TiledMap		  map;
    MapProperties 		properties;
    Animation           walk;     
    Texture             walkSheet;      
    TextureRegion[]         walkFrames;     
    TextureRegion           currentFrame;      
    
    float stateTime, animTime;    
    float speed = 70, oldX, oldY;
    float mapWidth, mapHeight;
    ApplicationType type;
        
    private String blockKey = "blocked";
    
	public Player(int x, int y, TiledMap map){
		super();
		this.map = map;
		
		damage = 34;
		properties = map.getProperties();
		collision = (TiledMapTileLayer) map.getLayers().get(0);

		mapWidth = AlphaGame.getInstance().mapWidth;
		mapHeight = AlphaGame.getInstance().mapHeight;
		
		type = Gdx.app.getType();
		
		//animation stuff
		walkSheet = new Texture(Gdx.files.internal("img/Kurokami.png"));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / COL, walkSheet.getHeight() / ROW);              // #10
        walkFrames = new TextureRegion[COL * ROW];
        int index = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        
        animTime = 0.25f;
        // < == faster
        walk = new Animation(animTime, walkFrames);   
        stateTime = 0;
        currentFrame = walk.getKeyFrame(0);
        //setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setSize(collision.getTileWidth(), collision.getTileHeight());
        setX(x);
        setY(y);
        setRegion(currentFrame);
        
	}
	
	public void render(SpriteBatch batch){
		super.draw(batch);
	}
	
	@Override
	public void draw(Batch batch) {	super.draw(batch);	}

	public void update(float delta) {
		stateTime += delta / animTime;
		if(stateTime > COL)
			stateTime = 0;
		
		oldX = getX();
		oldY = getY();
		
		handleControl();
		
		//System.out.println("X: " + getX() + ", Y: " + getY());
	    setRegion(currentFrame);
	    
	    if(collisionX(getX(), getY(), getWidth(), getHeight()) || isOutOfBoundsX(getX(), getWidth()))
	    	setX(oldX);
	    if(collisionY(getX(), getY(), getWidth(), getHeight()) || isOutOfBoundsY(getY(), getHeight()))
	    	setY(oldY);
	}

	public boolean isOutOfBoundsX(float x, float width) {
		return (x < 0 || x + width > mapWidth);
	}

	public boolean isOutOfBoundsY(float y, float height) {
		return (y < 0 || y + height > mapHeight);
	}
	
	private void handleControl() {
		
		float delta = Gdx.graphics.getDeltaTime();
		
		PlayerDirection direction = InputManager.sharedInstance().getPlayDirection();
		
		final float diagonalFactor = (float) (1 / Math.sqrt(2));
		
		if (direction == PlayerDirection.Up) {
			setY(getY() + speed * delta);
			currentFrame = walk.getKeyFrame((12 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.Down) {
			setY(getY() - speed * delta);		
			currentFrame = walk.getKeyFrame((stateTime) * animTime);
		}
		else if (direction == PlayerDirection.Left) {
			setX(getX() - speed * delta);
			currentFrame = walk.getKeyFrame((4 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.Right) {
			setX(getX() + speed * delta);
			currentFrame = walk.getKeyFrame((8 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.UpRight) {
			setY(getY() + speed * delta * diagonalFactor);
			setX(getX() + speed * delta * diagonalFactor);
			currentFrame = walk.getKeyFrame((8 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.UpLeft) {
			setY(getY() + speed * delta * diagonalFactor);
			setX(getX() - speed * delta * diagonalFactor);
			currentFrame = walk.getKeyFrame((4 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.DownRight) {
			setY(getY() - speed * delta * diagonalFactor);
			setX(getX() + speed * delta * diagonalFactor);
			currentFrame = walk.getKeyFrame((8 + stateTime) * animTime);
		}
		else if (direction == PlayerDirection.DownLeft) {
			setY(getY() - speed * delta * diagonalFactor);
			setX(getX() - speed * delta * diagonalFactor);
			currentFrame = walk.getKeyFrame((4 + stateTime) * animTime);
		}
		
	}

	public boolean collisionX(float x, float y, float width, float height) {
		
		/////left tiles
		if(isBlocked(x, y) ||
			isBlocked(x, y + height / 2) ||
			isBlocked(x, y + height))
		{
				return true;
		}
		
		/////right tiles
		else if(isBlocked(x + width, y) ||
				isBlocked(x + width, y + height / 2) ||
				isBlocked(x + width, y + height))
			{
					return true;
			}
		return false;
	}
	
	public boolean collisionY(float x, float y, float width, float height){
		/////up tiles
		if(isBlocked(x , y + height) ||
			isBlocked(x + width / 2, y + height) ||
			isBlocked(x + width, y + height))
		{
			return true;
		}
		
		/////bottom tiles
		else if(isBlocked(x, y) ||
				isBlocked(x + width / 2, y) ||
				isBlocked(x + width, y))
		{
			return true;
		}
		
		return false;
	}
	
	private boolean isBlocked(float x, float y){
		Cell cell = collision.getCell((int) (x / collision.getTileWidth()), (int) (y / collision.getTileHeight()));
		return cell != null && cell.getTile().getProperties().containsKey(blockKey);
	}
	
}
