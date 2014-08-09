package com.project.alpha.input;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.GraphicsType;

public class InputManager implements InputProcessor {
	
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
	
	float[] joystickLastTouchPosition;
	float[] joystickKnobDelta = new float[2];
	final float joystickThreshold = 10.f;

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
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
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
	
	
	public PlayerDirection getPlayDirection() {
		if (isMouseMode) {
			System.out.println("UNIMPLEMENTED!!!!!");
			return PlayerDirection.None;
		}
		else {
			boolean right = false;
			boolean left = false;
			boolean up = false;
			boolean down = false;
			
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
			
			if (up == true) {
				if (left == true)
					return PlayerDirection.UpLeft;
				if (right == true)
					return PlayerDirection.UpRight;
				return PlayerDirection.Up;
			}
			else if (down == true) {
				if (left == true)
					return PlayerDirection.DownLeft;
				if (right == true)
					return PlayerDirection.DownRight;
				return PlayerDirection.Down;
			}
			else if (left == true)
				return PlayerDirection.Left;
			else if (right == true)
				return PlayerDirection.Right;
			return PlayerDirection.None;
		}
		
	}
	
	public float[] getJoystickDelta() {
		return joystickKnobDelta;
	}
	
}
