package com.project.Entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.project.Preferences.AppPreferences;
import com.project.constants.Constants;
import com.project.networking.Common.DamagePacket;
import com.project.networking.Common.Packet;
import com.project.networking.Common.PlayerUpdatePacket;
import com.project.networking.Common.ProjectilePacket;
import com.project.networking.Common.UserActionPacket;
import com.project.networking.MultiplayerGameSessionController;
import com.project.networking.MultiplayerListener;
import com.project.networking.MultiplayerServer;

public class EnemyManager implements MultiplayerListener
{
	private Player target;
	private ArrayList<Entity> players;
	private ArrayList<Projectile> projectiles;
	private ArrayList<ParticleEffect> rocketEffects;
	
	public EnemyManager(Player target){
		this.target = target;
		
		players = new ArrayList<>();
		projectiles = new ArrayList<Projectile>();
		rocketEffects = new ArrayList<ParticleEffect>();

		MultiplayerGameSessionController.sharedInstance().registerListener(UserActionPacket.class,this);
		MultiplayerGameSessionController.sharedInstance().registerListener(ProjectilePacket.class,this);
		MultiplayerGameSessionController.sharedInstance().registerListener(PlayerUpdatePacket.class,this);
		MultiplayerGameSessionController.sharedInstance().registerListener(DamagePacket.class, this);
	}
	
	
	public void addProjectile(Projectile b){
		projectiles.add(b);
	}
	
	public void addPlayer(Entity p){
		players.add(p);
	}

	public void removePlayer(Entity p)
	{
		if (!players.remove(p)) {
			System.out.println("Failed to remove player. Didn't hold player!");
		}
	}
	
	//jeder guckt selbst ob er getroffen ist
	public void update(float delta) {
		Iterator<Projectile> it = projectiles.iterator();
		
		while(it.hasNext()){
			Projectile p = (Projectile) it.next();
			
			p.update(delta);
			
			//projectile collision with walls
			if(target.isOutOfBoundsX(p.getX(), p.getWidth()) || target.isOutOfBoundsYDown(p.getY(), p.getHeight()) ||
				target.collisionXLeft(p.getX(), p.getY(), p.getWidth()) || target.collisionXRight(p.getX(), p.getY(), p.getWidth(), p.getHeight()) || 
				target.collisionYDown(p.getX(), p.getY(), p.getHeight())|| target.collisionYUp(p.getX(), p.getY(), p.getWidth(), p.getHeight())){
				
				
				//if player is in explosionRadius
				if(p instanceof Rocket){
					newRocketEffect((int) p.getX(), (int) p.getY());
					
					if(Constants.circleIntersectsRectangle(new Point((int)p.getX(), (int)p.getY()), p.getRadius(),
							new Point((int)target.getX(), (int)target.getY()), target.getWidth(), target.getHeight())){
						System.out.println("it hit you bam bam");
						//do damage
						hit(p);
					}
				
				}
				it.remove();
				continue;
			}
			
			//if it hits the player
			if (p.getBoundingRectangle().overlaps(target.getBoundingRectangle()) && !p.getIsMyOwn()){
				System.out.println("I was hit!");				
				
				hit(p);
				//target.decreaseLife((int) p.getDamage());
				
				it.remove();
			}	
		}
		
		//handle particles
		for(ParticleEffect effect : rocketEffects){
			
			effect.update(delta);
			//if(effect.isComplete())
				//rocketEffects.remove(effect);
		}

	}

	public void render(Batch b){
		for(Entity player : players)
			player.render(b);
		for(Projectile p : projectiles)
			p.render(b);
		for(ParticleEffect particle : rocketEffects)
			particle.draw(b);
		
	}

