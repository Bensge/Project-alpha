package com.project.Entities;

public class Bullet extends Projectile{

	public final static float speed = 600;
	public final static float explosionRadius = 0;
	
	public Bullet(String s, float targetX, float targetY, float originX, float originY, byte ownerID, boolean isMyOwn){
		super(s, targetX, targetY, originX, originY, speed, explosionRadius, ownerID, isMyOwn);
		
		damage = 10;
		setSize(8, 8);
	}
	
	@Override
	public int getRadius() {
		return (int) explosionRadius;
	}
}
