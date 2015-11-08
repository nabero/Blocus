package elements.particles.individual;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import elements.generic.Ball;
import elements.generic.paddles.StraightPaddle;

public class Dot implements Poolable {
	
	public static final float WIDTH = Rubico.screenWidth / 140, HALF_WIDTH = WIDTH / 2, SPEED = Rubico.tierWidth, FURIOUS = SPEED * 4;
	public static final Pool<Dot> POOL = new Pool<Dot>() {
		protected Dot newObject() {			return new Dot();		}
	};
	public float x;
	public float y;
	private float[] colors;
	private int index;
	public static float tmpWidth;
	
	public Dot set(float x, float y, int index, float[] colors) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.colors = colors;
		return this;
	}
	
	@Override
	public void reset() {	}
	
	static int max;
	public static void act(Array<Dot> flammes, SpriteBatch batch) {
//		max = Math.max(max, flammes.size);
		for (final Dot f : flammes) {
			batch.setColor(f.colors[f.index]);
			batch.draw(AssetMan.debris, f.x, f.y, WIDTH, WIDTH);
			
			if (++f.index >= f.colors.length) {
				POOL.free(f);
				flammes.removeValue(f, true);
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<Dot> flammes) {
		POOL.freeAll(flammes);
		flammes.clear();
	}

}