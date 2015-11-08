package elements.particles.individual.explosions;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.particles.Particles;
import elements.particles.individual.Bump;
import elements.world.BlocWorld;

public class ExplodingBloc {

	private float x, y, color;
	private float[] colors;
	private int ttl;
	private float dirX, dirY;
	private static final int NB_BLOCS = 3;
	private static final float WIDTH = Bloc.WIDTH / NB_BLOCS, HEIGHT = Bloc.HEIGHT / NB_BLOCS , HALF_WIDTH = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2;
	private Body body = BlocWorld.bodyFactoryBox(Vector2.Zero, HALF_WIDTH, HALF_HEIGHT, null, BodyType.KinematicBody);
//	private Light light = getLight();
	private Color colorAsColor;
	public static final Pool<ExplodingBloc> POOL = new Pool<ExplodingBloc>() {
		protected ExplodingBloc newObject() {			return new ExplodingBloc();		}
	};

	public static void explode(Bloc b, Array<ExplodingBloc> array, Vector2 dir) {
//		FlashLight.add(x + HALF_WIDTH, y + HALF_HEIGHT, colorAsColor, 6);
		FlashLight.add(b.x + Bloc.HALF_WIDTH, b.y + Bloc.HALF_HEIGHT, b.light.getColor(), 12);
		
		for (int x = 0; x < NB_BLOCS; x++) {
			
//			if (x - 2 >= 0) {
//				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//						, dir.x, dir.y, b.light.getColor()
//						));
//				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (3 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//						, dir.x, dir.y, b.light.getColor()
//						));
//			} else {
//				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//						, dir.x, dir.y, b.light.getColor()
//						));
//				array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (3 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//						, dir.x, dir.y, b.light.getColor()
//						));
//			}
			array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
					, dir.x, dir.y, b.light.getColor()
					));
			array.add(POOL.obtain().init(b.x + (x * Bloc.QUART_WIDTH), b.y + (3 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
					, dir.x, dir.y, b.light.getColor()
					));
		}
		
		// CENTER -- SHADOW
		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors
				, dir.x, dir.y, b.light.getColor()
				));
		
		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (0 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
				, dir.x, dir.y, b.light.getColor()
				));
		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
				, dir.x, dir.y, b.light.getColor()
				));

//		// x = 0, y = 1
//		array.add(POOL.obtain().init(b.x + (0 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 0, y = 2
//		array.add(POOL.obtain().init(b.x + (0 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 3, y = 1
//		array.add(POOL.obtain().init(b.x + (3 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 3, y = 2
//		array.add(POOL.obtain().init(b.x + (3 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(int) b.place], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//
//		// x = 1, y = 1
//		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 1, y = 2
//		array.add(POOL.obtain().init(b.x + (1 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 2, y = 1
//		array.add(POOL.obtain().init(b.x + (2 * Bloc.QUART_WIDTH), b.y + (1 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
//		// x = 2, y = 2
//		array.add(POOL.obtain().init(b.x + (2 * Bloc.QUART_WIDTH), b.y + (2 * Bloc.QUART_HEIGHT), b.colors[(b.colors.length) - (int) (b.place)], b.colors
//				, dir.x, dir.y, b.light.getColor()
//				));
	}

	private Light getLight() {
		Light tmp = new PointLight(BlocWorld.rayHandler, 5);
		tmp.setDistance(2.5f);
		tmp.attachToBody(body, Bloc.QUART_WIDTH, Bloc.QUART_HEIGHT);
		tmp.setActive(false);
		return tmp;
	}

	private ExplodingBloc init(float x, float y, float color, float[] colors, float dirX, float dirY, Color color2) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.ttl = Rubico.R.nextInt(35);
		this.colors = colors;
		this.dirX = (float) (dirX + (Rubico.R.nextGaussian() * 6));
		this.dirY = (float) (dirY + (Rubico.R.nextGaussian() * 6));
		body.setActive(true);
//		light.setActive(true);
//		light.setColor(color2);
		colorAsColor = color2;
		return this;
	}

	static int tmp;
	public static void draw(SpriteBatch batch, Array<ExplodingBloc> array) {
		FlashLight.act();
		if (EndlessMode.alternate) {
			tmp = array.size / 10;
			for (ExplodingBloc e : array) {
//				if (EndlessMode.fps < 25)
//					e.ttl += 5;
				batch.setColor(e.color);
				batch.draw(AssetMan.partBloc, e.x, e.y, WIDTH, HEIGHT);
//			e.y -= (e.ttl * EndlessMode.delta);
				e.y += e.dirY * EndlessMode.delta;
				e.x += e.dirX * EndlessMode.delta;
				e.body.setTransform(e.x, e.y, 0);
				e.ttl += tmp; 
				if (++e.ttl > 50) {
					e.remove(array);
				}
			}
		} else {
			for (ExplodingBloc e : array) {
				batch.setColor(e.color);
				batch.draw(AssetMan.partBloc, e.x, e.y, WIDTH, HEIGHT);
				if (e.x < 0) {
					e.dirX = Math.abs(e.dirX);
					Bump.addLeft(e.y, WIDTH, HEIGHT, e.colors);
					// right
				} else if (e.x > Rubico.screenWidth) {
					e.dirX = -Math.abs(e.dirX);
				}
				// paddle
				if (e.y < Rubico.halfHeight) {
//					e.remove(array);
					e.ttl += 5;
					// top
				} else if (e.y > Rubico.screenHeight) {
					Bump.addTop(e.x, WIDTH, HEIGHT, e.colors);
					e.dirY = -Math.abs(e.dirY);
				}
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	private void remove(Array<ExplodingBloc> array) {
		POOL.free(this);
		array.removeValue(this, true);
		body.setActive(false);
//					e.light.setActive(false);
		SparklesColorOverTime.add(Particles.COLOR_OVER_TIME, colors, x, y, 6);
		if (EndlessMode.fps > 30)
			FlashLight.add(x + HALF_WIDTH, y + HALF_HEIGHT, colorAsColor, 6);
	}
	
	public static void clear(Array<ExplodingBloc> blocs) {
		for (ExplodingBloc b : blocs) {
			b.body.setActive(false);
//			b.light.setActive(false);
		}
		POOL.freeAll(blocs);
		blocs.clear();
	}
}