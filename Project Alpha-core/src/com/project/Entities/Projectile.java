package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends Sprite{
	private Vector2 direction;
	private byte ownerID;
	private boolean isXNegative = false, isMyOwn;
	private float eRadius;
	protected byte damage;
	public float targetX, targetY;
	
	public Projectile(String s, float targetX, float targetY, float originX, float originY, float speed, float explosionRadius, byte ownerID, boolean isMyOwn){
		super(new Texture(Gdx.files.internal(s)));
		//System.out.println("Mah owner is da hood:" + ownerID);
		this.isMyOwn = isMyOwn;
		this.targetX = targetX;
		this.targetY = targetY;
		this.ownerID = ownerID;
		this.eRadius = explosionRadius;
		
		damage = 0;
		setBounds(originX, originY, 8, 8);
		speed = 500;
		
		/* How it works
		*
		*	1. find out alpha
		*	- tan(alpha) = a/b
		*
		*	2. cos(alpha) = x/v	
		*	- v = speed 
		*	- y = x * v
		*	- x = cos(alpha) * v
		*
		*	3. I bet you didn't get what I'm telling you
		*
		*/
		
		direction = new Vector2();
		
		float b = (targetX - originX);
		if(b < 0){
			isXNegative = true;
			b = Math.abs(b);
		}	
		
		float a = (targetY - originY);
		
		float alpha = (float) (Math.atan((a / b)));
	
		direction.x = (float) (Math.cos(alpha) * speed);
		direction.y = (float) (direction.x * Math.tan(alpha));
		
		if(isXNegative){
			direction.x = -direction.x;
		}
		
	}
	
	public void update(float delta){
		setX(getX() + direction.x * delta);
		setY(getY() + direction.y * delta);
	}

	public void render(Batch b){
		super.draw(b);
	}
	
	public int getRadius() {
		return 0;
	}
	
	public byte getDamage(){
		return damage;
	}
	
	public Vector2 getDirection(){
		return direction;
	}
	public byte getOwnerID(){
		return ownerID;
	}

	public boolean getIsMyOwn() {
		return isMyOwn;
	}
}
