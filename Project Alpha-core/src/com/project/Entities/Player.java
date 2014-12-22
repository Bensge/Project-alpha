package com.project.Entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.project.CharacterControllers.Character;
import com.project.CharacterControllers.CharacterController;
import com.project.CharacterControllers.CharacterController.Direction;
import com.project.CharacterControllers.UserDesktopController;
import com.project.constants.Constants;

public class Player extends Entity implements Character {

	private float oldX, oldY;
	public CharacterController controller;
	private OrthographicCamera camera;
	private float bulletCooldown = 0.1f, bulletDelta, rocketCooldown = 3, rocketDelta;
	private boolean heroMode;
	private EnemyManager enemyManager;
	
	/*
	 * Booleans for the Character interface
	 */
	private boolean canMoveLeft, canMoveRight, hasGroundLeft, hasGroundRight, isJumping, canShoot;
	
	public Player(int x, int y, TiledMap map, OrthographicCamera camera) {
		super("img/enemy.png", map);
		
		this.camera = camera;
		
		gravity = 1000;
		XSpeed = 10;
		maxSpeed = 250;
		
		setX(x);
		setY(y);
		
		canJump = true;
		canShoot = true;
		jump = 450;
		
		heroMode = false;
		life = 100;
		
		enemyManager = new EnemyManager(this);
	}
	
	@Override
	public void update(float delta) {
		if(Gdx.input.isKeyJustPressed(Keys.H)){
			heroMode = !heroMode;
			System.out.println("change");
		}
		
		//Get old values
		oldX = getX();
		oldY = getY();
		
		if(!heroMode){
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
		if (collisionYUp(getX(), getY(), getWidth(), getHeight()))
		{
			velocity.y = 0;
			setY(oldY);
			isJumping = false;
		}
		else if (collisionYDown(getX(), getY(), getHeight()))
		{
			canJump = true;
			velocity.y = -velocity.y * 0.1f;
			setY(oldY);
			isJumping = false;
		}
		
		//x-axis collision
		if (collisionXLeft(getX(), getY(), getWidth()))
		{
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
			canMoveLeft = false;
		}
		else if (collisionXRight(getX(), getY(), getWidth(), getHeight()))
		{
			velocity.x = -velocity.x * 0.3f;
			setX(oldX);
			canMoveRight = false;
		}
		}
		
		else{
			if(Gdx.input.isKeyPressed(Keys.S))
				setY(getY() - maxSpeed * delta);
			else if (Gdx.input.isKeyPressed(Keys.W))
				setY(getY() + maxSpeed * delta);
			if(Gdx.input.isKeyPressed(Keys.A))
				setX(getX() - maxSpeed * delta);
			else if (Gdx.input.isKeyPressed(Keys.D))
				setX(getX() + maxSpeed * delta);
		}
			
		
		controller.update();
		if(controller instanceof UserDesktopController){
			//shooting handle
			bulletDelta += delta;
			rocketDelta += delta;
			//jeder berechnet ob er selbst getroffen wird
			float mouseX = controller.mouseX + (camera.position.x - camera.viewportWidth / 2);
			float mouseY = (Gdx.graphics.getHeight() - controller.mouseY) + (camera.position.y - camera.viewportHeight / 2);
				
			//bullet handling	
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && bulletDelta > bulletCooldown){
				enemyManager.sendNewBullet(new Bullet("img/rocket.png", mouseX, mouseY, getX(), getY(), this));
				bulletDelta = 0;
				System.out.println("new bullet");
			}
			//rocket handling
			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && rocketDelta > rocketCooldown){
				
				enemyManager.sendNewBullet(new Rocket("img/rocket.png", mouseX, mouseY, getX(), getY(), this));
				rocketDelta = 0;
				System.out.println("new rocket");
				
				}
		}
		
		enemyManager.update(delta);
	}
	
	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}
	public void decreaseLife(int amount){
		life -= amount;
	}

	
	@Override
	public void render(Batch b) {
		controller.update();
		if(controller instanceof UserDesktopController){
			//b.draw(crosshair, controller.mouseX, Gdx.graphics.getHeight() - controller.mouseY);
			//System.out.println("x: " + controller.mouseX + ", Y: " + controller.mouseY);	
		}
		super.render(b);
		enemyManager.render(b);
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

	@Override
	public boolean canShoot() {
		return canShoot;
	}

}
