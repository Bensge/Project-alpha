package com.project.alpha.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.project.alpha.Objects.Bullet;
import com.project.alpha.input.InputManager;
import com.project.alpha.input.InputManager.PlayerDirection;

public class Player extends Sprite {

	private static final int    COL = 4;     
    private static final int    ROW = 4;     

    TiledMapTileLayer collision;
    Animation           walk;     
    Texture             walkSheet;      
    TextureRegion[]         walkFrames;     
    TextureRegion           currentFrame;      
    
    float stateTime, animTime;    
    float speed = 100, oldX, oldY;
    ApplicationType type;
    
    private ArrayList<Bullet> bullets;
    
    private String blockKey = "blocked";
    
	public Player(int x, int y, TiledMapTileLayer collision){
		this.collision = collision;
		
		type = Gdx.app.getType();
		
		bullets = new ArrayList<Bullet>();
		
		//animation stuff
		walkSheet = new Texture(Gdx.files.internal("img/player.png"));
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
        setSize(currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        setSize(collision.getTileWidth(), collision.getTileHeight());
        setX(x);
        setY(y);
        setRegion(currentFrame);
        
	}
	
	public void render(SpriteBatch batch){
		super.draw(batch);
		
		
	}
	
	@Override
	public void draw(Batch batch) {
		
		for(Bullet b : bullets){
			b.draw(batch);
		}
		
		super.draw(batch);
	}

	public void update(float delta) {
		stateTime += delta / animTime;
		if(stateTime > COL)
			stateTime = 0;
		
		//System.out.println(Gdx.graphics.getFramesPerSecond() + "jf");
		
		oldX = getX();
		oldY = getY();
		
		handleControl();
		
		for(Bullet b : bullets){
			b.update(delta);
			//System.out.println("ius");
		}
		
	    setRegion(currentFrame);
	   // System.out.println(getWidth()+", "+ getHeight());
	    collision();
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
		
		if(InputManager.sharedInstance().getShouldShoot()){
			bullets.add(new Bullet(getX(), getY(), direction));
		}
	}

	private void collision() {
		/////up tiles
		if(isBlocked(getX() , getY() + getHeight()) ||
			isBlocked(getX() + getWidth() / 2, getY() + getHeight()) ||
			isBlocked(getX() + getWidth(), getY() + getHeight()))
		{
			setY(oldY);
		}
		
		/////bottom tiles
		else if(isBlocked(getX(), getY()) ||
				isBlocked(getX() + getWidth() / 2, getY()) ||
				isBlocked(getX() + getWidth(), getY()))
		{
			setY(oldY);
		}
		
		/////left tiles
		if(isBlocked(getX(), getY()) ||
			isBlocked(getX(), getY() + getHeight() / 2) ||
			isBlocked(getX(), getY() + getHeight()))
		{
				setX(oldX);
		}
		
		/////right tiles
		else if(isBlocked(getX() + getWidth(), getY()) ||
				isBlocked(getX() + getWidth(), getY() + getHeight() / 2) ||
				isBlocked(getX() + getWidth(), getY() + getHeight()))
			{
					setX(oldX);
			}
	}
	
	private boolean isBlocked(float x, float y){
		Cell cell = collision.getCell((int) (x / collision.getTileWidth()), (int) (y / collision.getTileHeight()));
		return cell != null && cell.getTile().getProperties().containsKey(blockKey);
	}
}
