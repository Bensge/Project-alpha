package com.project.CharacterControllers;

import java.awt.Cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;


public class UserDesktopController extends CharacterController {

	
	public UserDesktopController(Character c)
	{
		super(c);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("img/crosshair.png"));
        int xHotSpot = pm.getWidth() / 2;
        int yHotSpot = pm.getHeight() / 2;
        
        
        Gdx.input.setCursorImage(pm, xHotSpot, yHotSpot);
        pm.dispose();
		
		//Gdx.input.setCursorCatched(true);
		
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
	public void update() {
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();
		
		//keep mouse in bounds
		/*if(mouseX < 3)
			mouseX = 3;
		else if(mouseX >= Gdx.graphics.getWidth() - 3)
			mouseX = Gdx.graphics.getWidth() - 3;
		
		if(mouseY < 0)
			mouseY = 0;
		else if(mouseX >= Gdx.graphics.getHeight())
			mouseY = Gdx.graphics.getHeight();
		*/
		//Gdx.input.setCursorPosition(mouseX, mouseY);
	}

	

}
