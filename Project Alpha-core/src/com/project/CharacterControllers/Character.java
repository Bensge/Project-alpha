package com.project.CharacterControllers;

public interface Character {
	public boolean canMoveRight();
	public boolean canMoveLeft();
	public boolean hasGroundLeft();
	public boolean hasGroundRight();
	public boolean isJumping();
	public boolean canShoot();
}
