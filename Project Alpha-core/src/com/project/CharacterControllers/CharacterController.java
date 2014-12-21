package com.project.CharacterControllers;

public abstract class CharacterController
{
	protected Character character;
	public int mouseX, mouseY;
	
	public enum Direction {
		None,
		Left,
		Right
	};
	
	public CharacterController(Character c)
	{
		character = c;
	}
	
	public abstract boolean shouldJump();
	public abstract Direction walkDirection();
	public abstract void update();

}
