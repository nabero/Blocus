package elements.generic.paddles;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.ScreenShake;
import assets.AssetMan;
import assets.SoundMan;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.generic.blocs.Column;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.StaticSmoke;
import elements.particles.individual.explosions.FlashLight;
import elements.particles.individual.explosions.SparklesColorOverTime;
import elements.world.BlocWorld;

public class ShipShot {
	
	static final Pool<ShipShot> POOL = new Pool<ShipShot>() {		protected ShipShot newObject() {			return new ShipShot();				}	};
	public static final float WIDTH = 0.5f, SPEED = Rubico.screenHeight * 0.45f, HALF_WIDTH = WIDTH / 2;
	private static final int MAX_HP = 7;
	private float x, y, dirX, dirY;
	private int hp;
	private boolean dead;
	protected final Light light;
	private static final Rectangle R = new Rectangle(0, 0, WIDTH, WIDTH);
	
	public ShipShot() {
		light = new PointLight(BlocWorld.rayHandler, 12);
		light.setDistance(4.5f);
	}
		
	public static void add(float x, ShipPaddle paddle, float y) {
		final ShipShot left = POOL.obtain();
		left.init(paddle.x + .5f, paddle.y + 1f, paddle.lvl, 45);
		paddle.addShot(left);
		final ShipShot right = POOL.obtain();
		right.init(paddle.x + 3.5f, paddle.y + 1f, paddle.lvl, -45);
		paddle.addShot(right);
		final ShipShot mid = POOL.obtain();
		mid.init(paddle.x + 2.03f, paddle.y + 1f, paddle.lvl, 0);
		paddle.addShot(mid);
		SoundMan.playBruitage(SoundMan.shotRocket);
	}
	
	public void init(float x, float y, int lvl, float angle) {
		SparklesColorOverTime.add(x, y, (90 + angle), PrecalculatedParticles.getColors(lvl), Stats.U20, 6, 20, 3);
		this.x = x;
		this.y = y;
		Rubico.tmpDir.x = 0;
		Rubico.tmpDir.y = SPEED;
		Rubico.tmpDir.rotate(angle);
		dirX = Rubico.tmpDir.x;
		dirY = Rubico.tmpDir.y;
		hp = Math.min(MAX_HP, lvl);
		light.setColor(PrecalculatedParticles.getColor(lvl));
		light.setActive(true);
		dead = false;
	}
	
	public void act(SpriteBatch batch, ShipPaddle paddle) {
		y += EndlessMode.delta * dirY;
		x += EndlessMode.delta * dirX;
		light.setPosition(x, y);
		batch.setColor(paddle.colors[paddle.colors.length / 2 + Rubico.R.nextInt(paddle.colors.length / 2)]);
		batch.draw(AssetMan.dust, x - WIDTH * 2, y - WIDTH * 2, WIDTH * 4, WIDTH * 4);
		batch.setColor(paddle.colors[Rubico.R.nextInt(paddle.colors.length / 2)]);
		batch.draw(AssetMan.dust, x - WIDTH, y - WIDTH, WIDTH * 2, WIDTH * 2);
		batch.setColor(paddle.colors[0]);
		batch.draw(AssetMan.dust, x - HALF_WIDTH, y - HALF_WIDTH, WIDTH, WIDTH);
		batch.setColor(paddle.colors[Rubico.R.nextInt(paddle.colors.length/2)]);
		batch.draw(AssetMan.dust, x - HALF_WIDTH, y - HALF_WIDTH, WIDTH, WIDTH);
		StaticSmoke.add(x, y, 0.5f);
		StaticSmoke.add(x, y, 0.5f);
		if (x < 0) {
			SparklesColorOverTime.add(x, y, (180), PrecalculatedParticles.getColors(paddle.lvl), Stats.U20, 6, 20, 3);
			FlashLight.add(x, y, PrecalculatedParticles.getColor(paddle.lvl));
			dirX = Math.abs(dirX);
			ScreenShake.screenShake(100);
		} else if (x > Rubico.screenWidth) {
			FlashLight.add(x, y, PrecalculatedParticles.getColor(paddle.lvl));
			dirX = -Math.abs(dirX);
			SparklesColorOverTime.add(x, y, (0), PrecalculatedParticles.getColors(paddle.lvl), Stats.U20, 6, 20, 3);
			ScreenShake.screenShake(100);
		}
		if (y > Rubico.screenHeight) {
			dead = true;
		} else {
			testCollision(paddle);
		}
	}

	private void testCollision(ShipPaddle paddle) {
		updateRectanglePos();
		for (int i = 0; i < Column.COLUMNS.size; i++) {
			Column c = Column.COLUMNS.get(i);
			if (c.overlaps(x, WIDTH)) {
				for (int b = 0; b < c.getBlocs().size; b++) {
					Bloc B = c.getBlocs().get(b);
					if (testBloc(paddle.lvl, paddle, B))
						c.removeBloc(B, null);
				}
			}
			
		}
	}

	/**
	 * true if the bloc is removed
	 * @param paddle
	 * @param c
	 * @param B
	 * @return 
	 */
	public boolean testBloc(int lvl, ShipPaddle paddle, Bloc B) {
		if (B.overlaps(R)) {
			removeHp(-1);
			B.removeHp(Rubico.tmpDir.set(dirX, dirY), lvl);
			checkHp(lvl, paddle);
			if (B.hp <= 0)
				return true;
		}
		return false;
	}

	public boolean checkHp(int lvl, ShipPaddle paddle) {
		if (hp <= 0) {
			dead = true;
			ScreenShake.screenShake(500);
			FlashLight.add(x, y, PrecalculatedParticles.getColor(paddle.lvl), 3);
			SparklesColorOverTime.add(x, y, 0, PrecalculatedParticles.getColors(paddle.lvl), Stats.U20, 6, 360, 3);
			return true;
		}
		return false;
	}

	public void remove(ShipPaddle paddle) {
		light.setActive(false);
		paddle.removeShot(this);
		POOL.free(this);
	}

	public static void clear(Array<ShipShot> SHOTS) {
		for (ShipShot shot : SHOTS) 
			clear(shot);
		StaticSmoke.clear();
		POOL.freeAll(SHOTS);
		SHOTS.clear();
	}

	public static void clear(ShipShot shot) {
//		PaddleShotParticle.clear(shot.particles);
		shot.light.setActive(false);
	}

	public void updateRectanglePos() {		R.setPosition(x, y);	}
	public boolean isDead() {				return dead;			}
	public void removeHp(int i) {			hp += i;				}

}
