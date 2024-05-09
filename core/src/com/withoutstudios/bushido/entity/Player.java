package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.entity.projectile.AnimatedProjectile;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Player extends Entity {
	private Bushido bushido;
	private PointLight pointLight;
	private World world;
	
	//m
	private Vector2 tempImpulse = new Vector2();
	
	private boolean land, item;
	private boolean jump;
	//m
	
	private AnimationController animationController;
	
	private int damageEnemy;
	private float speed = 0.15f;
	
	private float x, y;
	
	private int saludTotal, saludActual;
	
	private boolean attack;
	
	private TextureRegion img = new TextureRegion(new Texture(Gdx.files.internal("ui/hitbox.png")));
	private Rectangle hitboxSword;
	
	private boolean hurt;
	private boolean dead;
	
	private long timeSoundWalk, timeSoundAttack;
	private long curita;
	
	private boolean ladder;
	private float targetVelocity;
	private long timeAttack;
	
	private boolean visible;

	public Player(World world, Bushido bushido, Vector2 respown, RayHandler rayHandler) {
		super(EntityType.PLAYER);
		this.world = world;
		this.bushido = bushido;
		
		setCardinalPoint(CardinalPoint.EAST);
		
		hitboxSword = new Rectangle();
		pointLight = new PointLight(rayHandler, 30, Color.ORANGE, 75 / GameManifest.PPM, 0, 0);
		pointLight.setXray(true);

		createBox2d(respown);
		createAnimation();
		
		setSaludTotal(12);
		setSaludActual(12);
		
		damageEnemy = 3;
		
		hurt = false;
		visible = true;

		timeSoundAttack = System.nanoTime();
		timeAttack = System.nanoTime();
		timeSoundWalk = System.nanoTime();
	}
	
	private void createBox2d(Vector2 respown) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(respown);
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.0f;
		
		setBody(world.createBody(bodyDef));
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(GameManifest.ROBOT_BODY_WIDTH / 2 / GameManifest.PPM, 42.0f / 2 / GameManifest.PPM);
		fixtureDef.shape = shape;
		
		fixtureDef.friction = 9f;
		fixtureDef.density = 600f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_PLAYER;
		fixtureDef.filter.maskBits = GameManifest.MASK_PLAYER;
		
		getBody().createFixture(fixtureDef).setUserData(this);
		
		//sensor
		shape.setAsBox((GameManifest.ROBOT_BODY_WIDTH - 1.0f) / 2 / GameManifest.PPM, 3.0f / 2 / GameManifest.PPM, new Vector2(0, -42.0f / 2 / GameManifest.PPM), 0);
		fixtureDef.density = 0;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_SENSOR_FOOT;
		fixtureDef.filter.maskBits = GameManifest.MASK_FOOT;
		fixtureDef.isSensor = true;
		this.getBody().createFixture(fixtureDef).setUserData(this);
		//sensor
        
        hitboxSword.width = (28.0f / 2 / GameManifest.PPM) * 2;
        hitboxSword.height = (42.0f / 2 / GameManifest.PPM) * 2;
        
        getHitbox().width = (18.0f / 2 / GameManifest.PPM) * 2;
        getHitbox().height = (42.0f / 2 / GameManifest.PPM) * 2;
        
        shape.dispose();
	}
	
	private void createAnimation() {
		AnimationCreator[] idle = new AnimationCreator[2];
		idle[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[0], CardinalPoint.EAST, null);
		idle[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[1], CardinalPoint.WEST, null);
		
		AnimationCreator[] walk = new AnimationCreator[2];
		walk[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[2], CardinalPoint.EAST, null);
		walk[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[3], CardinalPoint.WEST, null);
		
		AnimationCreator[] attack = new AnimationCreator[2];
		attack[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[4], CardinalPoint.EAST, new int[] {1});
		attack[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[5], CardinalPoint.WEST, new int[] {1});
		
		AnimationCreator[] attack2 = new AnimationCreator[2];
		attack2[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[6], CardinalPoint.EAST, new int[] {5, 8});
		attack2[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[7], CardinalPoint.WEST, new int[] {5, 8});
		
		AnimationCreator[] dead = new AnimationCreator[2];
		dead[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[8], CardinalPoint.EAST, new int[] {12});
		dead[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[9], CardinalPoint.WEST, new int[] {12});
		
		AnimationCreator[] rolling = new AnimationCreator[2];
		rolling[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[10], CardinalPoint.EAST, new int[] {6});
		rolling[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[11], CardinalPoint.WEST, new int[] {6});
		
		AnimationCreator[] jump = new AnimationCreator[2];
		jump[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[12], CardinalPoint.EAST, null);
		jump[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[13], CardinalPoint.WEST, null);
		
		AnimationCreator[] ladderUp = new AnimationCreator[2];
		ladderUp[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[14], CardinalPoint.NORTH, null);
		ladderUp[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[15], CardinalPoint.SOUTH, null);
		
		AnimationCreator[] attack3 = new AnimationCreator[2];
		attack3[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[16], CardinalPoint.EAST, new int[] {1});
		attack3[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[17], CardinalPoint.WEST, new int[] {1});
		
		AnimationCreator[] attack4 = new AnimationCreator[2];
		attack4[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[18], CardinalPoint.EAST, new int[] {1});
		attack4[1] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[19], CardinalPoint.WEST, new int[] {1});
		
		AnimationManager idleAnimation = new AnimationManager(idle);
		AnimationManager walkAnimation = new AnimationManager(walk);
		AnimationManager attackAnimation = new AnimationManager(attack);
		AnimationManager attack2Animation = new AnimationManager(attack2);
		AnimationManager deadAnimation = new AnimationManager(dead);
		AnimationManager rollingAnimation = new AnimationManager(rolling);
		AnimationManager jumpAnimation = new AnimationManager(jump);
		AnimationManager ladderUpAnimation = new AnimationManager(ladderUp);
		AnimationManager attack3Animation = new AnimationManager(attack3);
		AnimationManager attack4Animation = new AnimationManager(attack4);
		
		AnimationManager[] animationManager = new AnimationManager[10];
		animationManager[0] = idleAnimation;
		animationManager[1] = walkAnimation;
		animationManager[2] = attackAnimation;
		animationManager[3] = attack2Animation;
		animationManager[4] = deadAnimation;
		animationManager[5] = rollingAnimation;
		animationManager[6] = jumpAnimation;
		animationManager[7] = ladderUpAnimation;
		animationManager[8] = attack3Animation;
		animationManager[9] = attack4Animation;
		
		animationController = new AnimationController(animationManager);
		animationController.setCardinalPoint(getCardinalPoint());
		animationController.setIndexAnimation(0);
	}
	
	private void attackEnemy() {
		if(GameManifest.isServedTime(timeAttack, 150)) {

			if(GameManifest.isServedTime(timeSoundAttack, 450)) {
				bushido.playSoundEffect(bushido.getAssets().soundSword);
				timeSoundAttack = System.nanoTime();
			}
			setAttack(true);
			animationController.resetIndexAction();
			timeAttack = System.nanoTime();
		}
	}
	
	private void updateControls(float delta) {
		if(isLadder()) {
			getBody().setLinearVelocity(getBody().getLinearVelocity().x, 0);
		}
		
		float currentVelocity = getBody().getLinearVelocity().x;
		if((Gdx.input.isKeyPressed(Keys.X))||(GameManifest.PLAYER_ATTACK_1)) {
			GameManifest.PLAYER_ATTACK_2 = false;
			if(getAnimationController().getIndexAnimation() != 2 && getAnimationController().getIndexAnimation() != 8 && getAnimationController().getIndexAnimation() != 9) {
				animationController.setIndexAnimation(2);
			}
		}
		else if((Gdx.input.isKeyPressed(Keys.Z))||(GameManifest.PLAYER_ATTACK_2)) {
			if((animationController.getIndexAnimation() != 3)&&(!isLadder())) {
				animationController.setIndexAnimation(3);
			}
			else if(isLadder()) {
				GameManifest.PLAYER_ATTACK_2 = false;
			}
		}
		else if((Gdx.input.isKeyPressed(Keys.D))||(GameManifest.PLAYER_MOVE_RIGHT)) {
			if(!GameManifest.PLAYER_ROLLING) {
				animationController.setIndexAnimation(1);
			}
			
			targetVelocity = Math.min(currentVelocity + speed, GameManifest.ROBOT_MAX_SPEED);
            tempImpulse.x = (getBody().getMass() * 3) * (targetVelocity - currentVelocity);
            
           // System.out.println(tempImpulse.x);
            
            if(!isLadder()) {
				setCardinalPoint(CardinalPoint.EAST);
				getBody().applyLinearImpulse(tempImpulse, getBody().getWorldCenter(), true);
			}
            else {
				 getBody().setLinearVelocity(1.0f, getBody().getLinearVelocity().y);
            }
            
            if(GameManifest.isServedTime(timeSoundWalk, 800) && isJump()) {
            	bushido.playSoundEffect(bushido.getAssets().soundPlayerWalk);
            	timeSoundWalk = System.nanoTime();
            }
		}
		else if((Gdx.input.isKeyPressed(Keys.A))||(GameManifest.PLAYER_MOVE_LEFT)) {
			if(!GameManifest.PLAYER_ROLLING) {
				animationController.setIndexAnimation(1);
			}
			
			targetVelocity = Math.max(currentVelocity - speed, -GameManifest.ROBOT_MAX_SPEED);
			tempImpulse.x = (getBody().getMass() * 3) * (targetVelocity - currentVelocity);
            
            if(!isLadder()) {
				setCardinalPoint(CardinalPoint.WEST);
				getBody().applyLinearImpulse(tempImpulse, getBody().getWorldCenter(), true);
			}
			else {
				 getBody().setLinearVelocity(-1.0f, getBody().getLinearVelocity().y);
            }
            
            if(GameManifest.isServedTime(timeSoundWalk, 800) && isJump()) {
            	bushido.playSoundEffect(bushido.getAssets().soundPlayerWalk);
            	timeSoundWalk = System.nanoTime();
            }
		}
		else if((Gdx.input.isKeyPressed(Keys.W))||(GameManifest.PLAYER_MOVE_UP)) {
			if(isLadder()) {
				
				if(GameManifest.isServedTime(timeSoundWalk, 600)) {
	            	bushido.playSoundEffect(bushido.getAssets().ladderSound);
	            	timeSoundWalk = System.nanoTime();
	            }
				
				GameManifest.PLAYER_ATTACK_2 = false;
				getBody().setLinearVelocity(getBody().getLinearVelocity().x, 0);
	            getBody().setLinearVelocity(0, 1.0f);
	            setCardinalPoint(CardinalPoint.NORTH);
	            animationController.animate(elapsedAnim);
			}
		}
		else if((Gdx.input.isKeyPressed(Keys.S))||(GameManifest.PLAYER_MOVE_DOWN)) {
			if(isLadder()) {
				if(GameManifest.isServedTime(timeSoundWalk, 600)) {
	            	bushido.playSoundEffect(bushido.getAssets().ladderSound);
	            	timeSoundWalk = System.nanoTime();
	            }
				
				GameManifest.PLAYER_ATTACK_2 = false;
				getBody().setLinearVelocity(getBody().getLinearVelocity().x, 0);
				getBody().setLinearVelocity(0, -1.0f);
				setCardinalPoint(CardinalPoint.SOUTH);
				animationController.animate(elapsedAnim);
			}
		}
		else if(GameManifest.PLAYER_ROLLING) {
			animationController.setIndexAnimation(5);
		}
		else if(isJump() && !isLadder()) {
			animationController.setIndexAnimation(0);
		}
		
		if((Gdx.input.isKeyPressed(Keys.SPACE))||(GameManifest.PLAYER_JUMP)) {
			GameManifest.PLAYER_ATTACK_2 = false;
			if(isJump()) {
				getBody().setLinearVelocity(getBody().getLinearVelocity().x, 4.6f);
				bushido.playSoundEffect(bushido.getAssets().soundPlayerJump);
				setJump(false);
			}
		}
		
		if(!isJump() && !isLadder()) {
			animationController.setIndexAnimation(6);
		}
		
		if(isLadder()) {
			animationController.setIndexAnimation(7);
		}
		
		getHitbox().x = getBody().getPosition().x - (hitboxSword.width / 2) + 0.25f;
		getHitbox().y = getBody().getPosition().y - getHitbox().height / 2;
		
		if(getCardinalPoint() == CardinalPoint.EAST) {
			hitboxSword.x = getHitbox().x + getHitbox().width;
		}
		else if(getCardinalPoint() == CardinalPoint.WEST) {
			hitboxSword.x = (getHitbox().x - hitboxSword.width);
		}
		
		getHitboxSword().y = getBody().getPosition().y - hitboxSword.height / 2;

		pointLight.setPosition(getBody().getPosition());
	}
	
	public int getSaludTotal() {
		return saludTotal;
	}

	public void setSaludTotal(int saludTotal) {
		this.saludTotal = saludTotal;
	}

	public int getSaludActual() {
		return saludActual;
	}

	public void setSaludActual(int saludActual) {
		this.saludActual = saludActual;
	}
	
	public void recibirGolpe(int golpe) {
		if((getSaludActual() - golpe) >= 0) {
			setSaludActual(getSaludActual() - golpe);
			setHurt(true);
			curita = System.nanoTime();
		}
		else {
			setSaludActual(0);
		}
	}
	
	public boolean isAttack() {
		return attack;
	}

	private void setAttack(boolean attack) {
		this.attack = attack;
	}
	
	public void update(float delta) {
		if(isVisible()) {
			elapsedAnim += delta;
			
			if(getSaludActual() > 0) {
				
				if(getSaludActual() < getSaludTotal()) {
					if(GameManifest.isServedTime(curita, 3000)) {
						if(getSaludActual() + 1 <= getSaludTotal()) {
							setSaludActual(getSaludActual() + 1);
							curita = System.nanoTime();
						}
					}
				}
				
				updateControls(elapsedAnim);
				
				setAttack(false);
				
				switch(animationController.getIndexAnimation()) {
				case 2:
					setDamageEnemy(GameManifest.getRandomNumber(1, 4));
					attackEnemy();
					setJump(true);
					if(animationController.getFrameIndexCurrent(elapsedAnim) == 3) {
						animationController.setIndexAnimation(8);
					}
					break;
				case 3:
					if(animationController.getIndexAction() == 5) {
						AnimatedProjectile projectile = new AnimatedProjectile(20.0f, 3, 7, getCardinalPoint(), bushido.getMap().getMapLoader().getRayHandler());
						projectile.createAnimation(bushido.getAssets().getProjectile1Animation(), bushido.getAssets().getProjectile1EffectAnimation());
						projectile.createBody(getEntityType(), bushido.getMap().getWorld(), getBody().getPosition().x, getBody().getPosition().y, 8.0f, 8.0f);
						projectile.launch();
						
						bushido.getMap().getMapLoader().getProjectilList().add(projectile);
						
						bushido.playSoundEffect(bushido.getAssets().soundPlayerProjectile);
						setAttack(false);
					}
					else if(animationController.getIndexAction() == 8) {
						GameManifest.PLAYER_ATTACK_2 = GameManifest.PLAYER_ATTACK_2_PRESSED;
						
						animationController.resetIndexAction();
						animationController.setIndexAnimation(0);
					}
					break;
				case 8:
					setDamageEnemy(GameManifest.getRandomNumber(1, 4));
					attackEnemy();
					setJump(true);
					if(animationController.getFrameIndexCurrent(elapsedAnim) == 3) {
						animationController.setIndexAnimation(9);
					}
					break;
				case 9:
					setDamageEnemy(GameManifest.getRandomNumber(1, 4));
					attackEnemy();
					setJump(true);
					if(animationController.getFrameIndexCurrent(elapsedAnim) == 4) {
						animationController.setIndexAnimation(2);
					}
					break;
				}
			}
			else {
				getAnimationController().setIndexAnimation(4);
				if(getAnimationController().getIndexAction() == 12) {
					setDead(true);
					getAnimationController().resetIndexAction();
				}
			}
		}
	}
	
	public void render(SpriteBatch batch, float delta) {
		if(isVisible()) {
			x = getBody().getPosition().x - (GameManifest.ROBOT_BODY_WIDTH / 2) / GameManifest.PPM;
			y = getBody().getPosition().y - 42.0f / 2 / GameManifest.PPM;
			
			x -= 1.5f;
			y -= 0.5f;
			
			if((getCardinalPoint() != CardinalPoint.NORTH)&&(getCardinalPoint() != CardinalPoint.SOUTH)) {
				if((getCardinalPoint() == CardinalPoint.WEST)) {
					x -= 0.75f;
				}
			}
			
			animationController.setCardinalPoint(getCardinalPoint());
			
			if(!isLadder() || getSaludActual() <= 0)
				animationController.animate(elapsedAnim);
			
			batch.draw(animationController.getFrame(elapsedAnim, true), x,  y, 144 / GameManifest.PPM, 80 / GameManifest.PPM);
			
			if(GameManifest.DEBUG) {
				batch.draw(img, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
				batch.draw(img, getHitboxSword().x,  getHitboxSword().y, getHitboxSword().width, getHitboxSword().height);
			}
		}
	}

	public boolean isJump() {
		return jump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public Rectangle getHitboxSword() {
		return hitboxSword;
	}

	public boolean isHurt() {
		return hurt;
	}

	public void setHurt(boolean hurt) {
		this.hurt = hurt;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isLadder() {
		return ladder;
	}

	public void setLadder(boolean ladder) {
		this.ladder = ladder;
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public int getDamageEnemy() {
		return damageEnemy;
	}

	public void setDamageEnemy(int damageEnemy) {
		this.damageEnemy = damageEnemy;
	}

	public boolean isLand() {
		return land;
	}

	public void setLand(boolean land) {
		this.land = land;
	}

	public boolean isItem() {
		return item;
	}

	public void setItem(boolean item) {
		this.item = item;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}