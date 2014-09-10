package elements.particles.individual.explosions;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import elements.generic.blocs.Bloc;

public class SparklesPopBloc implements Poolable {

	private float x, y, ancreX, ancreY, angle;
	private float[] colors;
	private int index;
	public static final float HEIGHT = Stats.UUU / 2, HALF_HEIGHT = HEIGHT / 2, WIDTH = Stats.uDiv4, HALF_WIDTH = WIDTH / 2;
	public static final Pool<SparklesPopBloc> POOL = new Pool<SparklesPopBloc>() {
		protected SparklesPopBloc newObject() {			return new SparklesPopBloc();		}
	};
	
	@Override
	public void reset() {	}
	
	public SparklesPopBloc init(float ancreX, float ancreY, float[] colors) {
		this.ancreX = ancreX;
		this.ancreY = ancreY;
		this.colors = colors;
		index = 0;
		do {
			x = (float) (ancreX + Rubico.R.nextGaussian() * Stats.U90);
		} while (x > -Bloc.TIER_WIDTH && x < Rubico.screenWidth);
		y = (float) (ancreY + Rubico.R.nextGaussian() * Stats.U90);
		this.angle = Rubico.tmpDir.set(x, y).sub(ancreX, ancreY).angle();
		return this;
	}
	
	public static void act(Array<SparklesPopBloc> explosions, SpriteBatch batch) {
		for (SparklesPopBloc e : explosions) {
			batch.setColor(e.colors[e.index]);
			batch.draw(AssetMan.dust, e.x, e.y, HALF_HEIGHT, HALF_WIDTH, HEIGHT, WIDTH, 1f, 1f, e.angle);
			e.x += (e.ancreX - e.x) * EndlessMode.delta * 8;
			e.y += (e.ancreY - e.y) * EndlessMode.delta * 8;
			
//			if (++e.index >= e.colors.length) { // || Math.abs(e.x - e.ancreX) < Stats.u) {
			if (Math.abs(e.x - e.ancreX) < Stats.u) {
				explosions.removeValue(e, true);
				POOL.free(e);
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<SparklesPopBloc> explosions) {
		POOL.freeAll(explosions);
		explosions.clear();
	}
	
}