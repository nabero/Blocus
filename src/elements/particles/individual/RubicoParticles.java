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

public class RubicoParticles implements Poolable {
	
	public static final float WIDTH = Rubico.screenWidth / 35, HALF_WIDTH = WIDTH / 2, SPEED = Rubico.tierWidth, FURIOUS = SPEED * 4;
	public static final Pool<RubicoParticles> POOL = new Pool<RubicoParticles>() {
		protected RubicoParticles newObject() {			return new RubicoParticles();		}
	};
	private float x, y, vitesseX, vitesseY, width;
	private float[] colors;
	private int index;
	public static float tmpWidth;
	
	public RubicoParticles set(float x, float y, float speedX, float speedY, float width, int index, float[] colors) {
		this.x = x;
		this.y = y;
		this.vitesseX = speedX;
		this.vitesseY = speedY;
		this.width = width;
		this.index = index;
		this.colors = colors;
		return this;
	}
	
	public RubicoParticles init(float x, float y, int i, float[] colors, float angle) {
		Rubico.tmpPos.x = 0;
		Rubico.tmpPos.y = Stats.U;
		Rubico.tmpPos.rotate(angle);
		return set(x - HALF_WIDTH, y - HALF_WIDTH, Rubico.tmpPos.x, Rubico.tmpPos.y, (Rubico.R.nextFloat() + 2) * HALF_WIDTH, Rubico.R.nextInt(colors.length / 4), colors);
	}
	
	public RubicoParticles init(float x, float y, float width, float[] colors) {
		return set(x - HALF_WIDTH, y - HALF_WIDTH, (float) (Rubico.R.nextGaussian() * SPEED), (float) (Rubico.R.nextGaussian() * SPEED), (Rubico.R.nextFloat() + 2) * HALF_WIDTH, Rubico.R.nextInt(colors.length / 4), colors);
	}
	
	public RubicoParticles initFurious(Ball v) {
		Rubico.tmpDir.set((float) (Rubico.R.nextGaussian() * FURIOUS * (Ball.CHRONO_EXPLOSIVE + 1)), 0);
		Rubico.tmpDir.rotate(v.angle);
		width = (Rubico.R.nextFloat() + 2) * HALF_WIDTH;
		Rubico.tmpPos.x = 0;
		Rubico.tmpPos.y = Ball.WIDTH;//v.width;
		Rubico.tmpPos.rotate(Rubico.R.nextFloat() * 360);
		this.colors = v.getColors();
		return set((v.centerX - width/2) + Rubico.tmpPos.x, (v.centerY - width/2) + Rubico.tmpPos.y, Rubico.tmpDir.x, Rubico.tmpDir.y, width, Rubico.R.nextInt(colors.length / 4), v.getColors());
	}
	
	public RubicoParticles init(Ball v) {
//		Rubico.tmpPos.x = 0;
//		Rubico.tmpPos.y = v.width;
//		Rubico.tmpPos.rotate(Rubico.R.nextFloat() * 360);
		this.colors = v.getColors();
		width = (Rubico.R.nextFloat() + 2) * HALF_WIDTH;
		Rubico.tmpDir.x = 0;
		Rubico.tmpDir.y = Rubico.R.nextFloat() / 2;
		Rubico.tmpDir.rotate(Rubico.R.nextFloat() * 360);
		return set(
				(v.centerX - width/2) + Rubico.tmpDir.x,
				(v.centerY - width/2) + Rubico.tmpDir.y,
				(float) (Rubico.R.nextGaussian() * SPEED), (float) (Rubico.R.nextGaussian() * SPEED),
				width, Rubico.R.nextInt(colors.length / 4), v.getColors());
	}

	@Override
	public void reset() {	}
	
	static int max;
	public static void act(Array<RubicoParticles> flammes, SpriteBatch batch) {
//		max = Math.max(max, flammes.size);
		if (EndlessMode.alternate) {
			for (final RubicoParticles f : flammes) {
				batch.setColor(f.colors[f.index]);
				batch.draw(AssetMan.dust, f.x, f.y, f.width, f.width);
				
				f.x += (f.vitesseX * EndlessMode.deltaDiv2);
				f.y += (f.vitesseY * EndlessMode.deltaDiv2);
				// left
				if (f.x < 0) {
					f.vitesseX = Math.abs(f.vitesseX);
					Bump.addLeft(f.y, WIDTH, WIDTH, f.colors);
					// right
				} else if (f.x > Rubico.screenWidth) {
					f.vitesseX = -Math.abs(f.vitesseX);
					Bump.addRight(f.y, WIDTH, WIDTH, f.colors);
				}
				if (++f.index >= f.colors.length) {
					POOL.free(f);
					flammes.removeValue(f, true);
				}
			}
		} else {
			for (final RubicoParticles f : flammes) {
				batch.setColor(f.colors[f.index]);
				batch.draw(AssetMan.dust, f.x, f.y, f.width, f.width);
				tmpWidth = f.width * 0.035f; 
				f.x += tmpWidth; 
				f.y += tmpWidth; 
				f.width -= tmpWidth;
				
				// paddle
				if (f.y < EndlessMode.ship.top) {
					if (f.x > EndlessMode.ship.x && f.x < EndlessMode.ship.right) {
						f.vitesseY = Math.abs(f.vitesseY);
						f.y = EndlessMode.ship.top;
					}
				// top
				} else if (f.y > Rubico.screenHeight) {
					f.vitesseY = -Math.abs(f.vitesseY);
					Bump.addTop(f.x, WIDTH, WIDTH, f.colors);
				}
				if (++f.index >= f.colors.length) {
					POOL.free(f);
					flammes.removeValue(f, true);
				}
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<RubicoParticles> flammes) {
		POOL.freeAll(flammes);
		flammes.clear();
	}

}