package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class CheckPoint extends Entity {
	private AnimationController animationController;
	private float x, y;
	
	private Player player;

	public CheckPoint(Bushido bushido, float x, float y) {
		super(EntityType.NONE);
		
		this.x = x;
		this.y = y;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.NONE);
		createAnimation(bushido);
		
		getHitbox().width = 110 / GameManifest.PPM;
		getHitbox().height = 110 / GameManifest.PPM;
	}
	
	private void createAnimation(Bushido bushido) {
		AnimationCreator[] sword = new AnimationCreator[1];
		sword[0] = new AnimationCreator(bushido.getAssets().getCheckPointAnimation()[0], CardinalPoint.NONE, null);
		
		AnimationCreator[] check = new AnimationCreator[1];
		check[0] = new AnimationCreator(bushido.getAssets().getCheckPointAnimation()[1], CardinalPoint.NONE, new int[] {20});
		
		AnimationCreator[] neutral = new AnimationCreator[1];
		neutral[0] = new AnimationCreator(bushido.getAssets().getCheckPointAnimation()[2], CardinalPoint.NONE, null);
		
		AnimationManager deadAnimation = new AnimationManager(sword);
		AnimationManager checkAnimation = new AnimationManager(check);
		AnimationManager neutralAnimation = new AnimationManager(neutral);
		
		AnimationManager[] animationManager = new AnimationManager[3];
		animationManager[0] = deadAnimation;
		animationManager[1] = checkAnimation;
		animationManager[2] = neutralAnimation;
		
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
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		getHitbox().x = x;
		getHitbox().y = y;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(getHitbox().overlaps(getPlayer().getHitbox())) {
			GameManifest.PLAYER_RESPAWN.x = x + (getHitbox().width / 2);
			GameManifest.PLAYER_RESPAWN.y = y + 1.0f;
			
			getAnimationController().setIndexAnimation(1);
		}
		
		if(getAnimationController().getIndexAnimation() == 1 && getAnimationController().getIndexAction() == 20) {
			getAnimationController().setIndexAnimation(2);
			getAnimationController().resetIndexAction();
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
		
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 110 / GameManifest.PPM, 110 / GameManifest.PPM);
	}
}