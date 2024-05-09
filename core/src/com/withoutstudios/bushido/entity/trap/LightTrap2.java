package com.withoutstudios.bushido.entity.trap;

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
public class LightTrap2 extends Trap {
	private AnimationController animationController;
	private float x, y;
	
	private int frameCurrent;
	
	private long timeSound;
	
	private Bushido bushido;

	public LightTrap2(Bushido bushido, float x, float y) {
		this.bushido = bushido;
		
		this.x = x;
		this.y = y;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.NONE);
		createAnimation(bushido);
		
		getHitbox().width = 16 / GameManifest.PPM;
		getHitbox().height = 96 / GameManifest.PPM;
		
		getHitbox().x = x + (getHitbox().height / 2) - 0.25f;
		getHitbox().y = y;
		
		timeSound = System.nanoTime();
		
		setHibernate(true);
	}
	
	private void createAnimation(Bushido bushido) {
		AnimationCreator[] dead = new AnimationCreator[1];
		dead[0] = new AnimationCreator(bushido.getAssets().getLightTrap2Animation()[0], CardinalPoint.NONE, new int[] {4, 5, 6, 7, 8, 9, 10, 11});
		
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[1];
		animationManager[0] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(0);
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public void setAnimationController(AnimationController animationController) {
		this.animationController = animationController;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		frameCurrent = getAnimationController().getFrameIndexCurrent(elapsedAnim);
		
		if(getAnimationController().getIndexAction() == 4 ||
				getAnimationController().getIndexAction() == 5 ||
				getAnimationController().getIndexAction() == 6 ||
				getAnimationController().getIndexAction() == 7 ||
				getAnimationController().getIndexAction() == 8 ||
				getAnimationController().getIndexAction() == 9 ||
				getAnimationController().getIndexAction() == 10 ||
				getAnimationController().getIndexAction() == 11
				) {
			if(frameCurrent == 4)
				bushido.playSoundEffect(bushido.getAssets().lightTrap2Sound);
			
			if(GameManifest.isServedTime(timeSound, 600)) {
				timeSound = System.nanoTime();
			}
			if(getHitbox().overlaps(getPlayer().getHitbox())) {
				getAnimationController().resetIndexAction();
				getPlayer().recibirGolpe(2);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 96 / GameManifest.PPM, 96 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}