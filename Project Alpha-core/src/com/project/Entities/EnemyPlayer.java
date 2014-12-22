package com.project.Entities;

import java.awt.Point;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class EnemyPlayer extends Entity{

	private String name;
	
	public EnemyPlayer(Point position, String name){
		super(new Texture("img/player.png"));
		
		this.name = name;
		setBounds(position.x, position.y, getWidth(), getHeight());
	}
	
	public void update(float x, float y){
		setPosition(x, y);
	}
	
	public void render(Batch b){
		super.render(b);
	}

	@Override
	public void update(float delta) {	}
}
