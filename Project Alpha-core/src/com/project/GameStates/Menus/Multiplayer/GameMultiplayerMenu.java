package com.project.GameStates.Menus.Multiplayer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;

public class GameMultiplayerMenu extends GameMenu {
	
	public GameMultiplayerMenu(GameStateManager m)
	{
		super(m);
		
	    //Add buttons
	    TextButton button = new TextButton("Local Area Server", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    button.addListener(new ChangeListener() {
	    	public void changed(ChangeEvent event, Actor actor) {
	    		pushMenu(GameLocalMultiplayerMenu.class);
	    	}
		});
	    
	    button = new TextButton("Remote Server", style);
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				pushMenu(GameRemoteMultiplayerMenu.class);
			}
		});
	    
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
