package com.withoutstudios.bushido.tool.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase controla todas las animacion de forma individual
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class AnimationController {
	private int indexAnimation;
	private CardinalPoint cardinalPoint;
	private AnimationManager[] animationManager;
	
	private float delta;
	
	public AnimationController(AnimationManager[] animationManager) {
		this.animationManager = animationManager;
	}
	
	public void animate(float del) {
		delta += Gdx.graphics.getDeltaTime();
		
		animationManager[getIndexAnimation()].setCardinalPoint(getCardinalPoint());
		animationManager[getIndexAnimation()].animate(delta);
	}

	public int getIndexAnimation() {
		return indexAnimation;
	}

	public void setIndexAnimation(int indexAnimation) {
		
		if(indexAnimation != getIndexAnimation()) {
			delta = 0;
		}
		this.indexAnimation = indexAnimation;
	}

	public CardinalPoint getCardinalPoint() {
		return cardinalPoint;
	}

	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}
	
	public TextureRegion getFrame(float del, boolean loop) {
		return animationManager[getIndexAnimation()].getAnimation().getKeyFrame(delta, loop);
	}
	
	public void resetIndexAction() {
		animationManager[getIndexAnimation()].setActionIndex(-1);
	}
	
	public int getIndexAction() {
		return animationManager[getIndexAnimation()].getActionIndex();
	}
	
	public int getFrameIndexCurrent(float del) {
		return (int) (delta / animationManager[getIndexAnimation()].getAnimation().getFrameDuration()) % animationManager[getIndexAnimation()].getAnimation().getKeyFrames().length;
	}
}