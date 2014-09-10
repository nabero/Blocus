package elements.particles.individual.explosions;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.particles.Particles;

public class ExplodingBloc {

	private float x, y, color;
	private float[] colors;
	private int ttl;
	public static final Pool<ExplodingBloc> POOL = new Pool<ExplodingBloc>() {
		protected ExplodingBloc newObject() {			return new ExplodingBloc();		}
	};

	public static void explode(Bloc b, Array<ExplodingBloc> array, Vector2 dir) {
		for (int x = 0; x < 4; x++) {
			if (x - 2 >= 0) {
				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (3 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
			} else {
				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (3 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
			}
		}

		// x = 0, y = 1
		array.add(POOL.obtain().init(b.x + (0 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
		// x = 0, y = 2
		array.add(POOL.obtain().init(b.x + (0 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
		// x = 3, y = 1
		array.add(POOL.obtain().init(b.x + (3 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));
		// x = 3, y = 2
		array.add(POOL.obtain().init(b.x + (3 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors));

		// x = 1, y = 1
		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors));
		// x = 1, y = 2
		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors));
		// x = 2, y = 1
		array.add(POOL.obtain().init(b.x + (2 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors));
		// x = 2, y = 2
		array.add(POOL.obtain().init(b.x + (2 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors));
	}

	private ExplodingBloc init(float x, float y, float color, float[] colors) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.ttl = Rubico.R.nextInt(75);
		this.colors = colors;
		return this;
	}

	public static void draw(SpriteBatch batch, Array<ExplodingBloc> array) {
		for (ExplodingBloc e : array) {
			batch.setColor(e.color);
			batch.draw(AssetMan.debris, e.x, e.y, Bloc.QUART_WIDTH, Bloc.QUART_HEIGHT);
			if (++e.ttl > 85) {
				POOL.free(e);
				array.removeValue(e, true);
				for (int i = 0; i < EndlessMode.fps / 4; i++)
					SparklesColorOverTime.add(Particles.COLOR_OVER_TIME, e.colors, e.x, e.y);
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

}