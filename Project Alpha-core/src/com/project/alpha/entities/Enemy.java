package com.project.alpha.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.project.alpha.screens.AlphaGame;

public class Enemy extends Entity{

	int delay, life, speed;
	float playerX, playerY, oldX, oldY;
	private Random r;
	ShapeRenderer shapeRenderer;
	
	public Enemy(String s){
		super(s);
		
		shapeRenderer = new ShapeRenderer();
		
		r = new Random();
		speed = 50;
		life = 100;
		
		setX(r.nextInt((int) AlphaGame.getInstance().mapWidth));
		setY(r.nextInt((int) AlphaGame.getInstance().mapHeight));
		
		oldX = getX();
		oldY = getY();
		
		setSize(getTexture().getWidth(), getTexture().getHeight());
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
		oldX = getX();
		oldY = getY();
	}
	
	public void renderStuff(){	}
	
	public void update(float x, float y, float delta){
		playerX = x;
		playerY = y;
		
		
	}
	
	public void resetPosX(){
		setX(oldX);
	}
	
	public void resetPosY(){
		setY(oldY);
	}

	public void hit(int damage) {
		life -= damage;
	}
	
	public boolean isDown(){
		return life <= 0;
	}
}
