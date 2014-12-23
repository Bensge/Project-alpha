package com.project.Entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.project.constants.Constants;

public class EnemyManager {

	private Player target;
	private ArrayList<Player> player;
	private ArrayList<Projectile> projectiles;
	
	public EnemyManager(Player target){
		this.target = target;
		
		player = new ArrayList<Player>();
		projectiles = new ArrayList<Projectile>();
	}
	
	
	public void addProjectile(Projectile b){
		projectiles.add(b);
	}
	
	public void addPlayer(Player p){
		player.add(p);
	}
	
	public void update(float delta) {
		Iterator<Projectile> it = projectiles.iterator();
		
		while(it.hasNext()){
			Projectile p = (Projectile) it.next();
			
			p.update(delta);
			
			//projectile collision with walls
			if(target.isOutOfBoundsX(p.getX(), p.getWidth()) || target.isOutOfBoundsY(p.getY(), p.getHeight()) ||
				target.collisionXLeft(p.getX(), p.getY(), p.getWidth()) || target.collisionXRight(p.getX(), p.getY(), p.getWidth(), p.getHeight()) || 
				target.collisionYDown(p.getX(), p.getY(), p.getHeight())|| target.collisionYUp(p.getX(), p.getY(), p.getWidth(), p.getHeight())){
				
				
				//if player is in explosionRadius
				if(p.getRadius() != 0 && !(p.getOwner().equals(target))){
					if(Constants.circleIntersectsRectangle(new Point((int)p.getX(), (int)p.getY()), p.getRadius(),
							new Point((int)p.getX(), (int)p.getY()), p.getWidth(), p.getHeight())){
						System.out.println("wall hit");
						//do damage
						hit(p);
					}
				
				}
				it.remove();
				continue;
			}
			
			//if it hits the player
			if(p.getBoundingRectangle().overlaps(target.getBoundingRectangle()) && !(p.getOwner().equals(target))){
				System.out.println("I was hit!");				
				
				hit(p);
				target.decreaseLife((int) p.getDamage());
				
				it.remove();
			}
		}
	}

	public void render(Batch b){
		for(Player play : player)
			play.render(b);
		for(Projectile p : projectiles)
			p.render(b);
	}

	private void hit(Projectile p) {
		//send hit to server here
		
		target.decreaseLife((int) p.getDamage());
		
	}
	
	public void sendNewBullet(Projectile p){
		addProjectile(p);
		//send information bout new bullet here
	}
}
