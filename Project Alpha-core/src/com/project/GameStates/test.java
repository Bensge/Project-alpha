package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class test extends GameState {

	private Sprite sprite = null;
	
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
		if (sprite == null)
		{
			sprite = new Sprite(new Texture("img/enemy.png"));
			sprite.setSize(100, 20);
			sprite.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		}
		sprite.draw(b);
	}
	
}
