package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public abstract class GameState implements Disposable {
	
	public boolean shouldClearScreen;
	protected boolean isInBackground;

	protected GameStateManager manager;
	
	protected GameState(GameStateManager manager){
		this.manager = manager;
		this.shouldClearScreen = true;
	}
	
	public abstract void update(float delta);
	public void render(SpriteBatch b)
	{
		if (shouldClearScreen)
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}
	}
	
	public void setIsInBackground(boolean bg)
	{
		isInBackground = bg;
	}
	
	public abstract void dispose();
}
