package com.withoutstudios.bushido.tool;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.math.Vector2;
import com.withoutstudios.bushido.entity.Player;

/**
 * En esta clase se encuentran las variables y metodos estaticos que son usados por el juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class GameManifest {
	public static double SCOORE_ENEMY = 0;
	public static int COLLISION_COUNT = 0;
	public static int SCOORE = 0;
	
	public static boolean DEBUG = false;
	public static float PPM = 32;
	
	public static Vector2 PLAYER_RESPAWN = new Vector2();
	
	public static int DEAD_COUNT = 0;
	public static int ENEMY_COUNT = 0;
	
	public static boolean CHANGE_MAP = false;
	public static boolean RESET_MAP = false;
	public static int CURRENT_LEVEL = 1;
	public static int LEVEL = 1;
	public static boolean NIGHTMODE = false;
	public static boolean GAME_FINISH = false;
	
	public static final float ROBOT_BODY_WIDTH = 18;
    public static final float ROBOT_BODY_HEIGHT = 50;
    
    public static final float ROBOT_FEET_WIDTH = ROBOT_BODY_WIDTH - 0.5f;
    public static final float ROBOT_FEET_HEIGHT = 4;
    public static final float ROBOT_MAX_SPEED = 5;
	
	public static float SCREEN_WIDTH = 768, SCREEN_HEIGHT = 432;
	public static int LAND_SIZE = 16;
	
	public static boolean PAUSE = false;
	public static boolean SCOORE_SCREEN = false;
	
	public static boolean MUSIC = true;
	public static boolean SOUND = true;
	
	public static boolean PLAYER_MOVE_RIGHT = false;
	public static boolean PLAYER_MOVE_LEFT = false;
	public static boolean PLAYER_MOVE_UP = false;
	public static boolean PLAYER_MOVE_DOWN = false;
	
	public static boolean PLAYER_JUMP = false;
	public static boolean PLAYER_ROLLING = false;
	
	public static boolean PLAYER_ATTACK_1 = false;
	public static boolean PLAYER_ATTACK_2 = false;
	public static boolean PLAYER_ATTACK_2_PRESSED = false;
	
	//box2d categorias
	public static final short CATEGORY_LAND = 1;
	public static final short CATEGORY_NONE = 2;
	public static final short CATEGORY_PLAYER = 4;
	public static final short CATEGORY_ENEMY = 128;
	public static final short CATEGORY_PROJECTIL = 16;
	public static final short CATEGORY_ITEM = 32;
	public static final short CATEGORY_TRAP = 64;
	public static final short CATEGORY_LADDER = 112;
	
	
	
	public static final short CATEGORY_SENSOR_FOOT = 100;
	public static final short CATEGORY_SENSOR_ATTACK_LEFT = 200;
	public static final short CATEGORY_SENSOR_ATTACK_RIGHT = 400;
	public static final short CATEGORY_SENSOR_VISION = 800;
	//box2d categorias
	
	//box2d mask
	public static final short MASK_LAND = CATEGORY_PLAYER | CATEGORY_ENEMY | CATEGORY_PROJECTIL | CATEGORY_ITEM;
	public static final short MASK_PLAYER = CATEGORY_LAND | CATEGORY_ENEMY | CATEGORY_PROJECTIL | CATEGORY_LADDER | CATEGORY_ITEM;
	public static final short MASK_ENEMY = CATEGORY_LAND | CATEGORY_PROJECTIL | CATEGORY_ITEM;
	
	public static final short MASK_FOOT = CATEGORY_LAND | CATEGORY_ITEM;
	public static final short MASK_ENEMY_SENSOR_ATTACK = CATEGORY_PLAYER;
	public static final short MASK_PLAYER_SENSOR_ATTACK = CATEGORY_ENEMY;
	
	public static final short MASK_PLAYER_PROJECTILE = CATEGORY_LAND | CATEGORY_ENEMY | CATEGORY_PROJECTIL;
	public static final short MASK_ENEMY_PROJECTILE = CATEGORY_LAND | CATEGORY_PLAYER | CATEGORY_PROJECTIL;
	
	public static final short MASK_LADDER = CATEGORY_PLAYER;
	public static final short MASK_ITEM = CATEGORY_LAND | CATEGORY_ENEMY | CATEGORY_ITEM | CATEGORY_PLAYER;
	//box2d mask
	
	public static Player PLAYER;
	
	private static Random RANDOM = new Random();
	
	public static int getRandomNumber(int min, int max) {
		return (RANDOM.nextInt((max - min)) + min);
	}
	
	public static boolean isServedTime(long currentTime, int expectedTime) {
		if(TimeUnit.MILLISECONDS.convert((System.nanoTime() - currentTime), TimeUnit.NANOSECONDS) >= expectedTime) {
			return true;
		}
		return false;
	}
	
	public static long isServedTime(long currentTime) {
		return TimeUnit.MILLISECONDS.convert((System.nanoTime() - currentTime), TimeUnit.NANOSECONDS);
	}
}