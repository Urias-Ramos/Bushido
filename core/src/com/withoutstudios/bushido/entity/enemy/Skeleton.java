package com.withoutstudios.bushido.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.withoutstudios.bushido.Bushido;
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
public class Skeleton extends Enemy {
	private float x, y;
	
	private int idle;
	private long idleTime;
	private long timeWalk;
	
	private Rectangle hitboxVision;
	
	private boolean playerSeen;
	
	private Bushido bushido;
	
	public Skeleton(Bushido bushido) {
		super(9, 1.5f, 3, 3, false);
		this.bushido = bushido;
		
		setCardinalPoint(CardinalPoint.EAST);
		createAnimation();
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		hitboxVision = new Rectangle(0, 0, (4 * 16) / GameManifest.PPM, 32 / GameManifest.PPM);
		
		setHibernate(true);
		playerSeen = false;
		
		idleTime = System.nanoTime();
		idle = 1;
		timeWalk = System.nanoTime();
	}
	
	private void createAnimation() {
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[0], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[2], CardinalPoint.EAST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[3], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[4], CardinalPoint.EAST, new int[] {4});
		attack[1] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[5], CardinalPoint.WEST, new int[] {4});
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[6], CardinalPoint.EAST, new int[] {12});
		dead[1] = new AnimationCreator(bushido.getAssets().getSkeletonAnimation()[7], CardinalPoint.WEST, new int[] {12});
		
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[4];
		animationManager[0] = idleAnimation;
		animationManager[1] = walkAnimation;
		animationManager[2] = attackAnimation;
		animationManager[3] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(1);
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		if(getHealth() > 0) {
			if(getAnimationController().getIndexAnimation() != 2) {
				if(GameManifest.isServedTime(idleTime, idle)) {
					getAnimationController().setIndexAnimation(GameManifest.getRandomNumber(0, 2));
					idleTime = System.nanoTime();
					
					if(getAnimationController().getIndexAnimation() == 0) {
						idle = GameManifest.getRandomNumber(15, 35) * 100;
					}
					else {
						idle = GameManifest.getRandomNumber(50, 80) * 100;
					}
				}
			}
			
			if(getPlayer().isAttack()) {
				if(getHitbox().overlaps(getPlayer().getHitboxSword())) {
					
					if(!playerSeen) {
						if(getCardinalPoint() == getPlayer().getCardinalPoint()) {
							takeDamage(100);
						}
					}
					else {
						takeDamage(getPlayer().getDamageEnemy());
					}
				}
			}
			
			if(isHurt()) {
				playerSeen = true;
				if(getAnimationController().getIndexAnimation() == 0) {
					getAnimationController().setIndexAnimation(1);
					idle = 3000;
				}
				setHurt(false);
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					playerSeen = true;
					getAnimationController().setIndexAnimation(2);
				}
				
				if(hitboxVision.overlaps(getPlayer().getHitbox())) {
					playerSeen = true;
					getAnimationController().setIndexAnimation(1);
				}
				break;
			case 1:
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					playerSeen = true;
					getAnimationController().setIndexAnimation(2);
					
					if(getHitbox().x > getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.WEST);
					}
					else {
						setCardinalPoint(CardinalPoint.EAST);
					}
				}
				else {
					if(GameManifest.isServedTime(timeWalk, 40)) {
						if(getCardinalPoint() == CardinalPoint.WEST) {
							getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
						}
						else if(getCardinalPoint() == CardinalPoint.EAST) {
							getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
						}
						
						timeWalk = System.nanoTime();
					}
				}
				
				break;
			case 2:
				setCardinalPoint(CardinalPoint.EAST);
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 4) {
						bushido.playSoundEffect(bushido.getAssets().skeletonHit);
						getPlayer().recibirGolpe(3);
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
			case 3:
				break;
			case 4:
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 3) {
				getBody().setActive(false);
				bushido.playSoundEffect(bushido.getAssets().skeletonDead);
			}
			
			getAnimationController().setIndexAnimation(3);
			if(getAnimationController().getIndexAction() == 12) {
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
		x = getBody().getPosition().x - (64 / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - (64 / 2) / GameManifest.PPM;
		
		y -= 0.1f;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 64 / GameManifest.PPM, 64 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
			batch.draw(textureHitbox, hitboxVision.x,  hitboxVision.y, hitboxVision.width, hitboxVision.height);
		}
	}
}