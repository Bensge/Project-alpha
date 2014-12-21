package com.project.CharacterControllers;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;


public class UserDesktopController extends CharacterController {

	
	public UserDesktopController(Character c)
	{
		super(c);
		
		Pixmap pm = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
		int xHotspot =mouseX / 2;
		int yHotspot = mouseY / 2;
		
		//Pixmap pm = new Pixmap(Gdx.files.internal("img/crosshair.bmp"));
	
		//Gdx.input.setCursorCatched(true);
		//Gdx.input.setCursorImage(pm, xHotspot, yHotspot);
		
		//Gdx.input.setCursorImage(null, 2, 2);
		//crosshair stuff missing
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
		/*if(mouseX < 0)
			mouseX = 0;
		else if(mouseX >= Gdx.graphics.getWidth())
			mouseX = Gdx.graphics.getWidth();
		
		if(mouseY < 0)
			mouseY = 0;
		else if(mouseX >= Gdx.graphics.getHeight())
			mouseY = Gdx.graphics.getHeight();
		
		Gdx.input.setCursorPosition(mouseX, mouseY);*/
	}

	

}
