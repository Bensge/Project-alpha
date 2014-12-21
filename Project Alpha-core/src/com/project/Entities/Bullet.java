package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Sprite{

	private Vector2 direction;
	private float speed, factor;
	private float originY;
	private boolean isXNegative = false;
	
	public Bullet(String s, float targetX, float targetY, float originX, float originY){
		super(new Texture(Gdx.files.internal("img/rocket.png")));
		
		setBounds(originX, originY, 8, 8);
		System.out.println(targetX + ", " + targetY + ", " + originX + ", "+ originY );
		speed = 300;
		
		//targetY = Gdx.graphics.getHeight() - targetY;
		
		System.out.println(targetX + ", " + targetY);
		
		direction = new Vector2();
		
		float b = (targetX - originX);
		if(b < 0){
			isXNegative = true;
			b = Math.abs(b);
		}	
		
		float a = (targetY - originY);
		
		System.out.println("a: " + a + ", b: " + b);
		
		float alpha = (float) (Math.atan((a / b)));
		
		System.out.println("alpha: " + alpha);
		System.out.println("Cos(alpha): " + Math.toDegrees(Math.cos(alpha)));
		
		direction.x = (float) (Math.cos(alpha) * speed);
		direction.y = (float) (direction.x * Math.tan(alpha));
		
		if(isXNegative){
			direction.x = -direction.x;
			//direction.y = -direction.y;
		}
		
		System.out.println("x: "+ direction.x + ", y: " + direction.y);
		
	}
	
	public void update(float delta){
		//System.out.println(getX() + ", " + getY());
		
		setX(getX() + direction.x * delta);
		setY(getY() + direction.y * delta);
	}

	public void render(Batch b){
		super.draw(b);
	}
}
