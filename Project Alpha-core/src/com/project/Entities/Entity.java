package com.project.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public abstract class Entity extends Sprite {
	
	protected int mapWidth, mapHeight;
	protected Vector2 velocity;
	protected int damage;
	
	public Entity() {
		damage = 0;
		velocity = new Vector2();
	}
	
	public Entity(String s){
		super(new Sprite(new Texture(Gdx.files.internal(s))));
		damage = 0;
		velocity = new Vector2();
		setSize(getTexture().getWidth(), getTexture().getHeight());
	}
	
	public void render(SpriteBatch b){
		super.draw(b);
	}
	
	public boolean isOutOfBoundsX(float x, float width) {
		return (x < 0 || x + width > mapWidth);
	}

	public boolean isOutOfBoundsY(float y, float height) {
		return (y < 0 || y + height > mapHeight);
	}
	
	public abstract void update(float delta);
}
