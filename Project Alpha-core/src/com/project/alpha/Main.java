package com.project.alpha;

import com.badlogic.gdx.Game;

public class Main extends Game {
	
	MainMenu mainMenu;
	
	@Override
	public void create() {
		
		mainMenu = new MainMenu(this);
		
		setScreen(mainMenu);
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void render() {
		super.render();
		
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void pause() {
		super.pause();
	}
	
	@Override
	public void resume() {
		super.resume();
	}
}
