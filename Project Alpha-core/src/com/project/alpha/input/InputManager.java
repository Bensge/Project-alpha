package com.project.alpha.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.GraphicsType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input;

public class InputManager implements InputProcessor {
	
	PlayerDirection shootDirection = PlayerDirection.Down;
	
	public enum PlayerDirection {
		None,
		Up,
		Down,
		Left,
		Right,
		UpLeft,
		UpRight,
		DownLeft,
		DownRight
	}
	
	boolean isMouseMode;
	
	long shootTime = 0;
	
	
	boolean isAPressed = false, isSPressed = false, isWPressed = false, isDPressed = false, isSpacePressed = false;
	
	
	float[] joystickLastTouchPosition;
	float[] joystickKnobDelta = new float[2];
	final float joystickThreshold = 20.f;
	
	private static InputManager instance = null;
	
	public static InputManager sharedInstance() {
		if (instance == null) {
			instance = new InputManager();
		}
		return instance;
	}
	
	
	public InputManager() {
		Gdx.input.setInputProcessor(this);
		
		if (Gdx.app.getType() == ApplicationType.iOS || Gdx.app.getType() == ApplicationType.Android){
			//Joystick
			isMouseMode = false;
		}
		else
			isMouseMode = true;
		
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		switch (keycode) {
			case Keys.A:
				shootDirection = PlayerDirection.Left;
				isAPressed = true;
				break;
			case Keys.S:
				shootDirection = PlayerDirection.Down;
				isSPressed = true;
				break;
			case Keys.D:
				shootDirection = PlayerDirection.Right;
				isDPressed = true;
				break;
			case Keys.W:
				shootDirection = PlayerDirection.Up;
				isWPressed = true;
				break;
			case Keys.SPACE:
				isSpacePressed = true;
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		switch (keycode) {
			case Keys.A:
				isAPressed = false;
				break;
			case Keys.S:
				isSPressed = false;
				break;
			case Keys.D:
				isDPressed = false;
				break;
			case Keys.W:
				isWPressed = false;
				break;
			case Keys.SPACE:
				isSpacePressed = false;
				break;
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		joystickKnobDelta[0] = 0;
		joystickKnobDelta[1] = 0;
		joystickLastTouchPosition = new float[2];
		joystickLastTouchPosition[0] = screenX;
		joystickLastTouchPosition[1] = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		joystickKnobDelta[0] = 0;
		joystickKnobDelta[1] = 0;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		joystickKnobDelta[0] = screenX - joystickLastTouchPosition[0];
		if (joystickKnobDelta[0] > 30)
			joystickKnobDelta[0] = 30;
		else if (joystickKnobDelta[0] < -30)
			joystickKnobDelta[0] = -30;
		
		joystickKnobDelta[1] = joystickLastTouchPosition[1] - screenY;
		if (joystickKnobDelta[1] > 30)
			joystickKnobDelta[1] = 30;
		else if (joystickKnobDelta[1] < -30)
			joystickKnobDelta[1] = -30;
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////
	///////////                  EASY PUBLIC ACCESSOR METHODS              ////////////////
	///////////////////////////////////////////////////////////////////////////////////////
	
	public PlayerDirection getShootDirection(){
		return shootDirection;
	}
	
	public PlayerDirection getPlayDirection() {
		
		boolean right = false;
		boolean left = false;
		boolean up = false;
		boolean down = false;
		
		if (isMouseMode) {
			
			right = isDPressed;
			left = isAPressed;
			up = isWPressed;
			down = isSPressed;
			
		}
		else {
			
			if (Math.abs(joystickKnobDelta[0]) > joystickThreshold) {
				//Left or Right
				if (joystickKnobDelta[0] > 0)
					//Right
					right = true;
				else
					//Left
					left = true;
			}
			if (Math.abs(joystickKnobDelta[1]) > joystickThreshold) {
				//Up or down
				if (joystickKnobDelta[1] > 0)
					//Up
					up = true;
				else
					//Down
					down = true;
			}
		}
		
		//Identify Direction enum value and return
			
		if (up == true) {
			if (left == true){
				shootDirection = PlayerDirection.UpLeft;
				return PlayerDirection.UpLeft;
			}
			if (right == true){
				shootDirection = PlayerDirection.UpRight;
				return PlayerDirection.UpRight;
			}
			return PlayerDirection.Up;
		}
		else if (down == true) {
			if (left == true){
				shootDirection = PlayerDirection.DownLeft;
				return PlayerDirection.DownLeft;
			}
			if (right == true){
				shootDirection = PlayerDirection.DownLeft;
				return PlayerDirection.DownRight;
			}
			return PlayerDirection.Down;
		}
		else if (left == true)
			return PlayerDirection.Left;
		else if (right == true)
			return PlayerDirection.Right;
		return PlayerDirection.None;
		
	}
	
	public float[] getJoystickDelta() {
		return joystickKnobDelta;
	}
	
	public boolean getShouldShoot() {
		
		final int shotsPerSecond = 2;
		
		if (isMouseMode){
			if (isSpacePressed) {
				if (System.currentTimeMillis() - shootTime > 1000/shotsPerSecond) {
					shootTime = System.currentTimeMillis();
					return true;
				}
				else
					return false;
			}
				
			return isSpacePressed;
		}
		else
			return true;
	}
	
}
