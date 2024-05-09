package com.withoutstudios.bushido.entity.trap;

import com.withoutstudios.bushido.entity.Entity;
import com.withoutstudios.bushido.entity.Player;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class Trap extends Entity {
	private Player player;

	public Trap() {
		super(EntityType.TRAP);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}