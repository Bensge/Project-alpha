package com.project.GameStates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {

	protected GameStateManager manager;
	
	protected GameState(GameStateManager manager){
		this.manager = manager;
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch b);
}
