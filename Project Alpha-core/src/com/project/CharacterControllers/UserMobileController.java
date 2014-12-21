package com.project.CharacterControllers;

import java.awt.Point;

import com.badlogic.gdx.Gdx;

public class UserMobileController extends CharacterController {
	
	private Direction walkingDirection = Direction.None;

	public UserMobileController(Character c) {
		super(c);
	}

	@Override
	public boolean shouldJump() {
		return Gdx.input.isTouched();
	}

	@Override
	public Direction walkDirection() {
		return walkingDirection;
	}

	@Override
	public void update() {
		//As it turns out, a value of 0 of the accelerometer's Y axis means the device is in landscape right mode.
		float accelY = Gdx.input.getAccelerometerY();
		
		final float ACCELEROMETER_THRESHOLD = 1.5f;
		
		if (accelY > -ACCELEROMETER_THRESHOLD && accelY < ACCELEROMETER_THRESHOLD)
			walkingDirection = Direction.None;
		else if (accelY <= -ACCELEROMETER_THRESHOLD)
			walkingDirection = Direction.Right;
		else if (accelY >= ACCELEROMETER_THRESHOLD)
			walkingDirection = Direction.Left;
	}


}
