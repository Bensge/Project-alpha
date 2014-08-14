package com.project.alpha.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.project.alpha.screens.AlphaGame;

public class Zombie extends Enemy{

	public Zombie() {
		super("img/enemy.png");
		
		speed = 50;
	}

	@Override
	public void update(float playerX, float playerY, float delta) {
		super.update(playerX, playerY, delta);
		
		//Total distance to move, in both axes
		float runDistance = speed * delta;
		
		float xDistance = Math.abs(playerX - getX());
		float yDistance = Math.abs(playerY - getY());
		
		//TODO: handle yDistance == 0 case
		float axesRatio = xDistance / yDistance;
		
		//Super special magical formulas from James 
		// c = runDistance
		// a = x Axis
		// b = y Axis
		// a = +- sqrt((x^2 * c^2)/(x^2+1))
		// b = +- sqrt(c^2 / (x^2 + 1))
		
		float xPowerTwo = axesRatio * axesRatio;
		float cPowerTwo = runDistance * runDistance;
	
		float xRoot = (xPowerTwo * cPowerTwo) / (xPowerTwo + 1);
		float yRoot = cPowerTwo / (xPowerTwo + 1);
		
		float xMovement = (float) Math.sqrt(xRoot);
		float yMovement = (float) Math.sqrt(yRoot);
		
		//MOVE MOVE MOVE!!!!!!!!!!!!!!!!!!!!
		
		velocity.x = (playerX > getX()) ? xMovement : -xMovement;
		velocity.y = (playerY > getY()) ? yMovement : -yMovement; 
		
		setX(getX() + velocity.x);
		setY(getY() + velocity.y);
		
	}
	
	@Override
	public void draw(Batch batch) {
		super.draw(batch);
	}
	
	@Override
	public void renderStuff(){
	
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		
		Camera camera = AlphaGame.getInstance().camera;
		
		Vector3 windowPosition = camera.project(new Vector3(getX(), getY() + getHeight(), 0));
		Vector3 playerWidthVector3 =  camera.project(new Vector3(getWidth(), 0, 0));
		
		shapeRenderer.rect(windowPosition.x, windowPosition.y, playerWidthVector3.x, 3);
		shapeRenderer.end();
		
	}
}
