package com.project.Entities;

public class Bullet extends Projectile{

	public final static float speed = 500;
	public final static float explosionRadius = 0;
	
	public Bullet(String s, float targetX, float targetY, float originX, float originY, Entity owner){
		super(s, targetX, targetY, originX, originY, speed, explosionRadius, owner);
		
		damage = 10;
		setSize(8, 8);
	}
	
	@Override
	public int getRadius() {
		return (int) explosionRadius;
	}
}
