package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Player extends Entity {

	float oldX, oldY;
	
	public Player(int x, int y, TiledMap map) {
		super("img/enemy.png", map);
		
		gravity = 1000;
		XSpeed = 10;
		maxSpeed = 250;
		
		setX(x);
		setY(y);
		
		canJump = true;
		jump = 450;
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
		
		
		
		if(velocity.x > maxSpeed )
			velocity.x = maxSpeed;
		else if(velocity.x < -maxSpeed)
			velocity.x = -maxSpeed;
		
		if(Gdx.input.isKeyPressed(Keys.W) && canJump){
			velocity.y += jump;
			canJump = false;
		}
		
		else if(Gdx.input.isKeyPressed(Keys.S)){
			//go down a level

		}

		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
		
		if(getY() <= 0){
			canJump = true;
			setY(0);
			velocity.y = 0;
		}
		
		//check out of map
		if(isOutOfBoundsX(getX(), getWidth())){
			setX(oldX);
			velocity.x = -velocity.x * 0.3f;
		}
		
		if(isOutOfBoundsY(getY(), getWidth())){
			setY(oldY);
			velocity.y = -velocity.y * 0.3f;
		}
		
		System.out.println(oldX - getX());
		
		//check object collision
		if(collisionX(getX(), getY())){
			velocity.x = 0;
			setX(oldX);
		}
		
		
		
		if(collisionY(getX(), getY())){
			setY(oldY);
			velocity.y = 0;
			canJump = true;
		}
		
		//System.out.println("posX: " + getX());
	}
	
	private void decreaseXVelocity() {	velocity.x /= 1.1f;	}

	
	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}

}
