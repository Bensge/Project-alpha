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
	}

	@Override
	public boolean shouldJump() {
		if (jumpOnce)
			jumpOnce = false;
		return jumpOnce;
	}

	@Override
	public Direction walkDirection()
	{
		if (!character.isJumping() && (walkDirection == Direction.Right && !character.canMoveRight() || walkDirection == Direction.Left && !character.canMoveLeft()))
		{
			reconsiderDirection();
		}
		
		return walkDirection;
	}

}
