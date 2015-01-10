package com.project.GameStates.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.GameStates.GameState;
import com.project.GameStates.GameStateManager;
import com.project.UI.Table;
import com.project.common.Constants;

public abstract class GameMenu extends GameState {
	
	protected Table table;
	protected Stage stage;
	protected ShapeRenderer backgroundRenderer;
	
	protected LabelStyle headerStyle;
	protected TextButtonStyle style;
	
	protected boolean backgroundShouldUseStageAlpha = false;
	
	final static float TRANSITION_DURATION = 0.6f;
	
	public GameMenu(GameStateManager manager)
	{
		super(manager);
		
		//Create stage
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Create table
	    table = new Table();
	    table.setFillParent(true);
	    
	    //Setup stage
	    stage.addActor(table);
	    
	    //Setup renderer
	    backgroundRenderer = new ShapeRenderer();
	    
	    //Setup styles
	    style = new TextButtonStyle();
	    style.font = Constants.menlo32Font;
	    
	    headerStyle = new LabelStyle(Constants.menlo50Font, new Color(200, 10, 50, 255));
	    
	    //Setup table header
	    Label header = new Label(header(), headerStyle);
  		
  		table.row().pad(60);
  		table.add(header);
  		
  	}
	
	protected String header()
	{
		return "Header";
	}

	@Override
	public void update(float delta) {
		stage.act(delta);
	}
	
	@Override
	public void render(SpriteBatch b)
	{
		super.render(b);
		
		float alpha = backgroundShouldUseStageAlpha ? stage.getRoot().getColor().a : 1;
		//Draw dim background
		final float dimValue = .4f;
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		backgroundRenderer.begin(ShapeType.Filled);
		backgroundRenderer.setColor(0, 0, 0, dimValue * alpha);
		backgroundRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backgroundRenderer.end();
	    stage.draw();
	}

	@Override
	public void dispose()
	{
		stage.dispose();
		backgroundRenderer.dispose();
	}
	
	public void setIsInBackground(boolean bg)
	{
		super.setIsInBackground(bg);
		if (!bg)
			Gdx.input.setInputProcessor(stage);
	}
	
	/*
	 * Helper methods
	 * */
	
	//At first I tried caching this Listener, but as it turned out, ChangeListeners aren't meant to be reused :(
	//Memory efficiency --;
	protected ChangeListener backButtonListener() {
		return new ChangeListener() {
	        public void changed (ChangeEvent event, Actor actor) {
	        	stage.addAction(Actions.sequence(
	        			Actions.fadeOut(TRANSITION_DURATION / 2),
	        			new Action() {
							public boolean act(float delta) {
								manager.remove();
								((GameMenu)manager.topMostState()).stage.addAction(Actions.sequence(Actions.alpha(0),Actions.fadeIn(TRANSITION_DURATION / 2)));
								return true;
							}
	        			}
	        		)
	        	);
	        }
	    };
	}
	
	//Convenience method for animated pushing of a new menu
	//
	//I know literally everyone but me can't stand java reflection runtime APIs. I'm sorry, I can't help it, this time it was really the only way to do this.
	//Simply passing in an instance of a subclass of GameState is no option, because it results in a brief flickering and change of the global viewport on retina devices.
	//The only option known to me to work around that bug is to create the new GameMenu in the middle of the transition, when the old menu is already fully faded out.
	//And that's why I ended up using the new Menu's class instead of an instance of it, creating an instance of said class at the right time.
	//Now go ahead and yell at me for using reflection
	
	protected void pushMenu(Class<? extends GameMenu> menuClass)
	{
		final Class<? extends GameMenu> mClass = menuClass; //Because anonymous classes are stupid
		
		stage.addAction(Actions.sequence(Actions.alpha(1),Actions.fadeOut(TRANSITION_DURATION / 2),new Action() {
			public boolean act(float delta)
			{
				GameMenu menu = null;
				//Try&catch because type safety and exceptions are awesome and... actually no.
				try {
					menu = (GameMenu)mClass.getConstructors()[0].newInstance(manager);
				} catch (Exception e) {
					e.printStackTrace();
				}
				menu.stage.addAction(Actions.alpha(0));
				//Update stage to apply alpha ^ 
				menu.stage.act();
				manager.push(menu);
				menu.stage.addAction(Actions.fadeIn(TRANSITION_DURATION / 2));
				return true;
			}
		},Actions.alpha(1)));
	}

}
