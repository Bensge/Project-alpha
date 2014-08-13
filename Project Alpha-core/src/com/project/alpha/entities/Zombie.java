package com.project.alpha.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
		
		System.out.println("xDinstance = " + xDistance);
		System.out.println("yDinstance = " + yDistance);
		
		//TODO: handle yDistance == 0 case
		float axesRatio = xDistance / yDistance;
		
		System.out.println("axesRatio = " + axesRatio);
		
		//Super special magical formulas from James 
		// c = runDistance
		// a = x Axis
		// b = y Axis
		// a = +- sqrt((x^2 * c^2)/(x^2+1))
		// b = +- sqrt(c^2 / (x^2 + 1))
		
		float xPowerTwo = axesRatio * axesRatio;
		float cPowerTwo = runDistance * runDistance;
		
		System.out.println("xPowerTwo = " + xPowerTwo);
		System.out.println("cPowerTwo = " + cPowerTwo);
		
		float xRoot = (xPowerTwo * cPowerTwo) / (xPowerTwo + 1);
		float yRoot = cPowerTwo / (xPowerTwo + 1);
		
		System.out.println("xRoot = " + xRoot);
		System.out.println("yRoot = " + yRoot);
		
		float xMovement = (float) Math.sqrt(xRoot);
		float yMovement = (float) Math.sqrt(yRoot);
		
		System.out.println("xMovement = " + xMovement);
		System.out.println("yMovement = " + yMovement);
		//MOVE MOVE MOVE!!!!!!!!!!!!!!!!!!!!
		
		velocity.x = (playerX > getX()) ? xMovement : -xMovement;
		velocity.y = (playerY > getY()) ? yMovement : -yMovement; 
		
		setX(getX() + velocity.x);
		setY(getY() + velocity.y);
		
	}
	
	@Override
	public void draw(Batch batch) {
		float tmpX = getX();
		float tmpY = getY();
		
		if(tmpX > playerX - 2 && tmpX < playerX + 2)
			setX((int) playerX);
		if(tmpY > playerY - 2 && tmpY < playerY + 2)
			setY((int) playerY);
		
		super.draw(batch);
		setX(tmpX);
		setY(tmpY);
	}
	
	@Override
	public void renderStuff(){
		
		shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.re
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(getX(), getY(), getWidth(), 3);
		shapeRenderer.end();
		
	}
}
