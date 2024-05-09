package com.withoutstudios.bushido.map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.withoutstudios.bushido.entity.GravityBlock;
import com.withoutstudios.bushido.entity.Ladder;
import com.withoutstudios.bushido.entity.Player;
import com.withoutstudios.bushido.entity.enemy.Enemy;
import com.withoutstudios.bushido.entity.enemy.FireWorm;
import com.withoutstudios.bushido.entity.projectile.Projectile;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase es la encargada de detectar y manejar las colisiones de los cuerpos de BOX2D
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class CollisionListener implements ContactListener {

	public CollisionListener() {
		
	}

	@Override
	public void beginContact(Contact contact) {
		
		if(!GameManifest.RESET_MAP) {
			Fixture fixA = contact.getFixtureA();
			Fixture fixB = contact.getFixtureB();
			
			int id = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
			switch(id) {
			case GameManifest.CATEGORY_SENSOR_FOOT | GameManifest.CATEGORY_LAND:
				if((fixA.getUserData() instanceof Player)||(fixB.getUserData() instanceof Player)) {
					if(fixA.getUserData() instanceof Player) {
						((Player) fixA.getUserData()).setJump(true);
					}
					else if(fixB.getUserData() instanceof Player) {
						((Player) fixB.getUserData()).setJump(true);
					}
					
					GameManifest.PLAYER.setLand(true);
				}
				break;
			case GameManifest.CATEGORY_SENSOR_FOOT | GameManifest.CATEGORY_ITEM:
				if((fixA.getUserData() instanceof Player)||(fixB.getUserData() instanceof Player)) {
					if(fixA.getUserData() instanceof Player) {
						((Player) fixA.getUserData()).setJump(true);
					}
					else if(fixB.getUserData() instanceof Player) {
						((Player) fixB.getUserData()).setJump(true);
					}
					GameManifest.PLAYER.setItem(true);
				}
				break;
			case GameManifest.CATEGORY_ENEMY | GameManifest.CATEGORY_LAND:
				if((fixA.getUserData() instanceof Enemy)||(fixB.getUserData() instanceof Enemy)) {
					if(fixA.getUserData() instanceof Enemy) {
						((Enemy) fixA.getUserData()).changeDirection();
					}
					else if(fixB.getUserData() instanceof Enemy) {
						((Enemy) fixB.getUserData()).changeDirection();
					}
				}
				break;
			case GameManifest.CATEGORY_PROJECTIL | GameManifest.CATEGORY_LAND:
				projectileCollision(fixA, fixB, false);
				break;
			case GameManifest.CATEGORY_PROJECTIL | GameManifest.CATEGORY_ENEMY:
				projectileCollision(fixA, fixB, true);
				break;
			case GameManifest.CATEGORY_PROJECTIL | GameManifest.CATEGORY_PROJECTIL:
				projectileCollision(fixA, fixB, false);
				break;
			case GameManifest.CATEGORY_PROJECTIL | GameManifest.CATEGORY_PLAYER:
				projectileCollisionEnemy(fixA, fixB, true);
				break;
			case GameManifest.CATEGORY_LADDER | GameManifest.CATEGORY_PLAYER:
				playerLadder(fixA, fixB, true, 0);
				break;
			case GameManifest.CATEGORY_SENSOR_VISION | GameManifest.CATEGORY_PLAYER:
				gravityBlock(fixA, fixB);
				break;
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		
		if(!GameManifest.RESET_MAP) {
			Fixture fixA = contact.getFixtureA();
			Fixture fixB = contact.getFixtureB();
			
			int id = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
			switch(id) {
			case GameManifest.CATEGORY_SENSOR_FOOT | GameManifest.CATEGORY_LAND:
				if((fixA.getUserData() instanceof Player)||(fixB.getUserData() instanceof Player)) {
					if(!GameManifest.PLAYER.isItem()) {
						if(fixA.getUserData() instanceof Player) {
							((Player) fixA.getUserData()).setJump(false);
					    }
					    else if(fixB.getUserData() instanceof Player) {
						    ((Player) fixB.getUserData()).setJump(false);
					    }
					    GameManifest.PLAYER.setLand(false);
					}
				}
				else if((fixA.getUserData() instanceof Enemy)||(fixB.getUserData() instanceof Enemy)) {
					if(fixA.getUserData() instanceof Enemy) {
						((Enemy) fixA.getUserData()).changeDirection();
					}
					else if(fixB.getUserData() instanceof Enemy) {
						((Enemy) fixB.getUserData()).changeDirection();
					}
				}
				break;
			case GameManifest.CATEGORY_SENSOR_FOOT | GameManifest.CATEGORY_ITEM:
				if((fixA.getUserData() instanceof Player)||(fixB.getUserData() instanceof Player)) {
					if(!GameManifest.PLAYER.isLand()) {
						if(fixA.getUserData() instanceof Player) {
							((Player) fixA.getUserData()).setJump(false);
						}
						else if(fixB.getUserData() instanceof Player) {
							((Player) fixB.getUserData()).setJump(false);
						}
						GameManifest.PLAYER.setItem(false);
					}
				}
				break;
			case GameManifest.CATEGORY_LADDER | GameManifest.CATEGORY_PLAYER:
				playerLadder(fixA, fixB, false, 1);
				break;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int id = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch(id) {
		case GameManifest.CATEGORY_SENSOR_FOOT | GameManifest.CATEGORY_LAND:
			if((fixA.getUserData() instanceof Player)||(fixB.getUserData() instanceof Player)) {
				if(fixA.getUserData() instanceof Player) {
					((Player) fixA.getUserData()).setJump(true);
				}
				else if(fixB.getUserData() instanceof Player) {
					((Player) fixB.getUserData()).setJump(true);
				}
			}
			break;
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	private void projectileCollision(Fixture fixA, Fixture fixB, boolean enemy) {
		if(fixA.getUserData() instanceof Projectile) {
			Projectile Projectile = (Projectile) fixA.getUserData();
			Projectile.setOperative(false);
			
			if(enemy) {
				if(fixB.getUserData() instanceof FireWorm) {
					((Enemy) fixB.getUserData()).takeDamage(0);
				}
				else {
					((Enemy) fixB.getUserData()).takeDamage(Projectile.getAttack());
				}
			}
		}
		else if(fixB.getUserData() instanceof Projectile) {
			Projectile Projectile = (Projectile) fixB.getUserData();
			Projectile.setOperative(false);
			
			if(enemy) {
				
				if(fixA.getUserData() instanceof FireWorm) {
					((Enemy) fixA.getUserData()).takeDamage(0);
				}
				else {
					((Enemy) fixA.getUserData()).takeDamage(Projectile.getAttack());
				}
			}
		}
		else if(fixA.getUserData() instanceof GravityBlock) {
			((GravityBlock) fixA.getUserData()).setDamage(0);
			((GravityBlock) fixA.getUserData()).getBody().setLinearVelocity(((GravityBlock) fixA.getUserData()).getBody().getLinearVelocity().x, 0);
			((GravityBlock) fixA.getUserData()).setDead(true);
		}
		else if(fixB.getUserData() instanceof GravityBlock) {
			((GravityBlock) fixB.getUserData()).setDamage(0);
			((GravityBlock) fixB.getUserData()).getBody().setLinearVelocity(((GravityBlock) fixB.getUserData()).getBody().getLinearVelocity().x, 0);
			((GravityBlock) fixB.getUserData()).setDead(true);
		}
	}
	
	private void projectileCollisionEnemy(Fixture fixA, Fixture fixB, boolean player) {
		if(fixA.getUserData() instanceof Projectile) {
			Projectile Projectile = (Projectile) fixA.getUserData();
			Projectile.setOperative(false);
			
			if(player) {
				((Player) fixB.getUserData()).recibirGolpe(Projectile.getAttack());
			}
		}
		else if(fixB.getUserData() instanceof Projectile) {
			Projectile Projectile = (Projectile) fixB.getUserData();
			Projectile.setOperative(false);
			
			if(player) {
				((Player) fixA.getUserData()).recibirGolpe(Projectile.getAttack());
			}
		}
		else if(fixA.getBody().isActive() && fixB.getBody().isActive()) {
			if(fixA.getUserData() instanceof GravityBlock) {
				((GravityBlock) fixA.getUserData()).setDead(true);
				((Player) fixB.getUserData()).recibirGolpe(((GravityBlock) fixA.getUserData()).getDamage());
			}
			else if(fixB.getUserData() instanceof GravityBlock) {
				((GravityBlock) fixB.getUserData()).setDead(true);
				((Player) fixA.getUserData()).recibirGolpe(((GravityBlock) fixB.getUserData()).getDamage());
			}
		}
	}
	
	private void playerLadder(Fixture fixA, Fixture fixB, boolean player, float gravity) {
		if(((fixA.getUserData() instanceof Player)&&(fixB.getUserData() instanceof Ladder))||((fixA.getUserData() instanceof Ladder)&&(fixB.getUserData() instanceof Player))) {
			if(fixA.getUserData() instanceof Ladder) {
				((Player) fixB.getUserData()).setLadder(player);
				((Player) fixB.getUserData()).getBody().setGravityScale(gravity);

				if(gravity == 1) {
					((Player) fixB.getUserData()).getBody().applyForceToCenter(0, -9.81f, true);
				}

				if(player)
					((Player) fixB.getUserData()).setCardinalPoint(CardinalPoint.NORTH);
			}
			else if(fixB.getUserData() instanceof Ladder) {
				((Player) fixA.getUserData()).setLadder(player);
				((Player) fixA.getUserData()).getBody().setGravityScale(gravity);

				if(gravity == 1) {
					((Player) fixA.getUserData()).getBody().applyForceToCenter(0, -9.81f, true);
				}

				if(player)
					((Player) fixA.getUserData()).setCardinalPoint(CardinalPoint.NORTH);
			}
		}
	}
	
	private void gravityBlock(Fixture fixA, Fixture fixB) {
		if(fixA.getUserData() instanceof GravityBlock) {
			((GravityBlock) fixA.getUserData()).getBody().getFixtureList().get(0).setSensor(false);
			((GravityBlock) fixA.getUserData()).getBody().getFixtureList().get(1).getFilterData().categoryBits = GameManifest.CATEGORY_NONE;
			
			((GravityBlock) fixA.getUserData()).getBody().applyForceToCenter(0, -9.81f, true);
			((GravityBlock) fixA.getUserData()).getBody().setLinearVelocity(0, -4.5f);
		}
		else if(fixB.getUserData() instanceof GravityBlock) {
			((GravityBlock) fixB.getUserData()).getBody().getFixtureList().get(0).setSensor(false);
			((GravityBlock) fixB.getUserData()).getBody().getFixtureList().get(1).getFilterData().categoryBits = GameManifest.CATEGORY_NONE;
			
			((GravityBlock) fixB.getUserData()).getBody().applyForceToCenter(0, -9.81f, true);
			((GravityBlock) fixB.getUserData()).getBody().setLinearVelocity(0, -4.5f);
		}
	}
}