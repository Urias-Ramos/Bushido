package com.withoutstudios.bushido.entity.decoration;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.map.Map;
import com.withoutstudios.bushido.map.trigger.DisplayText;
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
public class TrainingDummy extends Decoration {
	private long time;
	private boolean on;
	
	private DisplayText displayText;

	public TrainingDummy(Bushido bushido, Rectangle rectangle) {
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		getHitbox().width = 32 / GameManifest.PPM;
		getHitbox().height = 32 / GameManifest.PPM;
		
		getHitbox().x = (rectangle.x / GameManifest.PPM);
		getHitbox().y = rectangle.y / GameManifest.PPM;
		
		x = getHitbox().x - (getHitbox().width - 0.5f) / 2;
		y = getHitbox().y - 0.2f;
		
		setCardinalPoint(CardinalPoint.NONE);
        
		createAnimation(bushido);
		
		setHibernate(true);
		time = -1;
		on = false;
		
		displayText = new DisplayText("", x, y, 1, 1);
	}
	
	private void displayText(String text) {
		displayText.setText(text);
		
		if(!displayText.isAdd()) {
			displayText.setX(x + getHitbox().width);
			displayText.setY(y + getHitbox().height + 0.5f);
			
			displayText.start();
			
			Map.mapRenderer.getHitList().add(displayText);
		}
	}
	
	private void createAnimation(Bushido bushido) {
        AnimationCreator[] idle = new AnimationCreator[1];
        idle[0] = new AnimationCreator(bushido.getAssets().getTrainingDummyAnimation()[0], CardinalPoint.NONE, null);
        
        AnimationCreator[] attack = new AnimationCreator[1];
        attack[0] = new AnimationCreator(bushido.getAssets().getTrainingDummyAnimation()[1], CardinalPoint.NONE, new int[] {4});
        
        AnimationCreator[] none = new AnimationCreator[1];
        none[0] = new AnimationCreator(bushido.getAssets().getTrainingDummyAnimation()[2], CardinalPoint.NONE, new int[] {7});

        AnimationManager idleAnimation = new AnimationManager(idle);
        AnimationManager attackAnimation = new AnimationManager(attack);
        AnimationManager noneAnimation = new AnimationManager(none);

        AnimationManager[] animationManager = new AnimationManager[3];
        animationManager[0] = idleAnimation;
        animationManager[1] = attackAnimation;
        animationManager[2] = noneAnimation;

        setAnimationController(new AnimationController(animationManager));
        getAnimationController().setCardinalPoint(getCardinalPoint());
        getAnimationController().setIndexAnimation(0);
    }

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		getAnimationController().animate(delta);
		
		if(GameManifest.PLAYER.getHitboxSword().overlaps(getHitbox())) {
			if(GameManifest.PLAYER.isAttack()) {
				displayText(""+GameManifest.PLAYER.getDamageEnemy());
				getAnimationController().setIndexAnimation(1);
			}
		}
		
		if(GameManifest.PLAYER.getHitbox().overlaps(getHitbox())) {
			if(!on)
				time = System.nanoTime();
			on = true;
		}
		else if(on && getAnimationController().getIndexAnimation() != 2) {
			if((GameManifest.isServedTime(time) <= 350)) {
				getAnimationController().setIndexAnimation(2);
			}
			else {
				on = false;
			}
		}
		
		if((getAnimationController().getIndexAction() == 4)&&(getAnimationController().getIndexAnimation() == 1)) {
			getAnimationController().resetIndexAction();
			getAnimationController().setIndexAnimation(0);
		}
		else if((getAnimationController().getIndexAction() == 7)&&(getAnimationController().getIndexAnimation() == 2)) {
			getAnimationController().resetIndexAction();
			getAnimationController().setIndexAnimation(0);
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 48 / GameManifest.PPM, 48 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}