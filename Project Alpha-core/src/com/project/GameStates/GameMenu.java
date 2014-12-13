package com.project.GameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.constants.Constants;

public class GameMenu extends GameState {

	private Table table;
	private Stage stage;
	private ShapeRenderer backgroundRenderer;
	
	public GameMenu(GameStateManager m)
	{
		super(m);
		
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		//Create table
	    table = new Table();
	    table.setFillParent(true);
	    table.debugTable();
	    table.debugCell();
	    table.debug();
	    
	    //Setup stage
	    stage.addActor(table);
	    
	    //Setup renderer
	    backgroundRenderer = new ShapeRenderer();
	    
	    //Add header
	    
	    LabelStyle headerStyle = new LabelStyle(Constants.menlo50Font, new Color(200, 10, 50, 255));
		Label header = new Label("Project alpha", headerStyle);
		
		table.row().pad(60);
		table.add(header);
	    
	    //Add buttons
	    TextButtonStyle style = new TextButtonStyle();
	    style.font = Constants.menlo32Font;

	    TextButton button = new TextButton("Single Player", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(new ChangeListener() {
	        public void changed (ChangeEvent event, Actor actor) {
	            manager.setRenderBackgroundStateExclusively(true);
	        }
	    });

	    button = new TextButton("Multiplayer", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    
	    button = new TextButton("Settings", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	}
	
	@Override
	public void update(float delta)
	{
		
	}

	@Override
	public void render(SpriteBatch b)
	{
		super.render(b);
		
		//Draw dim background
		Gdx.gl.glEnable(GL20.GL_BLEND);
		backgroundRenderer.begin(ShapeType.Filled);
		backgroundRenderer.setColor(0, 0, 0, .6f);
		backgroundRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundRenderer.end();
		
		//Draw UI
		stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	    
	}

}