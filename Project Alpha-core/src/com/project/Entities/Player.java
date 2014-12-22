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
	private ArrayList<Projectile> projectiles;
	private OrthographicCamera camera;
	private float bulletCooldown = 0.1f, bulletDelta, rocketCooldown = 3, rocketDelta;
	private boolean heroMode;
	
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
		projectiles = new ArrayList<Projectile>();
		
		life = 100;
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
		
		float mouseX = controller.mouseX + (camera.position.x - camera.viewportWidth / 2);
		float mouseY = (Gdx.graphics.getHeight() - controller.mouseY) + (camera.position.y - camera.viewportHeight / 2);
			
		//bullet handling	
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && bulletDelta > bulletCooldown){
			projectiles.add(new Bullet("img/rocket.png", mouseX, mouseY, getX(), getY()));
			bulletDelta = 0;
			//a = false;
		}
		//rocket handling
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && rocketDelta > rocketCooldown){
			projectiles.add(new Rocket("img/rocket.png", mouseX, mouseY, getX(), getY()));
			rocketDelta = 0;
		}
		
		//check if projectiles collided
		Iterator<Projectile> i = projectiles.iterator();
		while(i.hasNext()){
			Projectile b = i.next();
			
			b.update(delta);
			
			if(isOutOfBoundsX(b.getX(), b.getWidth()) || isOutOfBoundsY(b.getY(), b.getHeight())){
				
				if(b.getRadius() != 0){
					if(Constants.circleIntersectsRectangle(new Point((int)b.getX(), (int)b.getY()), b.getRadius(),
							new Point((int)getX(), (int)getY()), getWidth(), getHeight()))
						System.out.println("hit");
				//System.out.println("bullet removed");
				}
				i.remove();
			}
			else if(collisionXLeft(b.getX(), b.getY(), b.getWidth()) || collisionXRight(b.getX(), b.getY(), b.getWidth(), b.getHeight()) || 
					collisionYDown(b.getX(), b.getY(), b.getHeight())|| collisionYUp(b.getX(), b.getY(), b.getWidth(), b.getHeight())) {
				
				if(b.getRadius() != 0){
					if(Constants.circleIntersectsRectangle(new Point((int)b.getX(), (int)b.getY()), b.getRadius(),
							new Point((int)getX(), (int)getY()), getWidth(), getHeight()))
						System.out.println("hit very much");	
				//System.out.println("bullet removed");
				}
				i.remove();
			}
		}
		}
	}
	
	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}

	
	@Override
	public void render(Batch b) {
		controller.update();
		if(controller instanceof UserDesktopController){
			//b.draw(crosshair, controller.mouseX, Gdx.graphics.getHeight() - controller.mouseY);
			//System.out.println("x: " + controller.mouseX + ", Y: " + controller.mouseY);	
		}
		super.render(b);
		for(Projectile p : projectiles)
			p.render(b);
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
