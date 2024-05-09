package com.withoutstudios.bushido.entity.enemy;

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
public class Slime extends Enemy {
	private float x, y;
	private long timeWalk;
	
	private int idle;
	private long idleTime;
	
	private Bushido bushido;

	public Slime(Bushido bushido) {
		super(6, 1.4f, 2, 2, false);
		this.bushido = bushido;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		setCardinalPoint(CardinalPoint.EAST);
		createAnimation(bushido);
		
		setHibernate(true);
		
		idleTime = System.nanoTime();
		idle = 1;
		timeWalk = System.nanoTime();
	}
	
	private void createAnimation(Bushido bushido) {
		AnimationCreator[] idle = new AnimationCreator[2];
		
		idle[0] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[0], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[2], CardinalPoint.EAST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[3], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[4], CardinalPoint.EAST, new int[] {11});
		attack[1] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[5], CardinalPoint.WEST, new int[] {11});
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[6], CardinalPoint.EAST, new int[] {11});
		dead[1] = new AnimationCreator(bushido.getAssets().getSlimeAnimation()[7], CardinalPoint.WEST, new int[] {11});
		
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[4];
		animationManager[0] = idleAnimation;
		animationManager[1] = walkAnimation;
		animationManager[2] = attackAnimation;
		animationManager[3] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().setIndexAnimation(1);
	}
	
	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		x = getBody().getPosition().x - (64.0f / 2) / GameManifest.PPM;
		y = getBody().getPosition().y - 41.0f / 2 / GameManifest.PPM;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			x += 0.05f;
		}
		
		y += 0.35f;
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		getAnimationController().animate(elapsedAnim);
		
		if(getHealth() > 0) {
			
			if(getAnimationController().getIndexAnimation() != 2) {
				if(GameManifest.isServedTime(idleTime, idle)) {
					getAnimationController().setIndexAnimation(GameManifest.getRandomNumber(0, 2));
					idleTime = System.nanoTime();
					
					if(getAnimationController().getIndexAnimation() == 0) {
						idle = GameManifest.getRandomNumber(30, 50) * 100;
					}
					else {
						idle = GameManifest.getRandomNumber(50, 80) * 100;
					}
				}
			}
			
			if(getPlayer().isAttack()) {
				if(getHitbox().overlaps(getPlayer().getHitboxSword())) {
					takeDamage(getPlayer().getDamageEnemy());
				}
			}
			
			if(isHurt()) {
				if(getAnimationController().getIndexAnimation() == 0) {
					getAnimationController().setIndexAnimation(1);
					idle = 3000;
				}
				setHurt(false);
			}
			
			switch(getAnimationController().getIndexAnimation()) {
			case 0:
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getAnimationController().setIndexAnimation(2);
				}
				break;
			case 1:
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					getAnimationController().setIndexAnimation(2);
				}
				else {
					if(GameManifest.isServedTime(timeWalk, 40)) {
						if(getCardinalPoint() == CardinalPoint.WEST) {
							getBody().setLinearVelocity(getSpeed() * -1, getBody().getLinearVelocity().y);
						}
						else if(getCardinalPoint() == CardinalPoint.EAST) {
							getBody().setLinearVelocity(getSpeed(), getBody().getLinearVelocity().y);
						}
						
						timeWalk = System.nanoTime();
					}
					if(GameManifest.getRandomNumber(1, 100) == 51) {
						setCardinalPoint(CardinalPoint.WEST);
					}
				}
				break;
			case 2:
				setCardinalPoint(CardinalPoint.EAST);
				if(getHitbox().x > getPlayer().getHitbox().x) {
					setCardinalPoint(CardinalPoint.WEST);
				}
				
				if(getHitbox().overlaps(getPlayer().getHitbox())) {
					if(getAnimationController().getIndexAction() == 11) {
						getPlayer().recibirGolpe(2);
						getAnimationController().resetIndexAction();
					}
				}
				else {
					setCardinalPoint(CardinalPoint.EAST);
					if(getHitbox().x > getPlayer().getHitbox().x) {
						setCardinalPoint(CardinalPoint.WEST);
					}
					
					getAnimationController().setIndexAnimation(1);
				}
				idle = 0;
				break;
			}
		}
		else {
			if(getAnimationController().getIndexAnimation() != 3) {
				getBody().setActive(false);
				bushido.playSoundEffect(bushido.getAssets().slimeDead);
			}
			
			getAnimationController().setIndexAnimation(3);
			if(getAnimationController().getIndexAction() == 11) {
				setDead(true);
				getAnimationController().resetIndexAction();
			}
		}
		
		getHitbox().x = (getBody().getPosition().x - getHitbox().width / 2);
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 64 / GameManifest.PPM, 41 / GameManifest.PPM);
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}