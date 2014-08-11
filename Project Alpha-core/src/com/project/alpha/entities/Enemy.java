package com.project.alpha.entities;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.project.alpha.screens.AlphaGame;

public class Enemy extends Sprite{

	private int delay, life, speed;
	private float playerX, playerY;
	private Random r;
	private static Texture t = new Texture(Gdx.files.internal("img/enemy.png"));;
	
	public Enemy(){
		super(new Sprite(t));
		
		r = new Random();
		speed = 50;
		
		setX(r.nextInt((int) AlphaGame.getInstance().mapWidth));
		setY(r.nextInt((int) AlphaGame.getInstance().mapHeight));
		
		setSize(t.getWidth(),t.getHeight());
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}
	
	public void update(float x, float y, float delta){
		playerX = x;
		playerY = y;
		
		if(playerX > getX())
			setX(getX() + speed * delta);
		else
			setX(getX() - speed * delta);
		
		if(playerY > getY())
			setY(getY() + speed * delta);
		else
			setY(getY() - speed * delta);
	}
}
