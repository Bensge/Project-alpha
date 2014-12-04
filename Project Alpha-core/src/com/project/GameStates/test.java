package com.project.GameStates;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class test extends GameState {

	public test(GameStateManager manager){
		super(manager);
	}
	
	@Override
	public void update(float delta) {
		System.out.println("update");
	}

	@Override
	public void render(SpriteBatch b) {
		System.out.println("la");
		b.draw(new Sprite(new Texture("badlogic.jpg.png")), 10, 10);
	}

}
