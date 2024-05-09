package com.withoutstudios.bushido.entity.decoration;

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
public class Vase extends Decoration {
	private Bushido bushido;

	public Vase(Bushido bushido, Rectangle rectangle) {
		this.bushido = bushido;
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		getHitbox().width = 22 / GameManifest.PPM;
		getHitbox().height = 22 / GameManifest.PPM;
		
		getHitbox().x = (rectangle.x / GameManifest.PPM);
		getHitbox().y = rectangle.y / GameManifest.PPM;
		
		x = getHitbox().x;
		y = getHitbox().y;
		
		setCardinalPoint(CardinalPoint.NONE);
        
		createAnimation(bushido);
		
		setHibernate(true);
	}
	
	private void createAnimation(Bushido bushido) {
        AnimationCreator[] idle = new AnimationCreator[1];
        idle[0] = new AnimationCreator(bushido.getAssets().getVaseAnimation()[0], CardinalPoint.NONE, null);
        
        AnimationCreator[] attack = new AnimationCreator[1];
        attack[0] = new AnimationCreator(bushido.getAssets().getVaseAnimation()[1], CardinalPoint.NONE, new int[] {4});
        
        AnimationManager idleAnimation = new AnimationManager(idle);
        AnimationManager attackAnimation = new AnimationManager(attack);

        AnimationManager[] animationManager = new AnimationManager[2];
        animationManager[0] = idleAnimation;
        animationManager[1] = attackAnimation;

        setAnimationController(new AnimationController(animationManager));
        getAnimationController().setCardinalPoint(getCardinalPoint());
        getAnimationController().setIndexAnimation(0);
    }

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		getAnimationController().animate(delta);
		
		if((GameManifest.PLAYER.getHitboxSword().overlaps(getHitbox()))&&(GameManifest.PLAYER.isAttack())) {
			setHibernate(false);
			if(getAnimationController().getIndexAnimation() != 1) {
				getAnimationController().setIndexAnimation(1);
				bushido.playSoundEffect(bushido.getAssets().vaseSound);
			}
		}
		
		for(int i=0; i<bushido.getMap().getMapLoader().getProjectilList().size(); i++) {
			if(bushido.getMap().getMapLoader().getProjectilList().get(i).getHitbox().overlaps(getHitbox())) {
				setHibernate(false);
				if(getAnimationController().getIndexAnimation() != 1) {
					getAnimationController().setIndexAnimation(1);
					bushido.playSoundEffect(bushido.getAssets().vaseSound);
				}
			}
		}
		
		if(getAnimationController().getIndexAction() == 4) {
			setDead(true);
			getAnimationController().resetIndexAction();
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 22 / GameManifest.PPM, 22 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}