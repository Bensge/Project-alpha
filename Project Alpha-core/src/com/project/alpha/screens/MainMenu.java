package com.project.alpha.screens;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.project.alpha.Main;

public class MainMenu implements Screen {
	
	private Stage stage;
	private Skin skin;
	private BitmapFont font;
	private Table table;
	private TextButtonStyle buttonStyle;
	private TextButton topButton;
	private TextButton middleButton;
	private TextButton bottomButton;
	
	public MainMenu(){
		
		stage = new Stage(new ScreenViewport());
		skin = new Skin();
		font = new BitmapFont();
		table = new Table();
		buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;
        //textButtonStyle.up = skin.getDrawable("up-button");
        //textButtonStyle.down = skin.getDrawable("down-button");
        //textButtonStyle.checked = skin.getDrawable("checked-button");
        
        //Buttons
		topButton = new TextButton("Start game", buttonStyle);
		final MainMenu menu = this;
		topButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		        System.out.println("Start game!!!");
		        Main.sharedInstance().screenWantsDismissal(menu);
		    }
		});
		middleButton = new TextButton("Multiplayer", buttonStyle);
		middleButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		        System.out.println("Multiplayer!!!");
		    }
		});
		bottomButton = new TextButton("Options", buttonStyle);
		bottomButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		        System.out.println("Options!!!");
		    }
		});
		//Layout
		table.add(topButton).expandX().spaceBottom(50);
		table.row();
		table.add(middleButton).expandX().spaceBottom(50);
		table.row();
		table.add(bottomButton).expandX().spaceBottom(50);
		stage.addActor(table);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		table.setWidth(width);
		table.setHeight(height);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
