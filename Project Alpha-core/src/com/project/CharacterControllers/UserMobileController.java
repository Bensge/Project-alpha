package com.project.CharacterControllers;

import com.badlogic.gdx.math.Vector2;
import com.project.Overlays.JoystickOverlay;
import com.project.Overlays.Overlay;
import com.project.Overlays.OverlayContainer;

public class UserMobileController extends CharacterController
{
	private Direction walkingDirection = Direction.None;
	private boolean shouldJump;
	private boolean shouldShootBullet;
	private boolean shouldShootRocket;
	private Vector2 projectileDirection;

	private JoystickOverlay leftJoystick;
	private JoystickOverlay rightJoystick;

	public UserMobileController(Character c)
	{
		super(c);

		projectileDirection = new Vector2(0,0);

		leftJoystick = new JoystickOverlay(Overlay.ScreenCorner.BottomLeft);
		OverlayContainer.sharedInstance().addOverlay(leftJoystick);

		rightJoystick = new JoystickOverlay(Overlay.ScreenCorner.BottomRight);
		OverlayContainer.sharedInstance().addOverlay(rightJoystick);
	}

	@Override
	public boolean shouldJump() {
		return shouldJump;
	}

	@Override
	public Direction walkDirection() {
		return walkingDirection;
	}

	@Override
	public void update()
	{
		shouldJump = leftJoystick.position.y > 0.6;

		float x = leftJoystick.position.x;
		if (x < -0.5) {
			walkingDirection = Direction.Left;
		}
		else if (x > 0.5) {
			walkingDirection = Direction.Right;
		}
		else {
			walkingDirection = Direction.None;
		}

		//Projectiles
		shouldShootRocket = false;
		shouldShootBullet = rightJoystick.isTouched;

		projectileDirection = rightJoystick.position;
	}

	@Override
	public boolean shouldShootBullet()
	{
		return shouldShootBullet;
	}

	@Override
	public boolean shouldShootRocket()
	{
		return shouldShootRocket;
	}

	@Override
	public Vector2 projectileTarget()
	{
		return projectileDirection;
	}

	@Override
	public boolean isProjectileTargetRelativeToPlayer()
	{
		return true;
	}
}
