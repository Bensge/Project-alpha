package com.loligesh.ReJump.World;

import com.badlogic.gdx.Game;

public class WorldControler extends Game{

	
	
	@Override
	public void create() {
		setScreen(new WorldRenderer());
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
	public void resume() {
		super.resume();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
}
