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
public class WormTrap extends Trap {
	private AnimationController animationController;
	private float x, y;
	
	private int frameCurrent;
	private long timeSound;
	
	private Bushido bushido;

	public WormTrap(Bushido bushido, float x, float y) {
		this.bushido = bushido;
		this.x = x;
		this.y = y;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.NONE);
		createAnimation(bushido);
		
		getHitbox().width = 16 / GameManifest.PPM;
		getHitbox().height = 32 / GameManifest.PPM;
		
		timeSound = System.nanoTime();
		
		setHibernate(true);
	}
	
	private void createAnimation(Bushido bushido) {
		AnimationCreator[] dead = new AnimationCreator[1];
		dead[0] = new AnimationCreator(bushido.getAssets().getWormTrapAnimation()[0], CardinalPoint.NONE, new int[] {1, 5, 7, 9, 11, 13, 15, 17, 19, 28, 30, 34, 36, 38, 40, 42, 44, 46, 58, 61, 63, 65, 67, 69, 71, 73});
		
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
		
		getHitbox().y = (y - getHitbox().height / 2) + 0.40f;
		
		frameCurrent = getAnimationController().getFrameIndexCurrent(elapsedAnim);
		
		if((frameCurrent > -1)&&(frameCurrent < 7)) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 16) / GameManifest.PPM;
		}
		else if(((frameCurrent > 7)&&(frameCurrent < 11))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 48) / GameManifest.PPM;
		}
		else if(((frameCurrent > 11)&&(frameCurrent < 15))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 80) / GameManifest.PPM;
		}
		else if(((frameCurrent > 15)&&(frameCurrent < 19))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 112) / GameManifest.PPM;
		}
		else if(((frameCurrent > 19)&&(frameCurrent < 34))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 16) / GameManifest.PPM;
		}
		else if(((frameCurrent > 34)&&(frameCurrent < 38))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 48) / GameManifest.PPM;
		}
		else if(((frameCurrent > 38)&&(frameCurrent < 42))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 80) / GameManifest.PPM;
		}
		else if(((frameCurrent > 42)&&(frameCurrent < 46))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 112) / GameManifest.PPM;
		}
		else if(((frameCurrent > 46)&&(frameCurrent < 61))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 16) / GameManifest.PPM;
		}
		else if(((frameCurrent > 61)&&(frameCurrent < 65))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 48) / GameManifest.PPM;
		}
		else if(((frameCurrent > 65)&&(frameCurrent < 69))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 80) / GameManifest.PPM;
		}
		else if(((frameCurrent > 69)&&(frameCurrent < 73))) {
			getHitbox().x = (x - getHitbox().width / 2) + (128 - 112) / GameManifest.PPM;
		}
		
		if(getAnimationController().getIndexAction() == 1 ||
				getAnimationController().getIndexAction() == 5 ||
				getAnimationController().getIndexAction() == 7 ||
				getAnimationController().getIndexAction() == 9 ||
				getAnimationController().getIndexAction() == 11 ||
				getAnimationController().getIndexAction() == 13 ||
				getAnimationController().getIndexAction() == 15 ||
				getAnimationController().getIndexAction() == 17 ||
				getAnimationController().getIndexAction() == 19 ||
				getAnimationController().getIndexAction() == 28 ||
				getAnimationController().getIndexAction() == 30 ||
				getAnimationController().getIndexAction() == 34 ||
				getAnimationController().getIndexAction() == 36 ||
				getAnimationController().getIndexAction() == 38 ||
				getAnimationController().getIndexAction() == 40 ||
				getAnimationController().getIndexAction() == 42 ||
				getAnimationController().getIndexAction() == 44 ||
				getAnimationController().getIndexAction() == 46 ||
				getAnimationController().getIndexAction() == 58 ||
				getAnimationController().getIndexAction() == 61 ||
				getAnimationController().getIndexAction() == 63 ||
				getAnimationController().getIndexAction() == 65 ||
				getAnimationController().getIndexAction() == 67 ||
				getAnimationController().getIndexAction() == 69 ||
				getAnimationController().getIndexAction() == 71 ||
				getAnimationController().getIndexAction() == 73
				) {
			
			if(GameManifest.isServedTime(timeSound, 600)) {
				bushido.playSoundEffect(bushido.getAssets().wormTrapSound);
				timeSound = System.nanoTime();
			}
			if(getHitbox().overlaps(getPlayer().getHitbox())) {
				getAnimationController().resetIndexAction();
				getPlayer().recibirGolpe(3);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 128 / GameManifest.PPM, 64 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}