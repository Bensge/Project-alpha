package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

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
			System.out.println("jumping");
			velocity.y = jump; //use += instead!?
			canJump = false;
		}
		else if(Gdx.input.isKeyPressed(Keys.S)){
			//go down a level
			//TODO
		}

		
		//Handle jump states
		//(ground)
		if(getY() < 0){
			canJump = true;
			setY(0);
			velocity.y = 0;
		}
		
		//check out of map
		if(isOutOfBoundsX(getX(), getWidth())){
			velocity.x = -velocity.x * 0.3f;
		}
		
		if(isOutOfBoundsY(getY(), getWidth())){
			velocity.y = -velocity.y * 0.3f;
		}
		
		
		/*
		** check object collision
		*/
		
		float newX = 999999999;
		float newY = 999999999;
		
		//Check left
		Cell cell = collisionLayer.getCell((int) ((getX()) / collisionLayer.getTileWidth()), (int) ((getY() + 2) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			System.out.println("Can't move left!");
			//Can't move left!
			velocity.x = (velocity.x > 0) ? velocity.x : 0;
			//STOP MOVEEEEEEENNNNN
			//newX = getX();
		}
		//Check right
		cell = collisionLayer.getCell(((int) ((getX()) / collisionLayer.getTileWidth())) + 1, (int) ((getY() + 2) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			System.out.println("Can't move right!");
			//Can't move right!
			velocity.x = (velocity.x < 0) ? velocity.x : 0;
			//STOP MOVEEEEEEENNNNN
			//newX = getX();
		}
		
		//Check up
		cell = collisionLayer.getCell(((int) ((getX() + getWidth() / 2) / collisionLayer.getTileWidth())), (int)(getY() / collisionLayer.getTileHeight()) + 1);
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump up!
			System.out.println("Collision above");
			velocity.y = (velocity.y < 0) ? velocity.y : 0;
			canJump = false;
			//STOP MOVEEEEEEENNNNN
			//newY = getY();
		}
		
		//Check down (l)
		cell = collisionLayer.getCell(((int) ((getX()) / collisionLayer.getTileWidth())), (int)((getY()) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump down!
			System.out.println("Collision below");
			velocity.y = (velocity.y > 0) ? velocity.y : 0;
			canJump = true;
			//STOP MOVEEEEEEENNNNN
			//newY = getY();
		}
		//Check down (r)
		cell = collisionLayer.getCell(((int) ((getX() + getWidth()) / collisionLayer.getTileWidth())), (int)((getY()) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump down!
			System.out.println("Collision below");
			velocity.y = (velocity.y > 0) ? velocity.y : 0;
			canJump = true;
			//STOP MOVEEEEEEENNNNN
			//newY = getY();
		}
		
		//Update running stuff
		if (newX == 999999999)
			newX = getX() + velocity.x * delta;
		
		if (newY == 999999999)
			newY = getY() + velocity.y * delta;
		
		
		//Check up again
		cell = collisionLayer.getCell(((int) ((newX + getWidth() / 2) / collisionLayer.getTileWidth())), (int)(newY / collisionLayer.getTileHeight()) + 1);
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump up!
			System.out.println("Collision above");
			velocity.y = (velocity.y < 0) ? velocity.y : 0;
			canJump = false;
			//STOP MOVEEEEEEENNNNN
			newY = oldY;
		}
		
		//Check down again (l)
		cell = collisionLayer.getCell(((int) ((newX) / collisionLayer.getTileWidth())), (int)((newY) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump down!
			System.out.println("Collision below");
			velocity.y = (velocity.y > 0) ? velocity.y : 0;
			canJump = true;
			//STOP MOVEEEEEEENNNNN
			newY = oldY;
		}
		
		//Check down again (r)
		cell = collisionLayer.getCell(((int) ((newX + getWidth()) / collisionLayer.getTileWidth())), (int)((newY) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			//Can't jump down!
			System.out.println("Collision below");
			velocity.y = (velocity.y > 0) ? velocity.y : 0;
			canJump = true;
			//STOP MOVEEEEEEENNNNN
			newY = oldY;
		}
		
		
		
		//Check left again
		cell = collisionLayer.getCell((int) ((newX) / collisionLayer.getTileWidth()), (int) ((newY + 2) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			System.out.println("Can't move left!");
			//Can't move left!
			velocity.x = (velocity.x > 0) ? velocity.x : 0;
			//STOP MOVEEEEEEENNNNN
			newX = oldX;
		}
		//Check right again
		cell = collisionLayer.getCell(((int) ((newX) / collisionLayer.getTileWidth())) + 1, (int) ((newY + 2) / collisionLayer.getTileHeight()));
		if (cell != null && cell.getTile().getProperties().containsKey(blockKey))
		{
			System.out.println("Can't move right!");
			//Can't move right!
			velocity.x = (velocity.x < 0) ? velocity.x : 0;
			//STOP MOVEEEEEEENNNNN
			newX = oldX;
		}
		
		//Update player position
		setX(newX);
		setY(newY);
		
	}
	
	private void decreaseXVelocity() {
		velocity.x /= 1.2f;
	}

	
	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}

}
