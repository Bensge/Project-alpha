package com.project.alpha.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Entity extends Sprite {
	
	protected Vector2 velocity;
	int damage;
	
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
	
	public void update(float delta){
	}
	
	public void render(SpriteBatch batch){
		super.draw(batch);
	}
	
	public int getDamage(){
		return damage;
	}
}
