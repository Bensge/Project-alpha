package com.project.Entities;

public class Rocket extends Projectile {

	public final static float speed = 50;
	public final static float explosionRadius = 50;
	
	public Rocket(String s, float targetX, float targetY, float originX,
			float originY, Entity owner) {
		super(s, targetX, targetY, originX, originY, speed, explosionRadius, owner);
		
		damage = 34;
		setSize(16, 16);
	}

	@Override
	public int getRadius() {
		return (int) explosionRadius;
	}
}
