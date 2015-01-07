package com.project.CharacterControllers;

import java.awt.Cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;


public class UserDesktopController extends CharacterController {

	private Vector2 projectileTarget;
	
	public UserDesktopController(Character c)
	{
		super(c);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("img/crosshair.png"));
        int xHotSpot = pm.getWidth() / 2;
        int yHotSpot = pm.getHeight() / 2;
        
        
        Gdx.input.setCursorImage(pm, xHotSpot, yHotSpot);
        pm.dispose();

		projectileTarget = new Vector2(0,0);
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
	public void update()
	{
		projectileTarget.x = Gdx.input.getX();
		projectileTarget.y = Gdx.input.getY();
	}

	@Override
	public boolean shouldShootBullet()
	{
		return Gdx.input.isButtonPressed(Input.Buttons.LEFT);
	}

	@Override
	public boolean shouldShootRocket()
	{
		return Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	}

	@Override
	public Vector2 projectileTarget()
	{
		return projectileTarget;
	}

	@Override
	public boolean isProjectileTargetRelativeToPlayer()
	{
		return false;
	}


}
