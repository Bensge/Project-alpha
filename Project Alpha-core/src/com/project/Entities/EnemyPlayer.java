package com.project.Entities;

import com.badlogic.gdx.graphics.Texture;

import com.project.common.Point;

public class EnemyPlayer extends Entity
{
	byte id;

	public EnemyPlayer(Point position, String name, byte id){
		super(new Texture("img/enemy.png"));
		this.id = id;
		setName(name);
		setBounds(position.x, position.y, getWidth(), getHeight());
	}

	public byte getID()
	{
		return id;
	}
	
	public void update(float x, float y){
		setPosition(x, y);
	}
	
	@Override
	public void update(float delta) {	}
	
	public int getLife(){
		return life;
	}
	
	public String getName(){
		return name;
	}
}
