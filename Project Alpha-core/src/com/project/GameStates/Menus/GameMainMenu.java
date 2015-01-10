package com.project.GameStates.Menus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.Multiplayer.GameMultiplayerMenu;
import com.project.GameStates.Menus.Settings.SettingsMenu;

public class GameMainMenu extends GameMenu {

	public GameMainMenu(GameStateManager m)
	{
		super(m);
		
		//Add buttons
	    TextButton button = new TextButton("Single Player", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(new ChangeListener() {
	        public void changed (ChangeEvent event, Actor actor) {
	        	stage.addAction(Actions.sequence(
        			new Action() {
    					public boolean act(float delta) {
    						backgroundShouldUseStageAlpha = true;
    						return true;
    					}
    				},
		        	Actions.fadeOut(.6f),
		        	new Action() {
						public boolean act(float delta) {
							manager.setRenderBackgroundStateExclusively(true);
							backgroundShouldUseStageAlpha = false;
							return true;
						}
					}
    			));
	        }
	    });

	    button = new TextButton("Multiplayer", style);
	    button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				pushMenu(GameMultiplayerMenu.class);
			}
		});
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	    
	    button = new TextButton("Settings", style);
		button.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				pushMenu(SettingsMenu.class);
			}
		});
	    table.row().pad(20);
	    table.add(button);
	    table.row();
	}
	
	@Override
	protected String header() {
		return "Project alpha";
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
		//We probably won't ever dispose the main menu, so why even bother about it ~yolo
	}

}
