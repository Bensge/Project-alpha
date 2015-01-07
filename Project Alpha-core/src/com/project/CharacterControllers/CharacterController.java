package com.project.CharacterControllers;

import com.badlogic.gdx.math.Vector2;

public abstract class CharacterController
{
	protected Character character;
	public int mouseX, mouseY;
	
	public enum Direction {
		None,
		Left,
		Right
	}
	
	public CharacterController(Character c)
	{
		character = c;
	}
	
	public abstract boolean shouldJump();
	public abstract Direction walkDirection();
	public abstract void update();
	public abstract boolean shouldShootBullet();
	public abstract boolean shouldShootRocket();
	public abstract Vector2 projectileTarget();
	public abstract boolean isProjectileTargetRelativeToPlayer();
}
