package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.project.CharacterControllers.Character;
import com.project.CharacterControllers.CharacterController;
import com.project.CharacterControllers.UserMobileController;
import com.project.CharacterControllers.CharacterController.Direction;
import com.project.CharacterControllers.UserDesktopController;
import com.project.Preferences.AppPreferences;
import com.project.constants.Constants;
import com.project.networking.Common.PlayerUpdatePacket;
import com.project.networking.MultiplayerGameSessionController;

public class Player extends Entity implements Character {

	private float oldX, oldY;
	public CharacterController controller;
	private OrthographicCamera camera;
	private float bulletCooldown = 0.1f, bulletDelta, rocketCooldown = 2, rocketDelta = 3;
	private boolean heroMode, firstTime = true;
	private EnemyManager enemyManager;
	private long lastSendTime = 0;
	
	/*
	 * Booleans for the Character interface
	 */
	private boolean canMoveLeft, canMoveRight, hasGroundLeft, hasGroundRight, isJumping, canShoot;
	
	public Player(int x, int y, TiledMap map, OrthographicCamera camera) {
		super("img/enemy.png", map);
		
		this.camera = camera;
		setName(AppPreferences.sharedInstance().getUserName());
		
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
	public void update(float delta)
	{
		//Godmode is desktop only
		if (Gdx.input.isKeyJustPressed(Keys.H)){
			heroMode = !heroMode;
		}
		
		//Get old values
		oldX = getX();
		oldY = getY();
		
		if (!heroMode)
		{
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
		
		else
		{
			//Godmode is desktop only and experimental
			if (Gdx.input.isKeyPressed(Keys.S))
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
			desktopInputHandling(delta);
		}

		//player position is sent here
		long time;
		if ((time = System.nanoTime()) - lastSendTime > 1000 * 1000 * 1000 / Constants.TICKRATE && 
				MultiplayerGameSessionController.sharedInstance().isMultiplayerSessionActive() &&
				(oldX != getX() || oldY != getY() || firstTime) && MultiplayerGameSessionController.sharedInstance().isMultiplayerSessionActive()) {
			lastSendTime = time;
			PlayerUpdatePacket packet = new PlayerUpdatePacket();
			packet.locationX = (int)getX();
			packet.locationY = (int)getY();
			MultiplayerGameSessionController.sharedInstance().sendPacket(packet);
			
			firstTime = false;
		}
		
		enemyManager.update(delta);
	}

	/* Input handling methods */
	private void desktopInputHandling(float delta) {
		//shooting handle
		
		bulletDelta += delta;
		rocketDelta += delta;

		float mouseX = controller.projectileTarget().x + (camera.position.x - camera.viewportWidth / 2);
		float mouseY = (Gdx.graphics.getHeight() - controller.projectileTarget().y) + (camera.position.y - camera.viewportHeight / 2);

		if (controller.isProjectileTargetRelativeToPlayer()) {
			mouseX += getX();
			mouseY += getY();
		}

		//bullet handling
		if (controller.shouldShootBullet() && bulletDelta > bulletCooldown){
			float XDirection =  getX() + getWidth() / 2;
			float YDirection =  getY() + getHeight() / 2;
			Bullet b = new Bullet("img/rocket.png", mouseX, mouseY, XDirection, YDirection, (byte) -1, true);
			
			if(MultiplayerGameSessionController.sharedInstance().isMultiplayerSessionActive())
				enemyManager.sendNewBullet(b, Constants.BULLET_TYPE);
			else
				enemyManager.addProjectile(b);
			bulletDelta = 0;
			
		}
		//rocket handling
		if(controller.shouldShootRocket() && rocketDelta > rocketCooldown){
			Rocket r = new Rocket("img/rocket.png", mouseX, mouseY, getX(), getY(), (byte) -1, true);
			
			if(MultiplayerGameSessionController.sharedInstance().isMultiplayerSessionActive())
				enemyManager.sendNewBullet(r, Constants.ROCKET_TYPE);
			else
				enemyManager.addProjectile(r);
			rocketDelta = 0;
		}
	}

	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}

	public void resetLife()
	{
		life = 100;
	}

	public void respawn()
	{
		float randomX = (float)(MathUtils.random(collisionLayer.getWidth()));
		setPosition(randomX * collisionLayer.getTileWidth(),(collisionLayer.getHeight() - 1) * collisionLayer.getTileHeight());
	}

	public void decreaseLife(int amount){
		life -= amount;
	}

	public boolean IAmDead(){
		return life <= 0;
	}
	
	@Override
	public void render(Batch b) {
		controller.update();
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
