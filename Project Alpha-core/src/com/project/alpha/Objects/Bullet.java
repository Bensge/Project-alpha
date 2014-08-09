package com.project.alpha.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.project.alpha.input.InputManager.PlayerDirection;

public class Bullet extends Sprite{
	
	private Vector2 velocity;
	private float speed;
	private float factor = (float) (1f / Math.sqrt(2));
	
	public Bullet(float posX, float posY, PlayerDirection direction){
		super(new Sprite(new Texture(Gdx.files.internal("img/rocket.png"))));
		velocity = new Vector2();
		speed = 300;
		setX(posX);
		setY(posY);
		setSize(5, 5);
		switch(direction){
		case Left:
			velocity.x = -speed;
			velocity.y = 0;
			break;
		case Right:
			velocity.x = speed;
			velocity.y  = 0;
			break;
		case Up:
			velocity.x = 0;
			velocity.y = speed;
			break;
		case Down:
			velocity.x = 0;
			velocity.y = -speed;
			break;
		case DownLeft:
			velocity.x = -speed * factor;
			velocity.y = velocity.x;
			break;
		case DownRight:
			velocity.x = speed * factor;
			velocity.y = -velocity.x;
			break;
		case UpLeft:
			velocity.x = -speed * factor;
			velocity.y = -velocity.x;
			break;
		case UpRight:
			velocity.x = speed * factor;
			velocity.y = velocity.x;
			break;
		default:
			break;
		}
	}
	
	public void draw(SpriteBatch batch){
		super.draw(batch);
	}
	
	public void update(float delta){
		setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
		
	}
}
