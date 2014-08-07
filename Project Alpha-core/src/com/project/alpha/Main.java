package com.project.alpha;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class Main extends Game implements ScreenCallback {
	
	public static Main instance = null;
	
	public static Main sharedInstance()
	{
		return instance;
	}
	
	@Override
	public void create() {
		
		instance = this; //sharedInstance
		
		MainMenu mainMenu = new MainMenu();
		
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
	
	public void screenWantsDismissal(Screen screen)
	{
		if (screen instanceof MainMenu)
		{
			setScreen(new AlphaGame());
		}
		//else if () ///For more screens
	}
}
