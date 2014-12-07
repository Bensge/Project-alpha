package com.project.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.GameWorld;
import com.project.GameStates.test;

public class Main implements ApplicationListener{

	private GameStateManager manager;
	private SpriteBatch b;
	
	@Override
	public void create() {
		b = new SpriteBatch();
		
		manager = new GameStateManager();
		manager.push(new GameWorld(manager));
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void render() {
		Gdx.gl20.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		b.begin();
		manager.render(b);
		b.end();
		update();
	}
	
	

	private void update() {
		float delta = Gdx.graphics.getDeltaTime();
		manager.update(delta);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
