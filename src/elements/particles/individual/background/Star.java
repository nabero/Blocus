package elements.particles.individual.background;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import elements.particles.Particles;

public class Star implements Poolable {
	
	private static final float WIDTH = Rubico.screenWidth / 130, MINWIDTH = (int) (WIDTH / 2.5f);
	private static final Pool<Star> POOL = new Pool<Star>(Particles.MAX_BACKGROUND) {
		@Override
		protected Star newObject() {
			return new Star();
		}
	};
	private final float w, speed;
	private float y, x;
	
	public Star() {
		float tmp = Math.abs((float) (Rubico.R.nextFloat() * WIDTH));
		while (tmp < MINWIDTH)
			tmp = Math.abs((float) (Rubico.R.nextFloat() * WIDTH));
		w = tmp;
		x = (Rubico.R.nextFloat() * Rubico.screenWidth + w) - w/2;
		speed = (w * w) * 15f;
	}

	@Override 
	public void reset() {
		x = (Rubico.R.nextFloat() * Rubico.screenWidth + w) - w/2;
		y = Rubico.screenHeight + w;
	}

	public static void initBackground(Array<Star> stars) {
		while (stars.size < Particles.MAX_BACKGROUND) {
			final Star p = Star.POOL.obtain();
			do {
				p.y = (float) (Rubico.R.nextFloat() * Rubico.screenHeight);
			} while (p.y <= 0);
			stars.add(p);
		}
	}

	public static void act(SpriteBatch batch, Array<Star> stars) {
		while (stars.size < Particles.MAX_BACKGROUND)
			stars.add(Star.POOL.obtain());

		batch.setColor(AssetMan.WHITE);
		if (EndlessMode.triggerStop) {
			for (final Star star : stars)
				batch.draw(AssetMan.dust, star.x, star.y, star.w, star.w);
		} else {
			for (final Star star : stars) {
				batch.draw(AssetMan.dust, star.x, star.y, star.w, star.w);
				star.y -= (star.speed * EndlessMode.delta2);
				if (star.y < 0) {
					POOL.free(star);
					stars.removeValue(star, true);
				}
			}
		}
	}

	public static void clear(Array<Star> stars) {
		POOL.freeAll(stars);
		stars.clear();
	}
}

