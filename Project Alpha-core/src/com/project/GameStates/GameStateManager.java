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
	
	public void push(GameState state)
	{
		if (!states.isEmpty())
		{
			GameState bgState = states.peek();
			bgState.setIsInBackground(true);
		}
		//adds on top
		states.push(state);
	}
	
	public void remove(){
		//removes top
		GameState state = states.pop();
		//DIE
		state.dispose();
		
		//Reactivate new frontmost state
		state = states.peek();
		state.setIsInBackground(false);
	}
	
	public GameState topMostState()
	{
		return states.isEmpty() ? null : states.peek();
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
