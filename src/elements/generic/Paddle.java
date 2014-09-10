package elements.generic;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.TemporaryText;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import elements.particles.Particles;
import elements.particles.individual.Bump;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.SparklesColorOverTime;

public final class Paddle {

	public static final int WIDTH = (int) (Rubico.screenWidth * 0.25f), HALF_WIDTH = WIDTH/2, INITIAL_Y = (int) (Rubico.screenHeight * 0.20f);
	public static float y = INITIAL_Y, xCenter = 0, yCenter = 0, x, MIN = (Rubico.screenWidth * 0.02f), top, right;
	public static final float TIER_WIDTH = WIDTH / 3, TIER_WIDTH_MUL2 = TIER_WIDTH * 2, INC = 0.3f, HEIGHT = Rubico.screenHeight * 0.018f, HALF_HEIGHT = HEIGHT / 2, TIER_WIDTH_DIV2 = TIER_WIDTH / 2,
			 BALL_IMPACT = Rubico.screenHeight * 0.02f, RECOVERY = Rubico.screenHeight * 0.005f;
	private static float nextShot1, nextShot2, nextShot3, decrease;
	private static boolean shot1, shot2, shot3;
	public static int lvl;
	private static int index1, index2, index3;
	public static float[] colors;
	private static float magnet = 0;
	public static boolean magnetActivated = false;
	private static final TemporaryText MAGNET = new TemporaryText("Magnet");
	
	public void initialiser() {
		lvl = Rubico.profile.lvlPaddle;
		xCenter = Rubico.screenWidth * 0.5f;
		yCenter = Rubico.screenHeight * 0.1f;
		x = xCenter - HALF_WIDTH;
		shot1 = false;
		shot2 = false;
		shot3 = false;
		colors = PrecalculatedParticles.getColor(lvl);
		index1 = colors.length - 1;
		index2 = colors.length - 1;
		index3 = colors.length - 1;
		nextShot1 = 0;
		nextShot2 = 0;
		nextShot3 = 0;
		decrease = 0;
		magnet = 0;
		magnetActivated = false;
	}

