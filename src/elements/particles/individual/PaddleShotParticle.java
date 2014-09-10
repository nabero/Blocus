package elements.particles.individual;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Paddle;
import elements.generic.PaddleShot;
import elements.particles.Particles;

public class PaddleShotParticle {
	
	private static final float REDUCTION = 0.93f, SPEED = PaddleShot.WIDTH * 2;
	private float x, y, speedX, width, height;
	private int index;
	public static final Pool<PaddleShotParticle> POOL = new Pool<PaddleShotParticle>() {
		protected PaddleShotParticle newObject() {			return new PaddleShotParticle();		}
	};
	
	public static void act(Array<PaddleShotParticle> explosions, SpriteBatch batch) {
		for (PaddleShotParticle e : explosions) {
			batch.setColor(Paddle.colors[e.index]);
			batch.draw(AssetMan.debris, e.x, e.y, e.width, e.height);
			e.x += (e.width - (e.width * REDUCTION)) / 2;
			e.y += (e.height - (e.height * REDUCTION)) / 2;
			e.width *= REDUCTION;
			e.height *= REDUCTION;
			e.x += e.speedX * EndlessMode.delta;
			// left
			if (e.x < 0) {
				e.speedX = Math.abs(e.speedX);
				Bump.addLeft(e.y, e.height, e.width, Paddle.colors);
				// right
			} else if (e.x > Rubico.screenWidth) {
				e.speedX = -Math.abs(e.speedX);
				Bump.addRight(e.y, e.height, e.width, Paddle.colors);
			}
			
			if (++e.index >= Paddle.colors.length) {
				POOL.free(e);
				explosions.removeValue(e, true);
			}
				
		}
		batch.setColor(AssetMan.WHITE);
	}
	
	public static void clear(Array<PaddleShotParticle> explosions) {
		POOL.freeAll(explosions);
		explosions.clear();
	}

	public static void add(float y, float x) {
		PaddleShotParticle p = POOL.obtain();
		p.x = x;
		p.y = y;
		p.width = PaddleShot.WIDTH;
		p.height = PaddleShot.HEIGHT;
		p.index = 0;
		p.speedX = (float) (Rubico.R.nextGaussian() * SPEED);
		Particles.PADDLE_SHOT.add(p);
	}

}
