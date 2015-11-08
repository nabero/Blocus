package elements.particles;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import elements.particles.individual.PrecalculatedParticles;

public class DiamondParticles implements Poolable {
	
	public static final float WIDTH = Rubico.screenWidth / 30, HALF_WIDTH = WIDTH / 2, SPEED = Rubico.tierWidth,  
			TAKEN_X = Rubico.screenWidth * 0.9f, TAKEN_Y = Rubico.screenHeight * 0.1f, TARGET_X = TAKEN_X + ((Rubico.screenWidth - TAKEN_X)/2), TARGET_Y = Rubico.screenHeight * 0.05f;
	public static final Pool<DiamondParticles> POOL = new Pool<DiamondParticles>() {
		protected DiamondParticles newObject() {			return new DiamondParticles();		}
	};
	public float x, y;
	public boolean phase1;
	private final Vector2 dir = new Vector2();
	public static float tmpWidth;
	
	public DiamondParticles set(float x, float y) {
		this.x = x;
		this.y = y;
		dir.x = (float) (Rubico.R.nextGaussian() * 3);
		dir.y = (float) (Rubico.R.nextGaussian() * 3);
		phase1 = true;
		return this;
	}
	
	@Override
	public void reset() {	}
	
	static int max;
	public static void act(Array<DiamondParticles> flammes, SpriteBatch batch) {
//		max = Math.max(max, flammes.size);
		for (final DiamondParticles f : flammes) {
			batch.setColor(PrecalculatedParticles.blocBlue[10]);
			batch.draw(AssetMan.diamond, f.x, f.y, WIDTH, WIDTH);
			if (f.phase1) {
				f.dir.scl(0.96f);
				if (f.dir.len() < 0.2f) {
					f.phase1 = false;
					f.dir.x = TARGET_X - f.x;
					f.dir.y = TARGET_Y - f.y;
					f.dir.scl(2);
				}
			} else {
				if (f.x > TAKEN_X && f.y < TAKEN_Y) {
					POOL.free(f);
					flammes.removeValue(f, true);
				}
			}
			f.x += f.dir.x * EndlessMode.delta;
			f.y += f.dir.y * EndlessMode.delta;
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<DiamondParticles> flammes) {
		POOL.freeAll(flammes);
		flammes.clear();
	}


}
