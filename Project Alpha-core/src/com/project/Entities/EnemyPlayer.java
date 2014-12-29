package com.project.Entities;

import java.awt.Point;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.project.constants.Constants;

public class EnemyPlayer extends Entity
{
	private static BitmapFont nameFont = Constants.menlo10Font;
	private static ShapeRenderer lifeRenderer;
	static {
		lifeRenderer = new ShapeRenderer();
	}

	private String name;
	private float nameLength;
	private byte id;

	public EnemyPlayer(Point position, String name, byte id){
		super(new Texture("img/enemy.png"));
		this.id = id;
		this.name = name;
		this.nameLength = nameFont.getBounds(name).width;
		setBounds(position.x, position.y, getWidth(), getHeight());
	}

	public byte getID()
	{
		return id;
	}
	
	public void update(float x, float y){
		setPosition(x, y);
	}
	
	public void render(Batch b){
		super.render(b);

		nameFont.draw(b,name,getX() + getWidth()/2 - nameLength/2,getY() + getHeight() + nameFont.getLineHeight() + 14);

		/*
		//Draw life bar
		lifeRenderer.setProjectionMatrix(b.getProjectionMatrix());

		lifeRenderer.begin(ShapeRenderer.ShapeType.Line);
		lifeRenderer.setColor(1, 1, 1, 1);
		lifeRenderer.rect(getX(), getY() + getHeight() + 2, getWidth(), 5);
		lifeRenderer.end();

		lifeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		lifeRenderer.setColor(1, 1, 1, 1);
		lifeRenderer.rect(getX(), getY() + getHeight() + 2, getWidth() * (float)life / 100.f, 5);
		lifeRenderer.end();
		*/
	}

	@Override
	public void update(float delta) {	}
}
