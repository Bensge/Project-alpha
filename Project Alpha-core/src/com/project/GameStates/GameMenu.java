package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameMenu extends GameState {

	private Table table;
	private Stage stage;
	private ShapeRenderer shapeRenderer;
	
	public GameMenu(GameStateManager m)
	{
		super(m);
		
		Gdx.input.setInputProcessor(stage);
	    table = new Table();
	    table.setFillParent(true);
	    stage.addActor(table);
	    
	    shapeRenderer = new ShapeRenderer();
	    
	    //Add buttons
	    
	    TextButtonStyle style = new TextButtonStyle();
	    //style.up = new TextureRegionDrawable(upRegion);
	    //style.down = new TextureRegionDrawable(downRegion);
	    style.font = new BitmapFont();

	    TextButton button = new TextButton("Single Player", style);
	    table.add(button);
	    
	    button.addListener(new ChangeListener() {
	        public void changed (ChangeEvent event, Actor actor) {
	            System.out.println("Changed!");
	        }
	    });

	    button = new TextButton("Multiplayer", style);
	    table.add(button);
	    
	    button = new TextButton("Settings", style);
	    table.add(button);
	    
	}
	
	@Override
	public void update(float delta)
	{
		
	}

	@Override
	public void render(SpriteBatch b) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();

	    table.drawDebug(shapeRenderer); // This is optional, but enables debug lines for tables.
	}

}
