package com.project.GameStates;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {

	private Stack<GameState> states;
	private GameState backgroundState;
	private boolean renderBackgroundStateExclusively;
	
	public GameStateManager(){
		states = new Stack<GameState>();
	}
	
	public void push(GameState state){
		//adds on top
		states.push(state);
	}
	
	public void remove(){
		//removes top
		states.pop();
	}
	
	/*
	 * Properties 
	 */
	
	public void setBackgroundState(GameState state)
	{
		if (backgroundState != null)
			backgroundState.setIsInBackground(false);
		backgroundState = state;
		backgroundState.setIsInBackground(true);
	}
	
	public GameState getBackgroundState(){
		return backgroundState;
	}
	
	public void setRenderBackgroundStateExclusively(boolean exclusively){
		if (backgroundState != null)
			backgroundState.setIsInBackground(!exclusively);
		renderBackgroundStateExclusively = exclusively;
	}
	
	
	/*
	 * Render methods 
	 */
	
	public void update(float delta)
	{
		if (backgroundState != null)
			backgroundState.update(delta);
		
		if (!renderBackgroundStateExclusively || backgroundState == null)
			states.peek().update(delta);
	}
	
	public void render(SpriteBatch b)
	{
		if (backgroundState != null)
			backgroundState.render(b);
		
		if (!renderBackgroundStateExclusively || backgroundState == null){
			GameState s = states.peek();
			s.shouldClearScreen = backgroundState == null;
			s.render(b);
		}
	}
}
