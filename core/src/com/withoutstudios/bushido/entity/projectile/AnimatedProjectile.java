package com.withoutstudios.bushido.entity.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class AnimatedProjectile extends Projectile {
	private AnimationController animationController;
	private float elapsedAnim;
	private int projectileWidth, projectileHeight;

	public AnimatedProjectile(float speed, int attack, CardinalPoint cardinalPoint, RayHandler rayHandler) {
		super(speed, attack, cardinalPoint);

		pointLight = new PointLight(rayHandler, 10, Color.RED, 50 /GameManifest.PPM, 0, 0);
		pointLight.setXray(true);
	}
	
	public AnimatedProjectile(float speed, int minimumAttack, int MaximumAttack, CardinalPoint cardinalPoint, RayHandler rayHandler) {
		super(speed, minimumAttack, MaximumAttack, true, cardinalPoint);
		pointLight = new PointLight(rayHandler, 10, Color.RED, 50 /GameManifest.PPM, 0, 0);
		pointLight.setXray(true);
	}
	
	public void createAnimation(Animation<TextureRegion>[] animationProjectile, Animation<TextureRegion>[] animationEffect) {
		this.projectileWidth = 46;
		this.projectileHeight = 46;
		
		AnimationCreator[] projectile = new AnimationCreator[2];
		projectile[0] = new AnimationCreator(animationProjectile[0], CardinalPoint.EAST, null);
		projectile[1] = new AnimationCreator(animationProjectile[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] projectileEffect = new AnimationCreator[2];
		projectileEffect[0] = new AnimationCreator(animationEffect[0], CardinalPoint.EAST, new int[] {6});
		projectileEffect[1] = new AnimationCreator(animationEffect[1], CardinalPoint.WEST, new int[] {6});
		
		AnimationManager projectileAnimation = new AnimationManager(projectile);
		AnimationManager projectileEffectAnimation = new AnimationManager(projectileEffect);
		
		AnimationManager[] animationManager = new AnimationManager[2];
		animationManager[0] = projectileAnimation;
		animationManager[1] = projectileEffectAnimation;
		
		animationController = new AnimationController(animationManager);
		
		animationController.setCardinalPoint(getCardinalPoint());
		animationController.setIndexAnimation(0);
	}
	
	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		animationController.animate(elapsedAnim);
		
		if(isOperative()) {
			x = getBody().getPosition().x - (projectileWidth / 2) / GameManifest.PPM;
			y = getBody().getPosition().y - (projectileHeight / 2) / GameManifest.PPM;
			
			getHitbox().x = (getBody().getPosition().x - getHitbox().width / 2);
			getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;

			pointLight.setPosition(getBody().getPosition());
		}
		else {
			getBody().setActive(false);
			if(animationController.getIndexAnimation() != 1) {
				animationController.setIndexAnimation(1);
			}
			
			if(animationController.getIndexAction() == 6) {
				setDead(true);
				pointLight.remove();
				animationController.resetIndexAction();
			}
		}
		
		if(GameManifest.isServedTime(timeDead, 3500)) {
			setOperative(false);
			setDead(true);
			pointLight.remove();
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(animationController.getFrame(elapsedAnim, true), x,  y, projectileWidth / GameManifest.PPM, projectileHeight / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(getTextureHitbox(), getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}