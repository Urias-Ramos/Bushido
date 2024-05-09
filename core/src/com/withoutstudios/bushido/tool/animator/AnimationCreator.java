package com.withoutstudios.bushido.tool.animator;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase permite crear una animacion
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class AnimationCreator {
	private CardinalPoint cardinalPoint;
	private Animation<TextureRegion> animation;
	private int[] actionIndex;
	
	public AnimationCreator(Animation<TextureRegion> animation, CardinalPoint cardinalPoint, int[] actionIndex) {
		this.cardinalPoint = cardinalPoint;
		this.actionIndex = actionIndex;
		
		this.animation = animation;
	}

	public CardinalPoint getCardinalPoint() {
		return cardinalPoint;
	}
	
	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}

	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
	}

	public int[] getActionIndex() {
		return actionIndex;
	}
}