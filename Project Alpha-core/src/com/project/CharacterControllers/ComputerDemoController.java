package com.project.CharacterControllers;

public class ComputerDemoController extends CharacterController {

	public ComputerDemoController(Character c) {
		super(c);
	}

	@Override
	public boolean shouldJump() {
		return false;
	}

	@Override
	public Direction walkDirection() {
		return Direction.None;
	}

}
