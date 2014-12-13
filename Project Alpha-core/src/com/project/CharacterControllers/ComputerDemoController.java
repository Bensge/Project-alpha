package com.project.CharacterControllers;

import java.util.Random;

public class ComputerDemoController extends CharacterController {

	private boolean jumpOnce;
	private Direction walkDirection;
	private Random random;
	
	public ComputerDemoController(Character c) {
		super(c);
		random = new Random();
		reconsiderDirection();
	}
	
	private void reconsiderDirection()
	{
		if (character.canMoveLeft() && character.canMoveRight())
		{
			walkDirection = random.nextBoolean() ? Direction.Left : Direction.Right;
		}
		else if (character.canMoveLeft())
		{
			if (random.nextBoolean())
			{
				jumpOnce = true;
			}
			else
			{
				walkDirection = Direction.Left;
			}
		}
		else if (character.canMoveRight())
		{
			if (random.nextBoolean())
			{
				jumpOnce = true;
			}
			else
			{
				walkDirection = Direction.Right;
			}
		}
		else
		{
			jumpOnce = true;
			walkDirection = random.nextBoolean() ? Direction.Left : Direction.Right;
		}
		
		System.out.println("Recalculated direction = " + walkDirection + " jumpOnce = " + jumpOnce);
	}

	@Override
	public boolean shouldJump() {
		if (jumpOnce){
			jumpOnce = false;
			return true;
		}
		return false;
	}

	@Override
	public Direction walkDirection()
	{
		return walkDirection;
	}

	@Override
	public void update()
	{
		if (!(character.isJumping() || jumpOnce) && ((walkDirection == Direction.Right && !character.canMoveRight()) || (walkDirection == Direction.Left && !character.canMoveLeft())))
		{
			reconsiderDirection();
		}
		
		//System.out.println("Update isJumping=" + character.isJumping() + " canMoveRight=" + character.canMoveRight() + " canMoveLeft=" + character.canMoveLeft() + " walkingDirection=" + walkDirection);
	}
}
