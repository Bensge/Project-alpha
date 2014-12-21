package com.project.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingIndicator extends Actor
{
	private Texture texture;
	private float rotation;
	
	public LoadingIndicator()
	{
		super();
		
		rotation = 0;
		texture = new Texture(Gdx.files.internal("img/loading_indicator.png"));
		
		setWidth(texture.getWidth() / 2);
		setHeight(texture.getHeight() / 2);
	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		rotation = (rotation - 360.f * delta) % 360;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		//Pay attention to parent alpha, important for correct transitions
		batch.setColor(1, 1, 1, parentAlpha);
		batch.draw(texture, getX() + getOriginX(), getY() + getOriginY(), texture.getWidth() / 2 / 2, texture.getHeight() / 2 / 2, texture.getWidth() / 2, texture.getHeight() / 2, 0.5f, 0.5f, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
	}
}
