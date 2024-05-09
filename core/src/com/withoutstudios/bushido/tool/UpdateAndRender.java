package com.withoutstudios.bushido.tool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Esta interface establece los metodos de dibujado y de actualizar para todo el juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public interface UpdateAndRender {
	
	/**
	 * Este metodo se debe usar para crear la logica
	 * 
	 * @param delta variable delta del juego
	 */
	public void update(float delta);
	
	/**
	 * Este metodo debe usarse solo para implementar el dibujado
	 * 
	 * @param batch batch original
	 * @param delta variable delta del juego
	 */
	public void render(SpriteBatch batch, float delta);
}