package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.project.CharacterControllers.Character;
import com.project.CharacterControllers.CharacterController;
import com.project.CharacterControllers.CharacterController.Direction;

public class Player extends Entity implements Character {

	private float oldX, oldY;
	public CharacterController controller;
	
	/*
	 * Booleans for the Character interface
	 */
	private boolean canMoveLeft, canMoveRight, hasGroundLeft, hasGroundRight, isJumping;
	
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
		Direction dir = controller.walkDirection();
		if (dir == Direction.Left)
			velocity.x -= XSpeed;
		else if (dir == Direction.Right)
			velocity.x += XSpeed;
		else
			decreaseXVelocity();
		
		//Limit player speed
		if (velocity.x > maxSpeed)
			velocity.x = maxSpeed;
		else if (velocity.x < -maxSpeed)
			velocity.x = -maxSpeed;
		
		//Handle jump keyboard input
		if (controller.shouldJump() && canJump)
		{
			velocity.y = jump;
			canJump = false;
		}

		//Handle jump states
		
		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
		
		/*
		** check object collision
		*/
		
		isJumping = true;
		canMoveLeft = true;
		canMoveRight = true;
		
		//y-axis collision
		//TODO: collisionYUp is a stupid name, because the method actually checks for collisions below the player, hence it should be called DOWN not UP
		if (collisionYUp(getX(), getY()))
		{
			velocity.y = 0;
			setY(oldY);
			isJumping = false;
		}
		else if (collisionYDown(getX(), getY()))
		{
			canJump = true;
			velocity.y = -velocity.y * 0.1f;
			setY(oldY);
			isJumping = false;
		}
		
		//x-axis collision
		if (collisionXLeft(getX(), getY()))
		{
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
			canMoveLeft = false;
		}
		else if (collisionXRight(getX(), getY()))
		{
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
			canMoveRight = false;
		}
		
		controller.update();
	}
	
	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}

	
	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}
	
	/*
	 * Character interface
	 */

	@Override
	public boolean canMoveRight() {
		return canMoveRight;
	}

	@Override
	public boolean canMoveLeft() {
		return canMoveLeft;
	}

	@Override
	public boolean hasGroundLeft() {
		//TODO: implement correctly
		return hasGroundLeft;
	}

	@Override
	public boolean hasGroundRight() {
		//TODO: implement correctly
		return hasGroundRight;
	}

	@Override
	public boolean isJumping() {
		return isJumping;
	}

}
