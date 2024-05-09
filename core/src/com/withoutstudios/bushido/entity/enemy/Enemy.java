package com.withoutstudios.bushido.entity.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.withoutstudios.bushido.entity.Entity;
import com.withoutstudios.bushido.entity.Player;
import com.withoutstudios.bushido.map.Map;
import com.withoutstudios.bushido.map.trigger.DisplayText;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class Enemy extends Entity {
	private int health;
	private float speed;
	
	private int minimalDamage, maximunDamage;
	private boolean randomAttack;
	
	private boolean hurt;
	
	private Player player;
	private Rectangle hitbox;
	
	private AnimationController animationController;
	
	private DisplayText displayText;

	public Enemy(int health, float speed, int minimalDamage, int maximunDamage, boolean randomAttack) {
		super(EntityType.ENEMY);
		
		setHealth(health);
		setSpeed(speed);
		setMinimalDamage(minimalDamage);
		setMaximunDamage(maximunDamage);
		setRandomAttack(randomAttack);
		setHurt(false);
		
		hitbox = new Rectangle();
		
		animationController = null;
		displayText = new DisplayText("", 0, 0, 1, 1);
	}
	
	public void changeDirection() {
		if(getCardinalPoint() == CardinalPoint.WEST) {
			setCardinalPoint(CardinalPoint.EAST);
		}
		else if(getCardinalPoint() == CardinalPoint.EAST) {
			setCardinalPoint(CardinalPoint.WEST);
		}
		
		getAnimationController().setCardinalPoint(getCardinalPoint());
		
		if((this instanceof MushroomZombie)) {
			if(getAnimationController().getIndexAnimation() != 0) {
				getAnimationController().setIndexAnimation(3);
			}
		}
		else if((this instanceof MutatedBat)||(this instanceof FlyingEye)) {
			
		}
		else {
			getAnimationController().setIndexAnimation(1);
		}
	}
	
	public int getAttack() {
		if(isRandomAttack()) {
			return GameManifest.getRandomNumber(minimalDamage, maximunDamage);
		}
		return maximunDamage;
	}
	
	public void takeDamage(int damage) {
		if((this instanceof MushroomZombie)&&(this.getAnimationController().getIndexAnimation() == 0)) {
			damage = 0;
		}
		
		setHurt(true);
		if((getHealth() - damage) > 0) {
			setHealth(getHealth() - damage);
			displayText(""+(damage * -1));
		}
		else {
			setHealth(0);
			setHibernate(false);
			
			displayText("DEAD");
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getMinimalDamage() {
		return minimalDamage;
	}

	public void setMinimalDamage(int minimalDamage) {
		this.minimalDamage = minimalDamage;
	}

	public int getMaximunDamage() {
		return maximunDamage;
	}

	public void setMaximunDamage(int maximunDamage) {
		this.maximunDamage = maximunDamage;
	}

	public boolean isRandomAttack() {
		return randomAttack;
	}

	public void setRandomAttack(boolean randomAttack) {
		this.randomAttack = randomAttack;
	}

	public boolean isHurt() {
		return hurt;
	}
	
	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public void setAnimationController(AnimationController animationController) {
		this.animationController = animationController;
	}
	
	public AnimationController getAnimationController() {
		return animationController;
	}
	
	private void displayText(String text) {
		displayText.setText(text);
		
		if(!displayText.isAdd()) {
			displayText.setX(getBody().getPosition().x);
			displayText.setY(getBody().getPosition().y);
			
			displayText.start();
			
			Map.mapRenderer.getHitList().add(displayText);
		}
	}
}