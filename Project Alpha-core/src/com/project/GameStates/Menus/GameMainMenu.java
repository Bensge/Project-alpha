package com.project.GameStates.Menus;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.project.GameStates.GameState;
import com.project.GameStates.GameStateManager;
import com.project.constants.Constants;
import com.project.main.Transition;

public class GameMainMenu extends GameMenu {

	
	private Transition undimTransition;
	
	public GameMainMenu(GameStateManager m)
	{
		super(m);
		
		//Add buttons
	    TextButton button = new TextButton("Single Player", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(new ChangeListener() {
	        public void changed (ChangeEvent event, Actor actor) {
	        	undimTransition = new Transition() {
					public float duration() {
						return 0.6f;
					}
					public void completion() {
						manager.setRenderBackgroundStateExclusively(true);
					}
				};
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
		//Transition stuff
		float alpha = 1.f;
		if (undimTransition != null)
		{
			alpha = 1.f - undimTransition.progress();
			if (undimTransition.isCompleted())
			{
				undimTransition = null;
			}
		}
		
		table.setColor(1, 1, 1, alpha);
		
		super.render(b);
	}

	@Override
	public void dispose() {
		super.dispose();
		//We probably won't ever dispose the main menu, so why even bother about it ~yolo
	}

}
