package com.project.alpha.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Joystick extends Sprite {
	
	Sprite joystickBackgroundSprite;
	Sprite joystickKnobSprite;
	SpriteBatch joystickSpriteBatch;
	
	
	public Joystick() {
		
		//Joystick links unten, 20 % Höhe und 20 % Breite
			
		//float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		
		Texture joystickBackgroundTexture = new Texture(Gdx.files.internal("Joystick/Joystick_BG.png"));
		Texture joystickKnobTexture = new Texture(Gdx.files.internal("Joystick/Joystick_knob.png"));
		joystickBackgroundSprite = new Sprite(joystickBackgroundTexture);
		joystickKnobSprite = new Sprite(joystickKnobTexture);
		joystickBackgroundSprite.setBounds(screenWidth * 0.1f,screenWidth * 0.1f,screenWidth * 0.2f,screenWidth * 0.2f);
		updateJoystickKnobPosition();
		joystickSpriteBatch = new SpriteBatch();
	}
	
	
	public void render(SpriteBatch batch){
		//Joystick
		updateJoystickKnobPosition();
		joystickSpriteBatch.begin();
		joystickBackgroundSprite.draw(joystickSpriteBatch);
		joystickKnobSprite.draw(joystickSpriteBatch);
		joystickSpriteBatch.end();
	}
	
	private void updateJoystickKnobPosition() {
		float[] joystickKnobDelta = InputManager.sharedInstance().getJoystickDelta();
		float knobSize = Gdx.graphics.getWidth() * 0.05f;
		joystickKnobSprite.setBounds(joystickBackgroundSprite.getX() + joystickBackgroundSprite.getWidth() / 2 - knobSize / 2 + joystickKnobDelta[0], joystickBackgroundSprite.getY() + joystickBackgroundSprite.getHeight() / 2 - knobSize / 2  + joystickKnobDelta[1], knobSize, knobSize);
	}
	
	
	
}
