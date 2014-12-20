package com.project.GameStates.Menus.Multiplayer;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;

public class GameLocalMultiplayerMenu extends GameMenu {

	public GameLocalMultiplayerMenu(GameStateManager manager) {
		super(manager);
		
		TextButton button = new TextButton("Back", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(backButtonListener());
	}
	
	protected String header() {
		return "Local Server";
	}
}
