package elements.particles.individual.explosions;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

import elements.generic.Paddle;
import elements.generic.blocs.Bloc;
import elements.generic.bonus.Bonus;
import elements.particles.Particles;
import elements.particles.individual.Bump;
import elements.particles.individual.Ghost;
import elements.particles.individual.PrecalculatedParticles;

public class SparklesColorOverTime implements Poolable {

	private float x, y, speedX, speedY, angle;
	private float[] colors;
	private int index;
	public static final float HEIGHT = Stats.UUU * .75f, HALF_HEIGHT = HEIGHT / 2, WIDTH = Stats.uDiv4 * 2.5f, HALF_WIDTH = WIDTH / 2, WIDTH2 = WIDTH * 2;
	private static final Vector2 tmpVector = new Vector2();
	public static final Pool<SparklesColorOverTime> POOL = new Pool<SparklesColorOverTime>() {
		protected SparklesColorOverTime newObject() {			return new SparklesColorOverTime();		}
	};
	private static final Array<Ghost> GHOSTS = new Array<Ghost>();
	
	@Override
	public void reset() {	}
	
	public static void act(Array<SparklesColorOverTime> explosions, SpriteBatch batch) {
		if (EndlessMode.alternate && !EndlessMode.triggerStop) {
			for (SparklesColorOverTime e : explosions) {
				batch.setColor(e.colors[e.index]);
				batch.draw(AssetMan.dust, e.x, e.y, HALF_HEIGHT, HALF_WIDTH, HEIGHT, WIDTH, 1f, 1f, e.angle);
				e.x += e.speedX * EndlessMode.delta;
				e.y += e.speedY * EndlessMode.delta;
				// left
				if (e.x < 0) {
					Bump.addLeft(e.y, WIDTH, HEIGHT, e.colors);
					if (Bonus.noWalls > 0) {
						e.x = Rubico.screenWidth;
						Bump.addRight(e.y, WIDTH, HEIGHT, e.colors);
					} else {
						e.speedX = Math.abs(e.speedX);
						setAngle(e);
					}
					// right
				} else if (e.x > Rubico.screenWidth) {
					Bump.addRight(e.y, WIDTH, HEIGHT, e.colors);
					if (Bonus.noWalls > 0) {
						Bump.addLeft(e.y, WIDTH, HEIGHT, e.colors);
						e.x = 0;
					} else {
						e.speedX = -Math.abs(e.speedX);
						setAngle(e);
					}
				}
//				GHOSTS.add(Ghost.POOL.obtain().init(e.x, e.y, e.colors[e.index], e.angle));
				
				if (++e.index >= PrecalculatedParticles.SMALLL_MAX) {
					explosions.removeValue(e, true);
					POOL.free(e);
				}
			}
		} else {
			for (SparklesColorOverTime e : explosions) {
				batch.setColor(e.colors[e.index]);
				batch.draw(AssetMan.dust, e.x, e.y, HALF_HEIGHT, HALF_WIDTH, HEIGHT, WIDTH, 1f, 1f, e.angle);
//				GHOSTS.add(Ghost.POOL.obtain().init(e.x, e.y, e.colors[e.index], e.angle));
				
				// paddle
				if (e.y < Paddle.top) {
					if (e.x > Paddle.x && e.x < Paddle.right) { 
						e.speedY = Math.abs(e.speedY);
						e.y = Paddle.top;
						setAngle(e);
					}
					// top
				} else if (e.y > Rubico.screenHeight) {
					Bump.addTop(e.x, WIDTH, HEIGHT, e.colors);
					e.speedY = -Math.abs(e.speedY);
					setAngle(e);
				}
			}
		}
		
		Ghost.act(batch, GHOSTS);
	}

	protected static void setAngle(SparklesColorOverTime e) {
		e.angle = tmpVector.set(e.speedX, e.speedY).angle();
		//.scl(0.9f).angle();
//		e.speedX = tmpVector.x;
//		e.speedY = tmpVector.y;
	}

	public static void clear(Array<SparklesColorOverTime> explosions) {
		POOL.freeAll(explosions);
		explosions.clear();
		
		Ghost.clear(GHOSTS);
	}
	
	public static void add(float x, float y, float angle, float[] colors, Array<SparklesColorOverTime> array) {
		array.add(POOL.obtain().init(x, y, colors, 0, 0, 0, angle));
	}

	public static void add(float x, float y, float angle, float[] colors, float speed) {
		tmpVector.x = speed;
		tmpVector.y = 0;
		tmpVector.rotate(angle);
		Particles.COLOR_OVER_TIME.add(POOL.obtain().init(x, y, colors, Rubico.R.nextInt((int) (colors.length * 0.25f)), tmpVector.x, tmpVector.y, angle));
	}

	public static void add(Array<SparklesColorOverTime> colorOverTime, Bloc e, float[] colors, Vector2 dir) {
		for (int i = -EndlessMode.fps; i < EndlessMode.fps; i++)
			colorOverTime.add(POOL.obtain().init(e.getRandomXInside(), e.getRandomYInside(), colors, dir));
	}

	private SparklesColorOverTime init(float x, float y, float[] colors, Vector2 dir) {
		tmpVector.x = (float) ( Rubico.R.nextGaussian() * Stats.V_PARTICLE_EXPLOSION_SLOW ) + ( dir.x * (Rubico.R.nextFloat() + 1) );
		tmpVector.y = (float) ( Rubico.R.nextGaussian() * Stats.V_PARTICLE_EXPLOSION_SLOW ) + ( dir.y * (Rubico.R.nextFloat() + 1) );
		init(x - HALF_WIDTH, y - HALF_WIDTH, colors, Rubico.R.nextInt(colors.length / 5), tmpVector.x, tmpVector.y, tmpVector.angle());
		return this;
	}

	private SparklesColorOverTime init(float x, float y, float[] colors, int index, float speedX, float speedY, float angle) {
		this.x = x;
		this.y = y;
		this.colors = colors;
		this.index = index;
		this.speedX = speedX;
		this.speedY = speedY;
		this.angle = angle;
		return this;
	}

	public static void add(Array<SparklesColorOverTime> colorOverTime, float[] colors, float x, float y) {
		tmpVector.x = (float) ( Rubico.R.nextGaussian() * Stats.V_PARTICLE_EXPLOSION_SLOW );
		tmpVector.y = (float) ( Rubico.R.nextGaussian() * Stats.V_PARTICLE_EXPLOSION_SLOW );
		colorOverTime.add(POOL.obtain().init(x, y, colors, Rubico.R.nextInt(colors.length) / 4, tmpVector.x, tmpVector.y, tmpVector.angle()));
	}

}