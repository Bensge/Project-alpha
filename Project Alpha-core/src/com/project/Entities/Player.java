package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Entity {

	float oldX, oldY, jump;
	
	public Player(int x, int y) {
		super("img/enemy.png");
		
		setX(x);
		setY(y);
		
		jump = 50;
	}
	
	@Override
	public void update(float delta) {
		
		oldX = getX();
		oldY = getY();
		
		//velocity.x = 0;
		velocity.y = 0;
		
		if(Gdx.input.isKeyPressed(Keys.A))
			velocity.x = -10;
		else if(Gdx.input.isKeyPressed(Keys.D))
			velocity.x = 10;
		
		else{decreaseXVelocity();};
		
		if(Gdx.input.isKeyPressed(Keys.W))
			velocity.y = 10;
		else if(Gdx.input.isKeyPressed(Keys.S)){
			//go down a level
			velocity.y -= 10;
		}
			
		
		
		setX(getX() + velocity.x);
		setY(getY() + velocity.y);
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
