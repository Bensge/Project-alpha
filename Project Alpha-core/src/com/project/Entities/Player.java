package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Player extends Entity {

	float oldX, oldY;
	
	public Player(int x, int y, TiledMap map) {
		super("img/enemy.png", map);
		
		gravity = 70;
		XSpeed = 20;
		
		setX(x);
		setY(y);
		
		jump = 30;
	}
	
	@Override
	public void update(float delta) {
		
		oldX = getX();
		oldY = getY();
		
		//velocity.x = 0;
		velocity.y -= gravity * delta;
		
		if(Gdx.input.isKeyPressed(Keys.A))
			velocity.x -= XSpeed;
		else if(Gdx.input.isKeyPressed(Keys.D))
			velocity.x += XSpeed;
		
		else{decreaseXVelocity();};
		
		if(Gdx.input.isKeyPressed(Keys.W) && canJump){
			velocity.y += jump;
			canJump = false;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)){
			//go down a level
			
		}
			
		System.out.println(canJump);
		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
		
		if(getY() == 0){
			canJump = true;
		}
		
		
		if(getY() < 0){
			setY(0);
			velocity.y = 0;
		}
		if(getX() < 0){
			setX(0);
			velocity.x = -velocity.x * 0.3f;
		}
	}
	
	private void decreaseXVelocity() {
		if(velocity.x < 0)
			velocity.x += 1;
		else if(velocity.x > 0)
			velocity.x -= 1;
	}

	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}

}
