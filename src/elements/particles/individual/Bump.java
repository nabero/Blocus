package elements.particles.individual;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.particles.Particles;

public class Bump {

	private static final float BIG = Stats.U * 20, MIN = Stats.U;
	private float x, y, width, height;
	private float[] colors;
	private int index;
	public static final Pool<Bump> POOL = new Pool<Bump>() {
		protected Bump newObject() {			return new Bump();		}
	};
	
	public static void addUpBall(float x, float[] colors) {
		Particles.BUMPS.add(POOL.obtain().init(x - BIG / 2, Rubico.screenHeight - MIN / 2, colors, BIG, MIN));
	}

	public static void addLeftBall(float y, float[] colors) {
		Particles.BUMPS.add(POOL.obtain().init(-MIN / 2, y - BIG / 2, colors, MIN, BIG));
	}

	public static void addRightBall(float y, float[] colors) {
		Particles.BUMPS.add(POOL.obtain().init(Rubico.screenWidth - MIN / 2, y - BIG / 2, colors, MIN, BIG));
	}

	public static void addTop(float x, float min, float max, float[] colors) {
		if (EndlessMode.fps < 45)
			return;
		Particles.BUMPS.add(POOL.obtain().init(x - max / 2, Rubico.screenHeight - min / 2, colors, max, min));
	}

	public static void addLeft(float y, float min, float max, float[] colors) {
		if (EndlessMode.fps < 45)
			return;
		Particles.BUMPS.add(POOL.obtain().init(-min / 2, y - max / 2, colors, min, max));
	}

	public static void addRight(float y, float min, float max, float[] colors) {
		if (EndlessMode.fps < 45)
			return;
		Particles.BUMPS.add(POOL.obtain().init(Rubico.screenWidth - min / 2, y - max / 2, colors, min, max));
	}

	public Bump init(float x, float y, float[] colors, float width, float height, int index) {
		init(x, y, colors, width, height);
		this.index = index;
		return this;
	}
	
	private Bump init(float x, float y, float[] colors, float width, float height) {
		this.colors = colors;
		index = 0;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		return this;
	}

//	static int max;
	public static void act(SpriteBatch batch, Array<Bump> array) {
//		max = Math.max(max, array.size);
//		System.out.println("bump : " + max);
		if (EndlessMode.alternate) {
			for (Bump b : array) {
				batch.setColor(b.colors[b.index]);
				batch.draw(AssetMan.dust, b.x, b.y, b.width, b.height);
			}
		} else {
			for (Bump b : array) {
				batch.setColor(b.colors[b.index]);
				batch.draw(AssetMan.dust, b.x, b.y, b.width, b.height);
				
				if (++b.index >= b.colors.length) {
					POOL.free(b);
					array.removeValue(b, true);
				}
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<Bump> array) {
		POOL.freeAll(array);
		array.clear();
	}
}
