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
public class BringerDeath extends Enemy {
	private float x, y, x1, y1;
	
	private int idle, timeValue;
	private long idleTime;
	private long timeWalk;
	
	private Rectangle hitboxAttack1, hitboxAttack2;
	
	private AnimationController animationControllerInvocation;
	private long attack2Time;
	private boolean attack2;
	
	private Bushido bushido;
	
	public BringerDeath(Bushido bushido) {
		super(38, 1.5f, 1, 4, true);
		this.bushido = bushido;
		
		setCardinalPoint(CardinalPoint.WEST);
		createAnimation();
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		hitboxAttack1 = new Rectangle(0, 0, 42 / GameManifest.PPM, 32 / GameManifest.PPM);
		hitboxAttack2 = new Rectangle(0, 0, 16 / GameManifest.PPM, (2 * 16) / GameManifest.PPM);
		
		setHibernate(true);
		
		idleTime = System.nanoTime();
		idle = 1;
		timeValue = 1;
		timeWalk = System.nanoTime();
		attack2Time = System.nanoTime();
	}
	
	private void createAnimation() {
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[0], CardinalPoint.WEST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[1], CardinalPoint.EAST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[2], CardinalPoint.WEST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[3], CardinalPoint.EAST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[4], CardinalPoint.WEST, new int[] {4});
		attack[1] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[5], CardinalPoint.EAST, new int[] {4});
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[6], CardinalPoint.WEST, new int[] {6});
		dead[1] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[7], CardinalPoint.EAST, new int[] {6});
		
		AnimationCreator[] attack2 = new AnimationCreator[2];
		attack2[0] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[8], CardinalPoint.WEST, new int[] {7});
		attack2[1] = new AnimationCreator(bushido.getAssets().getBringerDeathAnimation()[9], CardinalPoint.EAST, new int[] {7});
		
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager deadAnimation = new AnimationManager(dead);
		AnimationManager attack2Animation = new AnimationManager(attack2);
		
		AnimationManager[] animationManager = new AnimationManager[5];
		animationManager[0] = idleAnimation;
		animationManager[1] = walkAnimation;
		animationManager[2] = attackAnimation;
		animationManager[3] = deadAnimation;
		animationManager[4] = attack2Animation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(1);
		
		AnimationCreator[] inv1 = new AnimationCreator[1];
		inv1[0] = new AnimationCreator(bushido.getAssets().getBringerInvocationAnimation()[0], CardinalPoint.NONE, new int[] {7});
		
		AnimationCreator[] inv2 = new AnimationCreator[1];
		inv2[0] = new AnimationCreator(bushido.getAssets().getBringerInvocationAnimation()[1], CardinalPoint.NONE, new int[] {7});
		
		AnimationManager inv1Animation = new AnimationManager(inv1);
		AnimationManager inv2Animation = new AnimationManager(inv2);
		
		AnimationManager[] invanimationManager = new AnimationManager[2];
		invanimationManager[0] = inv1Animation;
		invanimationManager[1] = inv2Animation;
		
		animationControllerInvocation = new AnimationController(invanimationManager);
		animationControllerInvocation.setCardinalPoint(CardinalPoint.NONE);
		animationControllerInvocation.setIndexAnimation(0);
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
				
				if(GameManifest.isServedTime(attack2Time, timeValue)) {
					getAnimationController().setIndexAnimation(4);
					
					timeValue = GameManifest.getRandomNumber(15, 45) * 100;
					attack2Time = System.nanoTime();
				}
			}
			
			if(getPlayer().isAttack()) {
				if(getHitbox().overlaps(getPlayer().getHitboxSword())) {
					takeDamage(getPlayer().getDamageEnemy());
				}
			}
			
			if(isHurt()) {
				if(getAnimationController().getIndexAnimation() == 0) {
					getAnimationController().setIndexAnimation(1);
					idle = 3000;
				}
				
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
				}
				else {
					setCardinalPoint(CardinalPoint.EAST);
				}
				
				setHurt(false);
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getAnimationController().setIndexAnimation(2);
				}
				break;
			case 1:
				if(hitboxAttack1.overlaps(getPlayer().getHitbox())) {
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
				
				if(hitboxAttack1.overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 4) {
						bushido.playSoundEffect(bushido.getAssets().skeletonHit);
						getPlayer().recibirGolpe(getAttack());
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
			case 4:
				if(getAnimationController().getIndexAction() == 7) {
					attack2 = true;
					getAnimationController().resetIndexAction();
					getAnimationController().setIndexAnimation(1);
					x1 = getPlayer().getBody().getPosition().x - 52 / GameManifest.PPM;
					y1 = getPlayer().getBody().getPosition().y + 0.5f;
					attack2Time = System.nanoTime();
				}
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 3) {
				getBody().setActive(false);
				bushido.playSoundEffect(bushido.getAssets().bringerDead);
			}
			
			getAnimationController().setIndexAnimation(3);
			if(getAnimationController().getIndexAction() == 6) {
				setDead(true);
				getAnimationController().resetIndexAction();
			}
		}
		
		getHitbox().x = (getBody().getPosition().x - getHitbox().width / 2);
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			hitboxAttack1.x = getHitbox().x + getHitbox().width;
		}
		else if(getCardinalPoint() == CardinalPoint.WEST) {
			hitboxAttack1.x = getHitbox().x - hitboxAttack1.width;
		}
		
		if(attack2) {
			if(animationControllerInvocation.getIndexAnimation() == 0) {
				if(animationControllerInvocation.getIndexAction() == 7) {
					if(hitboxAttack2.overlaps(getPlayer().getHitbox())) {
						getPlayer().recibirGolpe(3);
					}
					
					attack2 = false;
					animationControllerInvocation.resetIndexAction();
				}
			}
		}
		
		hitboxAttack1.y = getHitbox().y;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		x = getBody().getPosition().x - 92 / GameManifest.PPM;
		y = getBody().getPosition().y - 36.5f / GameManifest.PPM;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			x = getBody().getPosition().x - 27 / GameManifest.PPM;
		}
		
		y += 0.5;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
			batch.draw(textureHitbox, hitboxAttack1.x,  hitboxAttack1.y, hitboxAttack1.width, hitboxAttack1.height);
			batch.draw(textureHitbox, hitboxAttack2.x,  hitboxAttack2.y, hitboxAttack2.width, hitboxAttack2.height);
		}
		
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 120 / GameManifest.PPM, 73 / GameManifest.PPM);
		
		if(attack2) {
			hitboxAttack2.x =x1 + 1.4f;
			hitboxAttack2.y =y1 - 0.1f;
			
			animationControllerInvocation.animate(elapsedAnim);
			
			batch.draw(animationControllerInvocation.getFrame(elapsedAnim, true), x1,  y1, 120 / GameManifest.PPM, 73 / GameManifest.PPM);
		}
	}
}