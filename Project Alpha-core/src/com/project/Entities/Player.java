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
		
		//Get old values
		oldX = getX();
		oldY = getY();
		
		//Handle gravity
		velocity.y -= gravity * delta;
		
		//Handle left/right keyboard inputs
		if (Gdx.input.isKeyPressed(Keys.A))
			velocity.x -= XSpeed;
		else if (Gdx.input.isKeyPressed(Keys.D))
			velocity.x += XSpeed;
		else
			decreaseXVelocity();
		
		//Limit player speed
		if (velocity.x > maxSpeed)
			velocity.x = maxSpeed;
		else if (velocity.x < -maxSpeed)
			velocity.x = -maxSpeed;
		
		//Handle jump keyboard input
		if (Gdx.input.isKeyPressed(Keys.W) && canJump){
			velocity.y = jump; //use += instead!?
			canJump = false;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)){
			//go down a level
		}

		//Handle jump states
		
		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
		
		/*
		** check object collision
		*/
		
		//y-axis collision
		if(collisionYUp(getX(), getY())){
			velocity.y = 0;
			
			setY(oldY);
		}
		else if(collisionYDown(getX(), getY())){
			canJump = true;
			velocity.y = -velocity.y * 0.1f;
			setY(oldY);
		}
		
		//x-axis collision
		if(collisionXLeft(getX(), getY())){
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
		}
		else if(collisionXRight(getX(), getY())){
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
		}
	}
	
	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}

	
	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}

}
