package com.project.main;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundManager {

	public ArrayList<Background> backgrounds;
	
	public BackgroundManager(){
		backgrounds = new ArrayList<Background>();
	}
	
	public void addBackground(String path, float distance){
		backgrounds.add(new Background(path, distance));
	}
	
	public void removeBackground(int index){
		backgrounds.remove(index);
	}
	
	public void update(float delta){
		for(Background back : backgrounds)
			back.update(delta);
	}
	
	public void render(SpriteBatch b){
		for(Background back : backgrounds)
			back.render(b);
	}
	
	
	private class Background extends Sprite{
		
		private float distanceFactor;
		
		public Background(String path, float factor){
			super(new Texture(Gdx.files.internal(path)));
		}
		
		public void update(float delta){
			
		}
		
		public void render(SpriteBatch b){
			
		}
	}
}