	public void draw(SpriteBatch batch) {
		if (index1 >= colors.length) {
			index1 = colors.length - 1;
			shot1 = false;
		} else if (index1 <= 0) {
			shot1 = true;
			index1 = 0;
		}
		if (index2 >= colors.length) {
			index2 = colors.length - 1;
			shot2 = false;
		} else if (index2 <= 0) {
			shot2 = true;
			index2 = 0;
		}
		if (index3 >= colors.length) {
			index3 = colors.length - 1;
			shot3 = false;
		} else if (index3 <= 0) {
			shot3 = true;
			index3 = 0;
		}
		
		batch.setColor(colors[PrecalculatedParticles.SMALLL_MAX]);
//		for (int i = 0; i < 2; i++) {
			batch.draw(AssetMan.debris, x, y, TIER_WIDTH, HEIGHT);
			batch.draw(AssetMan.debris, x + TIER_WIDTH, y, TIER_WIDTH, HEIGHT);
			batch.draw(AssetMan.debris, x + TIER_WIDTH_MUL2, y, TIER_WIDTH, HEIGHT);
//		}
		
		batch.setColor(colors[index1]);
		batch.draw(AssetMan.debris, 
				(x +  TIER_WIDTH_DIV2) - ((TIER_WIDTH / getInnerDiv(index1)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index1)) / 2), 
				TIER_WIDTH / getInnerDiv(index1), 				HEIGHT / getInnerDiv(index1));
		batch.draw(AssetMan.debris, 
				(x +  TIER_WIDTH_DIV2) - ((TIER_WIDTH / getInnerDiv(index1)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index1)) / 2), 
				TIER_WIDTH / getInnerDiv(index1), 				HEIGHT / getInnerDiv(index1));
		
		batch.setColor(colors[index2]);
		batch.draw(AssetMan.debris, 
				(x +  TIER_WIDTH_DIV2 + TIER_WIDTH) - ((TIER_WIDTH / getInnerDiv(index2)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index2)) / 2), 
				TIER_WIDTH / getInnerDiv(index2), 				HEIGHT / getInnerDiv(index2));
		batch.draw(AssetMan.debris, 
				(x +  TIER_WIDTH_DIV2 + TIER_WIDTH) - ((TIER_WIDTH / getInnerDiv(index2)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index2)) / 2), 
				TIER_WIDTH / getInnerDiv(index2), 				HEIGHT / getInnerDiv(index2));
		
		batch.setColor(colors[index3]);
		batch.draw(AssetMan.debris, 
				(x + TIER_WIDTH_DIV2 + TIER_WIDTH_MUL2) - ((TIER_WIDTH / getInnerDiv(index3)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index3)) / 2), 
				TIER_WIDTH / getInnerDiv(index3), 				HEIGHT / getInnerDiv(index3));
		batch.draw(AssetMan.debris, 
				(x + TIER_WIDTH_DIV2 + TIER_WIDTH_MUL2) - ((TIER_WIDTH / getInnerDiv(index3)) / 2), 	(y + HALF_HEIGHT) - ((HEIGHT / getInnerDiv(index3)) / 2), 
				TIER_WIDTH / getInnerDiv(index3), 				HEIGHT / getInnerDiv(index3));
		
		if (EndlessMode.alternate && magnetActivated)
			for (float f = 0; f < magnet; f = f + 0.33f ) {
				Particles.BUMPS.add(Bump.POOL.obtain().init(
						(x + Rubico.R.nextFloat() * WIDTH) - SparklesColorOverTime.HALF_WIDTH, top, PrecalculatedParticles.brightYellow, 
						SparklesColorOverTime.WIDTH, (float) (SparklesColorOverTime.WIDTH2 + (SparklesColorOverTime.WIDTH2 * Rubico.R.nextGaussian())), PrecalculatedParticles.SMALLL_MAX));
			}
		
		if (y < INITIAL_Y) {
			y += RECOVERY;
		} else {
			y = INITIAL_Y;
		}
		top = y + HEIGHT;
		right = x + WIDTH;
		paddleShot();
		
		if (magnet > 0) {
			magnet -= Gdx.graphics.getDeltaTime();
		} else {
			magnet = 0;
			magnetActivated = false;
			for (Ball b : Ball.BALLS) {
				b.updateDir();
			}
		}
	}

	protected void paddleShot() {
		if (shot1 && nextShot1 < EndlessMode.now) {
			nextShot1 = EndlessMode.now + 0.5f;
			PaddleShot.add(x);
			index1 += 13;
		}
		if (shot2 && nextShot2 < EndlessMode.now) {
			nextShot2 = EndlessMode.now + 0.5f;
			PaddleShot.add(x + TIER_WIDTH);
			index2 += 13;
		} 
		if (shot3 && nextShot3 < EndlessMode.now) {
			nextShot3 = EndlessMode.now + 0.5f;
			PaddleShot.add(x + TIER_WIDTH_MUL2);
			index3 += 13;
		}
		if (decrease < EndlessMode.now) {
			index1++;
			index2++;
			index3++;
			decrease = EndlessMode.now + 0.5f;
		}
	}

	public void move() {
		Paddle.x = Gdx.input.getX() - HALF_WIDTH;
		xCenter = x + HALF_WIDTH;
	}

	public float getRandomXInside() {			return x + (Rubico.R.nextFloat() * WIDTH);	}
	protected float getInnerDiv(int index) {	return Math.max(index/5f, 1);				}
	public float getRandomYInside() {			return y + HALF_HEIGHT;						}

	public static void hit(Ball b) {
		b.y = y + HEIGHT;
		y -= BALL_IMPACT;
		b.hit(90, 60);
		b.dir.y = Math.abs(b.dir.y);
		if (b.x < x + TIER_WIDTH) {
			b.dir.x = -Math.abs(b.dir.x);
			index1 -=  10 + (b.lvl);
		} else if (b.x < x + TIER_WIDTH_MUL2) {
			index2 -= 10 + (b.lvl);
		} else { 
			b.dir.x = Math.abs(b.dir.x);
			index3 -= 10 + (b.lvl);
		}
	}

	public static void magnet() {
		TemporaryText.add(MAGNET);
		magnet += 5;
		magnetActivated = true;
		Ball.saveDir();
	}

}