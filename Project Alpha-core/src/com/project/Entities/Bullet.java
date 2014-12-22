package com.project.Entities;

public class Bullet extends Projectile{

	private final static float speed = 500;
	private final static float explosionRadius = 0;
	
	public Bullet(String s, float targetX, float targetY, float originX, float originY){
		super(s, targetX, targetY, originX, originY, speed, explosionRadius);
		
		setSize(8, 8);
	}
	
	@Override
	public int getRadius() {
		return (int) explosionRadius;
	}
}
