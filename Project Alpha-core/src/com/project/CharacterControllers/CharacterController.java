package com.project.CharacterControllers;

public abstract class CharacterController
{
	@SuppressWarnings("unused")
	private Character character;
	
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
}
