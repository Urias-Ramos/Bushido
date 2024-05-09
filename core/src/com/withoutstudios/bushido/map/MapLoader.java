package com.withoutstudios.bushido.map;

import java.util.LinkedList;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.entity.CheckPoint;
import com.withoutstudios.bushido.entity.Entity;
import com.withoutstudios.bushido.entity.GravityBlock;
import com.withoutstudios.bushido.entity.Ladder;
import com.withoutstudios.bushido.entity.Portal;
import com.withoutstudios.bushido.entity.decoration.Decoration;
import com.withoutstudios.bushido.entity.decoration.TrainingDummy;
import com.withoutstudios.bushido.entity.decoration.Vase;
import com.withoutstudios.bushido.entity.decoration.WoonderBox;
import com.withoutstudios.bushido.entity.enemy.BringerDeath;
import com.withoutstudios.bushido.entity.enemy.Enemy;
import com.withoutstudios.bushido.entity.enemy.FireWorm;
import com.withoutstudios.bushido.entity.enemy.FlyingEye;
import com.withoutstudios.bushido.entity.enemy.MushroomZombie;
import com.withoutstudios.bushido.entity.enemy.MutatedBat;
import com.withoutstudios.bushido.entity.enemy.Skeleton;
import com.withoutstudios.bushido.entity.enemy.Slime;
import com.withoutstudios.bushido.entity.projectile.Projectile;
import com.withoutstudios.bushido.entity.trap.LightTrap2;
import com.withoutstudios.bushido.entity.trap.SpikeTrap;
import com.withoutstudios.bushido.entity.trap.Trap;
import com.withoutstudios.bushido.entity.trap.WormTrap;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

import box2dLight.RayHandler;

