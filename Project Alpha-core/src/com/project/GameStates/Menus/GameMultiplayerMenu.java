package com.project.GameStates.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.GameStates.GameState;
import com.project.GameStates.GameStateManager;
import com.project.constants.Constants;
import com.project.main.Transition;

public class GameMultiplayerMenu extends GameMenu {
	
	public GameMultiplayerMenu(GameStateManager m)
	{
		super(m);
		
	    //Add buttons
	    TextButton button = new TextButton("Local Area Server", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    
	    button = new TextButton("Remote Server", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    
	    button = new TextButton("Back", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(backButtonListener());
	}
	
	@Override
	protected String header() {
		return "Multiplayer";
	}

	@Override
	public void update(float delta)
	{
		super.update(delta);
	}
	
	@Override
	public void render(SpriteBatch b)
	{
		super.render(b);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
