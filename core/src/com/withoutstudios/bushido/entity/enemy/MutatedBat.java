package com.withoutstudios.bushido.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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
public class MutatedBat extends Enemy {
	private float x, y;
	private Bushido bushido;

	public MutatedBat(Bushido bushido) {
		super(3, 1.0f, 2, 2, false);
		this.bushido = bushido;
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.EAST);
		createAnimation(bushido);
		
		setHibernate(true);
	}
	
	private void createAnimation(Bushido bushido) {
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getMutatedBatAnimation()[0], CardinalPoint.WEST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getMutatedBatAnimation()[1], CardinalPoint.EAST, null);
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getMutatedBatAnimation()[2], CardinalPoint.WEST, new int[] {3, 7});
		dead[1] = new AnimationCreator(bushido.getAssets().getMutatedBatAnimation()[3], CardinalPoint.EAST, new int[] {3, 7});
		
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[2];
		animationManager[0] = walkAnimation;
		animationManager[1] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(0);
	}
	
	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		x = getBody().getPosition().x - (96 / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - (80 / 2) / GameManifest.PPM;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(getHealth() > 0) {
			
			if(getHitbox().overlaps(getPlayer().getHitbox())) {
				setHealth(0);
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				
				if(getHitbox().y > getPlayer().getHitbox().y + (getPlayer().getHitbox().height / 2)) {
					getBody().setLinearVelocity(getBody().getLinearVelocity().x, getSpeed() * -1);
				}
				else if(getHitbox().y < getPlayer().getHitbox().y) {
					getBody().setLinearVelocity(getBody().getLinearVelocity().x, getSpeed());
				}
				
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.EAST);
					getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
				}
				else if(getHitbox().x < getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
					getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
				}
				break;
			}
		}
		else {
			getAnimationController().setIndexAnimation(1);
			getBody().setLinearVelocity(0, 0);
			
			if(getAnimationController().getIndexAction() == 3) {
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getPlayer().recibirGolpe(4);
					getAnimationController().resetIndexAction();
					
					Vector2 direction = new Vector2(GameManifest.PLAYER.getBody().getPosition()).sub(getBody().getPosition()).nor();
					float dis = GameManifest.PLAYER.getBody().getPosition().dst(getBody().getPosition());
					float fuerza = 69999 / dis;
					
					Vector2 v = direction.scl(fuerza);//new Vector2(-59000f, 169999);
					GameManifest.PLAYER.getBody().applyForceToCenter(v, true);
				}
				if(Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
					Gdx.input.vibrate(150);
				}
				bushido.getMap().camera.activate = true;
			}
			
			if(getAnimationController().getIndexAction() == 7) {
				bushido.playSoundEffect(bushido.getAssets().mutatedBatDead);
				setDead(true);
				getAnimationController().resetIndexAction();
			}
		}
		
		getHitbox().x = (getBody().getPosition().x) - getHitbox().width / 2;
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 96 / GameManifest.PPM, 80 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}