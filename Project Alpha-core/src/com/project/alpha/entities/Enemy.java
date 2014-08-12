package com.project.alpha.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.project.alpha.screens.AlphaGame;

public class Enemy extends Entity{

	int delay, life, speed;
	float playerX, playerY;
	private Random r;
	
	public Enemy(String s){
		super(s);
		
		r = new Random();
		speed = 50;
		
		setX(r.nextInt((int) AlphaGame.getInstance().mapWidth));
		setY(r.nextInt((int) AlphaGame.getInstance().mapHeight));
		
		setSize(getTexture().getWidth(), getTexture().getHeight());
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}
	
	public void update(float x, float y, float delta){
		playerX = x;
		playerY = y;
		
		
	}
}