/**
 * Esta clase se encarga de leer los datos del mapa, cargar las entidades, trampas, objetos etc.
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class MapLoader {
	private Bushido bushido;
    private RayHandler rayHandler;
	private int mapWidth, mapHeight;
	private int abyssMap;
	
	private LinkedList<Enemy> enemyList;
	private LinkedList<Projectile> projectilList;
	private LinkedList<Trap> trapList;

	private LinkedList<Entity> entityList;
	private LinkedList<Decoration> decorationList;
	private LinkedList<Body> bodyList;
	
	private int musicIndex;

	public MapLoader(TiledMap tiledMap, World world, Bushido bushido) {
		this.bushido = bushido;

		rayHandler = new RayHandler(world);
		
		enemyList = new LinkedList<Enemy>();
		projectilList = new LinkedList<Projectile>();
		trapList = new LinkedList<Trap>();
		entityList = new LinkedList<Entity>();
		bodyList = new LinkedList<Body>();
		decorationList = new LinkedList<Decoration>();
		
		//loadCollision(tiledMap, world);
		
		//loadEnemy(tiledMap, world, bushido);
		//playerRespawn(tiledMap);
		
		musicIndex = -1;
	}
	
	private void playerRespawn(TiledMap tiledMap) {
		MapLayer mapLayer = tiledMap.getLayers().get("player");
		Rectangle rectangle = null;
		
		if(mapLayer != null) {
			MapObjects objects = mapLayer.getObjects();
			
			if(objects.get(0) instanceof RectangleMapObject) {
				rectangle = ((RectangleMapObject) objects.get(0)).getRectangle();
				
				GameManifest.PLAYER_RESPAWN.x = rectangle.x / GameManifest.PPM;
				GameManifest.PLAYER_RESPAWN.y = rectangle.y / GameManifest.PPM;
			}
		}
	}
	
	private void loadCollision(TiledMap tiledMap, World world) {
		MapLayer mapLayer = tiledMap.getLayers().get("collision");
		
		GameManifest.COLLISION_COUNT = 0;
		if(mapLayer != null) {
			MapObjects objects = mapLayer.getObjects();
			
			for(MapObject object: objects) {
				if(object instanceof RectangleMapObject) {
					GameManifest.COLLISION_COUNT++;
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
					
					BodyDef bodyDef = new BodyDef();
					bodyDef.type = BodyDef.BodyType.StaticBody;
					bodyDef.position.set((rectangle.x + rectangle.width / 2) / GameManifest.PPM, (rectangle.y + rectangle.height / 2) / GameManifest.PPM);
					
					Body body = world.createBody(bodyDef);
					
					PolygonShape shape = new PolygonShape();
					shape.setAsBox((rectangle.width / 2) / GameManifest.PPM, (rectangle.height / 2) / GameManifest.PPM);
					
					FixtureDef fixtureDef = new FixtureDef();//body.createFixture(shape, 1.0f);
					fixtureDef.shape = shape;
					fixtureDef.filter.categoryBits = GameManifest.CATEGORY_LAND;
		            fixtureDef.filter.maskBits = GameManifest.MASK_LAND;
					
		            body.createFixture(fixtureDef).setUserData("collision");
		            
					shape.dispose();
					
					getBodyList().add(body);
				}
			}
		}
	}
	
	private void createEntity(String id, World world, Bushido bushido, Rectangle rectangle) {
		Enemy enemy = null;
		switch(id) {
		case "skeleton":
			enemy = new Skeleton(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 18.0f, 38.0f));
			enemyList.add(enemy);
			break;
		case "bringer":
			enemy = new BringerDeath(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 18.0f, 38.0f));
			enemyList.add(enemy);
			break;
		case "slime":
			enemy = new Slime(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 18.0f, 18.0f));
			enemyList.add(enemy);
			break;
		case "mutatedBat":
			enemy = new MutatedBat(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 42.0f, 42.0f));
			enemyList.add(enemy);
			break;
		case "mushroomZombie":
			enemy = new MushroomZombie(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 18.0f, 38.0f));
			enemyList.add(enemy);
			break;
		case "fireWorm":
			enemy = new FireWorm(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 48.0f, 26.0f));
			enemyList.add(enemy);
			break;
		case "spikeTrap":
			getTrapList().add(new SpikeTrap(bushido, rectangle));
			break;
		case "wormTrap":
			getTrapList().add(new WormTrap(bushido, rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM));
			break;
		case "lightTrap2":
			getTrapList().add(new LightTrap2(bushido, rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM));
			break;
		case "portal":
			getEntityList().add(new Portal(bushido, rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM));
			break;
		case "ladder":
			getEntityList().add(new Ladder(bushido, world, rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM, rectangle.height));
			break;
		case "gravityBlock":
			getEntityList().add(new GravityBlock(bushido, world, rectangle));
			break;
		case "checkpoint":
			getEntityList().add(new CheckPoint(bushido, rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM));
			break;
		case "dummy":
			getDecorationList().add(new TrainingDummy(bushido, rectangle));
			break;
		case "vase":
			getDecorationList().add(new Vase(bushido, rectangle));
			break;
		case "woonderBox":
			getDecorationList().add(new WoonderBox(bushido, rectangle));
			break;
		case "flyingEye":
			enemy = new FlyingEye(bushido);
			enemy.setBody(createBodyEntity(world, enemy, rectangle.x, rectangle.y, 18.0f, 18.0f));
			enemyList.add(enemy);
			break;
		}
	}
	
	private Body createBodyEntity(World world, Enemy entity, float postX, float postY, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		
		if((entity instanceof MutatedBat)||(entity instanceof FlyingEye))
			bodyDef.type = BodyDef.BodyType.KinematicBody;
		else
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		bodyDef.position.set(new Vector2(postX / GameManifest.PPM, postY / GameManifest.PPM));
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.0f;
		Body body = null;
		
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / GameManifest.PPM, height / 2 / GameManifest.PPM);
		fixtureDef.shape = shape;
		
		entity.getHitbox().width = (width / 2 / GameManifest.PPM) * 2;
		entity.getHitbox().height = (height / 2 / GameManifest.PPM) * 2;
		
		fixtureDef.friction = 1.1f;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_ENEMY;
		fixtureDef.filter.maskBits = GameManifest.MASK_ENEMY;
		body.createFixture(fixtureDef).setUserData(entity);
		
		if((!(entity instanceof MutatedBat))&&(!(entity instanceof FlyingEye))) {
			shape.setAsBox(3 / 2 / GameManifest.PPM, GameManifest.ROBOT_FEET_HEIGHT / 2 / GameManifest.PPM, new Vector2(((-width + 3) / 2) / GameManifest.PPM, - height / 2 / GameManifest.PPM), 0);
	        fixtureDef.isSensor = true;
	        fixtureDef.shape = shape;
	        fixtureDef.filter.categoryBits = GameManifest.CATEGORY_SENSOR_FOOT;
	        fixtureDef.filter.maskBits = GameManifest.MASK_FOOT;
	        body.createFixture(fixtureDef).setUserData(entity);
			
			shape.setAsBox(3 / 2 / GameManifest.PPM, GameManifest.ROBOT_FEET_HEIGHT / 2 / GameManifest.PPM, new Vector2(((width - 3) / 2) / GameManifest.PPM, - height / 2 / GameManifest.PPM), 0);
	        fixtureDef.isSensor = true;
	        fixtureDef.shape = shape;
	        fixtureDef.filter.categoryBits = GameManifest.CATEGORY_SENSOR_FOOT;
	        fixtureDef.filter.maskBits = GameManifest.MASK_FOOT;
	        body.createFixture(fixtureDef).setUserData(entity);
		}
        
        shape.dispose();
        
        return body;
	}
	
	private void loadEnemy(TiledMap tiledMap, World world, Bushido bushido) {
		String[] id = {"skeleton", "slime", "mushroomZombie", "fireWorm", "flyingEye",
				"wormTrap", "spikeTrap", "portal", "ladder",
				"checkpoint", "lightTrap2", "mutatedBat", "bringer", "dummy", "vase", "woonderBox"};
		
		MapLayer mapLayer = null;
		Rectangle rectangle = null;
		
		for(int i=0; i<id.length; i++) {
			mapLayer = tiledMap.getLayers().get(id[i]);
			rectangle = null;
			if(mapLayer != null) {
				MapObjects objects = mapLayer.getObjects();
				
				for(MapObject object: objects) {
					if(object instanceof RectangleMapObject) {
						rectangle = ((RectangleMapObject) object).getRectangle();
						
						createEntity(id[i], world, bushido, rectangle);
					}
				}
			}
		}
		
		GameManifest.ENEMY_COUNT = getEnemyList().size();
		GameManifest.SCOORE_ENEMY = (double) 99999 / getEnemyList().size();
	}

	public void changeMap() {
		GameManifest.PLAYER.setVisible(true);
		GameManifest.DEAD_COUNT = 0;
		
		bushido.getAssets().loadMap(GameManifest.CURRENT_LEVEL);

        if(GameManifest.CURRENT_LEVEL == 5) {
            rayHandler.setAmbientLight(0.1f);
        }
		else if(GameManifest.CURRENT_LEVEL == 9) {
			rayHandler.setAmbientLight(0.0f);
		}

		if(!GameManifest.GAME_FINISH) {
			musicIndex = GameManifest.getRandomNumber(1, 6);
			if(bushido.getMap().randomMusic != musicIndex) {
				
				if(bushido.getAssets().ambient != null) {
					if(bushido.getAssets().ambient.isPlaying()) {
						bushido.getAssets().ambient.stop();
					}
					
					bushido.getAssets().ambient.dispose();
				}
				bushido.getAssets().ambient = null;
				
				bushido.getMap().randomMusic = musicIndex;
				bushido.getAssets().loadMusic(bushido.getMap().randomMusic);
			}
			
			bushido.getMap().setTiledMap(null);
			bushido.getMap().setTiledMap(bushido.getAssets().tiledMap);
			
			bushido.getMap().setTiledMapRenderer(null);
			bushido.getMap().setTiledMapRenderer(new OrthogonalTiledMapRenderer(bushido.getMap().getTiledMap(), 1 / GameManifest.PPM));
			
			setMapWidth(bushido.getAssets().tiledMap.getProperties().get("width", Integer.class) * 16);
			setMapHeight(bushido.getAssets().tiledMap.getProperties().get("height", Integer.class) * 16);

			setAbyssMap(-3);
			
			for(int i=0; i<getEnemyList().size(); i++) {
				getBodyList().add(getEnemyList().get(i).getBody());
			}
			
			for(int i=0; i<getProjectilList().size(); i++) {
				getBodyList().add(getProjectilList().get(i).getBody());
			}
			
			for(int i=0; i<getEntityList().size(); i++) {
				if(getEntityList().get(i).getBody() != null) {
					getBodyList().add(getEntityList().get(i).getBody());
				}
				
				if(getEntityList().get(i) instanceof Portal) {
					((Portal) getEntityList().get(i)).pointLight.remove();
				}
			}
			
			for(int i=0; i<getDecorationList().size(); i++) {
				if(getDecorationList().get(i).getBody() != null) {
					getBodyList().add(getDecorationList().get(i).getBody());
				}
			}
			
			for(int i=0; i<getBodyList().size(); i++) {
				bushido.getMap().getWorld().destroyBody(getBodyList().get(i));
			}
			
			getEnemyList().clear();
			getEntityList().clear();
			getTrapList().clear();
			getProjectilList().clear();
			getDecorationList().clear();
			bushido.getMap().getMapRenderer().getHitList().clear();
			
			getBodyList().clear();
			loadCollision(bushido.getAssets().tiledMap, bushido.getMap().getWorld());

			loadEnemy(bushido.getAssets().tiledMap, bushido.getMap().getWorld(), bushido);
			playerRespawn(bushido.getAssets().tiledMap);
			GameManifest.PLAYER.getBody().setTransform(GameManifest.PLAYER_RESPAWN, 0);
			
			if(GameManifest.CURRENT_LEVEL > GameManifest.LEVEL) {
				GameManifest.LEVEL++;
			}
			
			bushido.getPreferences().putBoolean("music", GameManifest.MUSIC);
			bushido.getPreferences().putBoolean("soundEffect", GameManifest.SOUND);
			bushido.getPreferences().putInteger("levelUnlocked", GameManifest.LEVEL);
			bushido.getPreferences().flush();
			
			GameManifest.CHANGE_MAP = false;
		}
		else {
			GameManifest.CHANGE_MAP = false;
			GameManifest.GAME_FINISH = false;
			bushido.changeStage(StageScene.CREDITS);
		}
	}
	
	public int getAbyssMap() {
		return abyssMap;
	}

	public void setAbyssMap(int abyssMap) {
		this.abyssMap = abyssMap;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	public LinkedList<Enemy> getEnemyList() {
		return enemyList;
	}

	public void setEnemyList(LinkedList<Enemy> enemyList) {
		this.enemyList = enemyList;
	}

	public LinkedList<Projectile> getProjectilList() {
		return projectilList;
	}

	public void setProjectilList(LinkedList<Projectile> projectilList) {
		this.projectilList = projectilList;
	}

	public LinkedList<Trap> getTrapList() {
		return trapList;
	}

	public void setTrapList(LinkedList<Trap> trapList) {
		this.trapList = trapList;
	}

	public LinkedList<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(LinkedList<Entity> entityList) {
		this.entityList = entityList;
	}
	
	public LinkedList<Decoration> getDecorationList() {
		return decorationList;
	}

	public void setDecorationList(LinkedList<Decoration> decorationList) {
		this.decorationList = decorationList;
	}

	private LinkedList<Body> getBodyList() {
		return bodyList;
	}

	public void setBodyList(LinkedList<Body> bodyList) {
		this.bodyList = bodyList;
	}

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void setRayHandler(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }
}