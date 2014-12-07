package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Entity {

	float oldX, oldY, posX, posY;
	
	public Player(int x, int y) {
		super("img/enemy.png");
		
		setX(x);
		setY(y);
		
		posX = x;
		posY = y;
		
	}
	
	@Override
	public void update(float delta) {
		
		oldX = posX;
		oldY = posY;
		
		if(Gdx.input.isKeyPressed(Keys.A))
			velocity.x = -10;
		else if(Gdx.input.isKeyPressed(Keys.D))
			velocity.x = 10;
		
		if(Gdx.input.isKeyPressed(Keys.W))
			velocity.y = -10;
		else if(Gdx.input.isKeyPressed(Keys.S))
			velocity.y = 10;
		
		posX = velocity.x;
		posY = velocity.y;
	}
	
	@Override
	public void render(SpriteBatch b) {
		super.render(b);
	}

}
