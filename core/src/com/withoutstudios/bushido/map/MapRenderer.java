package com.withoutstudios.bushido.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.withoutstudios.bushido.map.trigger.DisplayText;
import com.withoutstudios.bushido.tool.UpdateAndRender;

/**
 * Esta clase es la encargada de administrar los disparadores del mapa.
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class MapRenderer implements UpdateAndRender {
	private ArrayList<DisplayText> hitList;

	public MapRenderer() {
		hitList = new ArrayList<DisplayText>();
	}

	@Override
	public void update(float delta) {
		if(getHitList().size() > 0) {
        	for(int i=getHitList().size() - 1; i>=0; i--) {
        		if(getHitList().get(i).isDead()) {
        			getHitList().get(i).setAdd(false);
        			getHitList().remove(i);
        		}
        	}
        }
		
		if(getHitList().size() > 0) {
			for(int i=0; i<getHitList().size(); i++) {
				getHitList().get(i).update(delta);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		if(getHitList().size() > 0) {
			for(int i=0; i<getHitList().size(); i++) {
				getHitList().get(i).render(batch, delta);
			}
		}
	}

	public ArrayList<DisplayText> getHitList() {
		return hitList;
	}

	public void setHitList(ArrayList<DisplayText> hitList) {
		this.hitList = hitList;
	}
}