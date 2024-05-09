package com.withoutstudios.bushido.tool.animator;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase es la que se encarga de administrar un grupo de animacion
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class AnimationManager {
	private int indexAnimation;
	private CardinalPoint CardinalPoint;
	private AnimationCreator[] animationCreator;
	
	private int currentActionIndex, auxActionIndex;
	private int actionIndex;

	public AnimationManager(AnimationCreator[] animationCreator) {
		this.animationCreator = animationCreator;
		indexAnimation = 0;
		
		currentActionIndex = -1;
		auxActionIndex = -2;
		actionIndex = -1;
	}
	
	public void animate(float delta) {
		for(int i=0; i<animationCreator.length; i++) {
			if(animationCreator[i].getCardinalPoint() == getCardinalPoint()) {
				indexAnimation = i;
				break;
			}
		}
		
		getIndexAcction(delta);
	}
	
	public Animation<TextureRegion> getAnimation() {
		return animationCreator[indexAnimation].getAnimation();
	}
	
	public int getIndexAnimation() {
		return indexAnimation;
	}

	public void setIndexAnimation(int indexAnimation) {
		this.indexAnimation = indexAnimation;
	}

	public CardinalPoint getCardinalPoint() {
		return CardinalPoint;
	}

	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		CardinalPoint = cardinalPoint;
	}

	public AnimationCreator[] getAnimationCreator() {
		return animationCreator;
	}

	public void setAnimationCreator(AnimationCreator[] animationCreator) {
		this.animationCreator = animationCreator;
	}
	
	public int getActionIndex() {
		return actionIndex;
	}

	public void setActionIndex(int actionIndex) {
		this.actionIndex = actionIndex;
	}

	private void getIndexAcction(float delta) {
		setActionIndex(-1);
		if(getAnimationCreator()[indexAnimation].getActionIndex() != null) {
			currentActionIndex = (int) (delta / getAnimation().getFrameDuration()) % getAnimation().getKeyFrames().length;
			
			if(currentActionIndex != auxActionIndex) {
				auxActionIndex = currentActionIndex;
				
				for(int i=0; i<getAnimationCreator()[indexAnimation].getActionIndex().length; i++) {
					if(auxActionIndex == getAnimationCreator()[indexAnimation].getActionIndex()[i]) {
						setActionIndex(getAnimationCreator()[indexAnimation].getActionIndex()[i]);
					}
				}
			}
		}
	}
}