	private void hit(Projectile p) {
		//send hit to server here
		DamagePacket packet  = new DamagePacket();
		packet.targetID = -1;
		packet.hunterID = p.getOwnerID();
		packet.restLife = (byte) (target.life - p.getDamage());
		
		target.decreaseLife((int) p.getDamage());
		if(target.IAmDead()){
			System.out.println(playerWithID(packet.hunterID).name + " killed you. NOOOOOOOooOOOB");
			System.out.println("You should respawn now.");
		}
		
		if(MultiplayerGameSessionController.sharedInstance().isMultiplayerSessionActive()){
			System.out.println("sent packet");
			MultiplayerGameSessionController.sharedInstance().sendPacket(packet);
		}
	}
	
	public void sendNewBullet(Projectile p, byte projectileType){
		addProjectile(p);
		
		//send bullet information
		ProjectilePacket packet = new ProjectilePacket();
		packet.originX = p.getX();
		packet.originY = p.getY();
		packet.targetX = p.targetX;
		packet.targetY = p.targetY;
		packet.projectileType = projectileType;
		
		MultiplayerGameSessionController.sharedInstance().sendPacket(packet);
		//send information bout new bullet here
	}
	
	private void newRocketEffect(int x, int y) {
		
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal("effect/tapB.p"), Gdx.files.internal("effect"));
		e.scaleEffect(0.33f);
		e.setDuration(1000);
		e.setPosition(x, y);
		e.allowCompletion();
		e.start();
		
		rocketEffects.add(e);
	}

	@Override
	public void multiplayerSessionStarted(MultiplayerServer server)
	{

	}

	@Override
	public void multiplayerSessionEnded()
	{
		players.clear();
	}

	private Entity playerWithID(byte id)
	{
		for (Entity e : players) {
			if (e instanceof EnemyPlayer) {
				EnemyPlayer p = (EnemyPlayer)e;
				if (p.getID() == id)
					return p;
			}
		}
		return null;
	
	}

	@Override
	public void receivedPacket(Packet p)
	{
		if (p instanceof UserActionPacket)
		{
			UserActionPacket packet = (UserActionPacket)p;
			if (packet.action == UserActionPacket.Action.Join)
			{
				EnemyPlayer player = new EnemyPlayer(new Point(0, 0), packet.userName, packet.userID);
				addPlayer(player);
				System.out.println("Added player");
			}
			else if (packet.action == UserActionPacket.Action.Leave)
			{
				EnemyPlayer player = (EnemyPlayer) playerWithID(packet.userID);
				removePlayer(player);
				System.out.println("Removed player");
			}
		}
		else if (p instanceof PlayerUpdatePacket)
		{
			PlayerUpdatePacket packet = (PlayerUpdatePacket)p;

			EnemyPlayer player = (EnemyPlayer) playerWithID(packet.userID);
			if (player != null)
				player.update(packet.locationX, packet.locationY);
			else
				System.out.println("!!!!!!!!!!!!!!!!!");

		}
		else if(p instanceof ProjectilePacket)
		{
			ProjectilePacket packet = (ProjectilePacket)p;
			
			switch(packet.projectileType){
			case 0:
				addProjectile(new Bullet("img/rocket.png", packet.targetX, packet.targetY, packet.originX, packet.originY,
					 packet.userID, false));
				break;
			case 1:
				addProjectile(new Rocket("img/rocket.png", packet.targetX, packet.targetY, packet.originX, packet.originY,
					packet.userID, false));
				default:
				break;
			}
			
		}
		
		else if(p instanceof DamagePacket)
		{
			System.out.println("I received a hit");
			//jemand meldet dass jemand jemanden getroffen hat
			DamagePacket packet = (DamagePacket)p;

			String hasHitName;
			String wasHitName;

			Entity hasHit = playerWithID(packet.hunterID);
			hasHitName = hasHit == null ? AppPreferences.sharedInstance().getUserName() : hasHit.name;

			Entity wasHit = playerWithID(packet.targetID);
			wasHitName = wasHit.name;
			
			System.out.println(hasHitName + "hit " + wasHitName + " with " + (wasHit.life - (100 - packet.restLife)) + " damage");
			System.out.println("Update dat bitch bruh: " + packet.restLife);
			wasHit.life = packet.restLife;

		}
	}
}
