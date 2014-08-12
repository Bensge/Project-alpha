package com.project.alpha.entities;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Zombie extends Enemy{

	public Zombie() {
		super("img/enemy.png");
		
		speed = 10;
	}

	@Override
	public void update(float playerX, float playerY, float delta) {
		super.update(playerX, playerY, delta);
		
		if(playerX > getX())
			velocity.x = speed;
		else if(playerX < getX())
			velocity.x = -speed;
		
		if(playerY > getY())
			velocity.y = speed;
		else if(playerY < getY())
			velocity.y = -speed;
		
		
		setX(getX() + velocity.x * delta);
		setY(getY() + velocity.y * delta);
		
	}
	
	@Override
	public void draw(Batch batch) {
		float tmpX = getX();
		float tmpY = getY();
		
		if(tmpX > playerX - 2 && tmpX < playerX + 2)
			setX((int) playerX);
		if(tmpY > playerY - 2 && tmpY < playerY + 2)
			setY((int) playerY);
		
		super.draw(batch);
		
		setX(tmpX);
		setY(tmpY);
	}
}
