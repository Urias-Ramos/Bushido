package com.withoutstudios.bushido.entity.decoration;

import com.withoutstudios.bushido.entity.Entity;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class Decoration extends Entity {
	public float x, y;
	private AnimationController animationController;

	public Decoration() {
		super(EntityType.DECORATION);
		
		animationController = null;
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public void setAnimationController(AnimationController animatorController) {
		this.animationController = animatorController;
	}
}