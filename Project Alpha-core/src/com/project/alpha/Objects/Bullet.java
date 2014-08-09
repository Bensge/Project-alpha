package com.project.alpha.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Sprite{
	
	private Vector2 velocity;
	
	public Bullet(float posX, float posY){
		velocity = new Vector2();
		setX(posX);
		setY(posY);
	}
	
	public void draw(SpriteBatch batch){
		super.draw(batch);
	}
	
	public void update(float delta){
		setPosition(getX() + velocity.x, getY() + velocity.y);
		
	}
}
