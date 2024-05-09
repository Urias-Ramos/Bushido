package com.withoutstudios.bushido.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.entity.projectile.AnimatedProjectile;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class FireWorm extends Enemy {
	private Bushido bushido;
	private float x, y;
	private long timeWalk;
	
	private Rectangle hitboxVision;

	public FireWorm(Bushido bushido) {
		super(15, 0.8f, 2, 5, true);
		this.bushido = bushido;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.WEST);
		createAnimation(bushido);
		
		hitboxVision = new Rectangle(0, 0, (8 * 16) / GameManifest.PPM, 32 / GameManifest.PPM);
		
		setHibernate(true);
		timeWalk = System.nanoTime();
	}
	
	private void createAnimation(Bushido bushido) {		
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[0], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[2], CardinalPoint.EAST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[3], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[4], CardinalPoint.EAST, new int[] {11});
		attack[1] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[5], CardinalPoint.WEST, new int[] {11});
		
		AnimationCreator[] hit = new AnimationCreator[2];
		hit[0] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[6], CardinalPoint.EAST, null);
		hit[1] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[7], CardinalPoint.WEST, null);
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[8], CardinalPoint.EAST, new int[] {8});
		dead[1] = new AnimationCreator(bushido.getAssets().getFireWormAnimation()[9], CardinalPoint.WEST, new int[] {8});
		
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager hitAnimation = new AnimationManager(hit);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[5];
		animationManager[0] = idleAnimation;
		animationManager[1] = walkAnimation;
		animationManager[2] = attackAnimation;
		animationManager[3] = hitAnimation;
		animationManager[4] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(1);
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		x = getBody().getPosition().x - (90.0f / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - (90.0f / 2) / GameManifest.PPM;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			x += 0.5f;
		}
		else if(getCardinalPoint() == CardinalPoint.WEST) {
			x -= 0.5f;
		}
		
		y += 1.00f;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(getHealth() > 0) {
			
			if(getPlayer().isAttack()) {
				if(getHitbox().overlaps(getPlayer().getHitboxSword())) {
					takeDamage(getPlayer().getDamageEnemy());
				}
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				break;
			case 1:
				if(GameManifest.isServedTime(timeWalk, 30)) {
					if(getCardinalPoint() == CardinalPoint.WEST) {
						getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
					}
					else if(getCardinalPoint() == CardinalPoint.EAST) {
						getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
					}
					
					timeWalk = System.nanoTime();
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())||(hitboxVision.overlaps(getPlayer().getHitbox()))) {
					getAnimationController().setIndexAnimation(2);
				}
				break;
			case 2:
				setCardinalPoint(CardinalPoint.EAST);
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 11) {
						
						getPlayer().recibirGolpe(getAttack());
						
						getAnimationController().resetIndexAction();
					}
				}
				else if(hitboxVision.overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 11) {
						
						AnimatedProjectile projectile = new AnimatedProjectile(20.0f, 5, getCardinalPoint(), bushido.getMap().getMapLoader().getRayHandler());
						projectile.createAnimation(bushido.getAssets().getProjectile1Animation(), bushido.getAssets().getProjectile1EffectAnimation());
						projectile.createBody(getEntityType(), bushido.getMap().getWorld(), getBody().getPosition().x, getBody().getPosition().y + 0.5f, 8.0f, 8.0f);
						projectile.launch();
						
						bushido.getMap().getMapLoader().getProjectilList().add(projectile);
						
						
						bushido.playSoundEffect(bushido.getAssets().soundPlayerProjectile);
						
						getAnimationController().resetIndexAction();
					}
				}
				else {
					setCardinalPoint(CardinalPoint.EAST);
					if(getHitbox().x > getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.WEST);
					}
					
					getAnimationController().setIndexAnimation(1);
				}
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 4) {
				getAnimationController().setIndexAnimation(4);
				getBody().setActive(false);
			}
			
			if(getAnimationController().getIndexAction() == 8) {
				setDead(true);
				getAnimationController().resetIndexAction();
			}
		}
		
		getHitbox().x = (getBody().getPosition().x - getHitbox().width / 2);
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			hitboxVision.x = getHitbox().x + getHitbox().width;
		}
		else if(getCardinalPoint() == CardinalPoint.WEST) {
			hitboxVision.x = getHitbox().x - (hitboxVision.width);
		}
		
		hitboxVision.y = getHitbox().y;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 90 / GameManifest.PPM, 90 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
			batch.draw(textureHitbox, hitboxVision.x,  hitboxVision.y, hitboxVision.width, hitboxVision.height);
		}
	}
}