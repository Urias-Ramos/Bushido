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
public class FlyingEye extends Enemy {
	private float x, y;
	private int random;
	
	private Rectangle hitboxVision;
	
	private Bushido bushido;
	
	public FlyingEye(Bushido bushido) {
		super(29, 1.5f, 3, 3, false);
		this.bushido = bushido;
		
		setCardinalPoint(CardinalPoint.EAST);
		createAnimation();
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		hitboxVision = new Rectangle(0, 0, (10 * 16) / GameManifest.PPM, 1 / GameManifest.PPM);
		
		setHibernate(true);
	}
	
	private void createAnimation() {
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[0], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack1 = new AnimationCreator[2];
		attack1[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[2], CardinalPoint.EAST, new int[] {6});
		attack1[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[3], CardinalPoint.WEST, new int[] {6});
		
		AnimationCreator[] attack2 = new AnimationCreator[2];
		attack2[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[4], CardinalPoint.EAST, new int[] {5});
		attack2[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[5], CardinalPoint.WEST, new int[] {5});
		
		AnimationCreator[] attack3 = new AnimationCreator[2];
		attack3[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[6], CardinalPoint.EAST, new int[] {3});
		attack3[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[7], CardinalPoint.WEST, new int[] {3});
		
		AnimationCreator[] hit = new AnimationCreator[2];
		hit[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[8], CardinalPoint.EAST, new int[] {3});
		hit[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[9], CardinalPoint.WEST, new int[] {3});
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[10], CardinalPoint.EAST, new int[] {4});
		dead[1] = new AnimationCreator(bushido.getAssets().getFlyingEyeAnimation()[11], CardinalPoint.WEST, new int[] {4});
		
		AnimationManager flyAnimation = new AnimationManager(idle);
		AnimationManager attack1Animation = new AnimationManager(attack1);
		AnimationManager attack2Animation = new AnimationManager(attack2);
		AnimationManager attack3Animation = new AnimationManager(attack3);
		AnimationManager hitAnimation = new AnimationManager(hit);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[6];
		animationManager[0] = flyAnimation;
		animationManager[1] = attack1Animation;
		animationManager[2] = attack2Animation;
		animationManager[3] = attack3Animation;
		animationManager[4] = hitAnimation;
		animationManager[5] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(0);
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		if(getHealth() > 0) {
			
			if(getPlayer().isAttack()) {
				if(getHitbox().overlaps(getPlayer().getHitboxSword())) {
					takeDamage(getPlayer().getDamageEnemy());
				}
			}
			
			if(isHurt()) {
				getAnimationController().setIndexAnimation(4);
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				
				if(GameManifest.PLAYER.getHitbox().overlaps(getHitbox())) {
					if((getAnimationController().getIndexAnimation() != 1)&&(getAnimationController().getIndexAnimation() != 2)&&(getAnimationController().getIndexAnimation() != 3))
						getAnimationController().setIndexAnimation(1);
				}
				else if(GameManifest.PLAYER.getHitbox().overlaps(hitboxVision)) {
					if((getAnimationController().getIndexAnimation() != 1)&&(getAnimationController().getIndexAnimation() != 2)&&(getAnimationController().getIndexAnimation() != 3)) {
						random = GameManifest.getRandomNumber(1, 100);
						if(random > 95) {
							getAnimationController().setIndexAnimation(3);
						}
						else {
							getAnimationController().setIndexAnimation(2);
						}
					}
				}
				else {
					if(getHitbox().y > getPlayer().getHitbox().y + (getPlayer().getHitbox().height / 2)) {
						getBody().setLinearVelocity(getBody().getLinearVelocity().x, getSpeed() * -1);
					}
					else if(getHitbox().y < getPlayer().getHitbox().y) {
						getBody().setLinearVelocity(getBody().getLinearVelocity().x, getSpeed());
					}
					
					if(getHitbox().x > getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.WEST);
						getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
					}
					else if(getHitbox().x < getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.EAST);
						getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
					}
				}
				break;
			case 1:
				if(!GameManifest.PLAYER.getHitbox().overlaps(getHitbox())) {
					getAnimationController().setIndexAnimation(0);
				}
				else if(getAnimationController().getIndexAction() == 6) {
					GameManifest.PLAYER.recibirGolpe(2);
					getAnimationController().resetIndexAction();
				}
				break;
			case 2:
				if(!GameManifest.PLAYER.getHitbox().overlaps(hitboxVision)) {
					getAnimationController().setIndexAnimation(0);
					getBody().setLinearVelocity(0, 0);
				}
				else if(getAnimationController().getIndexAction() == 5) {
					if(getCardinalPoint() == CardinalPoint.EAST)
						getBody().setLinearVelocity(8.0f, getBody().getLinearVelocity().y);
					else if(getCardinalPoint() == CardinalPoint.WEST)
						getBody().setLinearVelocity(-8.0f, getBody().getLinearVelocity().y);
				}
				break;
			case 3:
				getBody().setLinearVelocity(0, 0);
				if(!GameManifest.PLAYER.getHitbox().overlaps(hitboxVision)) {
					getAnimationController().setIndexAnimation(0);
				}
				else if(getAnimationController().getIndexAction() == 3) {
					AnimatedProjectile projectile = new AnimatedProjectile(15.0f, 2, getCardinalPoint(), bushido.getMap().getMapLoader().getRayHandler());
					projectile.createAnimation(bushido.getAssets().getProjectile2Animation(), bushido.getAssets().getProjectile2EffectAnimation());
					projectile.createBody(getEntityType(), bushido.getMap().getWorld(), getBody().getPosition().x, getBody().getPosition().y - 0.3f, 8.0f, 8.0f);
					projectile.launch();
					
					bushido.getMap().getMapLoader().getProjectilList().add(projectile);
					bushido.playSoundEffect(bushido.getAssets().soundPlayerProjectile);
					
					getAnimationController().resetIndexAction();
				}
				break;
			case 4:
				if(getAnimationController().getIndexAction() == 3) {
					getAnimationController().resetIndexAction();
					getAnimationController().setIndexAnimation(0);
					setHurt(false);
				}
				break;
			case 5:
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 5) {
				getBody().setActive(false);
				bushido.playSoundEffect(bushido.getAssets().skeletonDead);
			}
			
			getAnimationController().setIndexAnimation(5);
			if(getAnimationController().getIndexAction() == 4) {
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
		
		hitboxVision.y = getHitbox().y + 0.5f;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		x = getBody().getPosition().x - (64 / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - (64 / 2) / GameManifest.PPM;
		
		y -= 0.1f;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
			batch.draw(textureHitbox, hitboxVision.x,  hitboxVision.y, hitboxVision.width, hitboxVision.height);
		}
		
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 64 / GameManifest.PPM, 64 / GameManifest.PPM);
	}
}