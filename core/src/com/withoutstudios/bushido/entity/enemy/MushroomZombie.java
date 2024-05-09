package com.withoutstudios.bushido.entity.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
public class MushroomZombie extends Enemy {
	private float x, y;
	private long timeWalk;
	
	private Bushido bushido;

	public MushroomZombie(Bushido bushido) {
		super(12, 1.0f, 1, 1, false);
		this.bushido = bushido;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.WEST);
		createAnimation(bushido);
		
		setHibernate(true);
		
		timeWalk = System.nanoTime();
	}
	
	private void createAnimation(Bushido bushido) {		
		AnimationCreator[] mushroom = new AnimationCreator[2];
		
		mushroom[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[0], CardinalPoint.EAST, null);
		mushroom[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] awaken = new AnimationCreator[2];
		awaken[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[2], CardinalPoint.EAST, new int[] {7});
		awaken[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[3], CardinalPoint.WEST, new int[] {7});
		
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[4], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[5], CardinalPoint.WEST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[6], CardinalPoint.EAST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[7], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[8], CardinalPoint.EAST, new int[] {3});
		attack[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[9], CardinalPoint.WEST, new int[] {3});
		
		AnimationCreator[] hit = new AnimationCreator[2];
		hit[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[10], CardinalPoint.EAST, null);
		hit[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[11], CardinalPoint.WEST, null);
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[12], CardinalPoint.EAST, new int[] {6});
		dead[1] = new AnimationCreator(bushido.getAssets().getMushroomZombieAnimation()[13], CardinalPoint.WEST, new int[] {6});
		
		AnimationManager mushroomAnimation = new AnimationManager(mushroom);
		AnimationManager awakenAnimation = new AnimationManager(awaken);
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager hitAnimation = new AnimationManager(hit);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[7];
		animationManager[0] = mushroomAnimation;
		animationManager[1] = awakenAnimation;
		animationManager[2] = idleAnimation;
		animationManager[3] = walkAnimation;
		animationManager[4] = attackAnimation;
		animationManager[5] = hitAnimation;
		animationManager[6] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(0);
	}
	
	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		x = getBody().getPosition().x - (50.0f / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - 56.0f / 2 / GameManifest.PPM;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			x -= 0.1f;
		}
		else if(getCardinalPoint() == CardinalPoint.EAST) {
			x += 0.1f;
		}
		
		y += 0.25f;
		
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
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getAnimationController().setIndexAnimation(1);
				}
				break;
			case 1:
				if(getAnimationController().getIndexAction() == 7) {
					getAnimationController().setIndexAnimation(3);
					getAnimationController().resetIndexAction();
				}
				break;
			case 3:
				if(GameManifest.isServedTime(timeWalk, 30)) {
					if(getCardinalPoint() == CardinalPoint.WEST) {
						getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
					}
					else if(getCardinalPoint() == CardinalPoint.EAST) {
						getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
					}
					
					timeWalk = System.nanoTime();
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getAnimationController().setIndexAnimation(4);
				}
				break;
			case 4:
				setCardinalPoint(CardinalPoint.EAST);
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 3) {
						getPlayer().recibirGolpe(getAttack());
						getAnimationController().resetIndexAction();
					}
				}
				else {
					setCardinalPoint(CardinalPoint.EAST);
					if(getHitbox().x > getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.WEST);
					}
					
					getAnimationController().setIndexAnimation(3);
				}
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 6) {
				getBody().setActive(false);
				
				bushido.playSoundEffect(bushido.getAssets().mushroomZombieDead);
			}
			
			getAnimationController().setIndexAnimation(6);
			if(getAnimationController().getIndexAction() == 6) {
				setDead(true);
				getAnimationController().resetIndexAction();
			}
		}
		
		getHitbox().x = (getBody().getPosition().x - getHitbox().width / 2);
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 50 / GameManifest.PPM, 56 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}