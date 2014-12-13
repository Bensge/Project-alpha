package com.project.CharacterControllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class UserDesktopController extends CharacterController {

	public UserDesktopController(Character c)
	{
		super(c);
	}

	@Override
	public boolean shouldJump()
	{
		return Gdx.input.isKeyPressed(Keys.W);
	}

	@Override
	public Direction walkDirection()
	{
		if (Gdx.input.isKeyPressed(Keys.A))
			return Direction.Left;
		if (Gdx.input.isKeyPressed(Keys.D))
			return Direction.Right;
		return Direction.None;
	}

	@Override
	public void update() {}
	

}
