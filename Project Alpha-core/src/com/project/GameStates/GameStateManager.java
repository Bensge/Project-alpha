package com.project.GameStates;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {

	private Stack<GameState> states;
	
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
	
	public void update(float delta){
		states.peek().update(delta);
	}
	
	public void render(SpriteBatch b){
		states.peek().render(b);
	}
}
