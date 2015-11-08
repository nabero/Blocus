package elements.generic.paddles;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
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
import elements.particles.individual.PaddleShotParticle;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.SparklesColorOverTime;
import elements.world.BlocWorld;

public class SpreadShot {
	
	private static final Pool<SpreadShot> POOL = new Pool<SpreadShot>() {		protected SpreadShot newObject() {			return new SpreadShot();				}	};
	public final Array<PaddleShotParticle> particles = new Array<PaddleShotParticle>();
	public static final float WIDTH = StraightPaddle.TIER_WIDTH, HEIGHT = StraightPaddle.HEIGHT, SPEED = Rubico.screenHeight * 0.30f, HALF_WIDTH = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2;
	public static final Rectangle R = new Rectangle(0,0, WIDTH, HEIGHT);
	private static final Vector2 DIR = new Vector2(0, SPEED);
	private static final int MAX_HP = 7;
	private float x, y;
	private int hp;
	private boolean dead, spread = false;
	private final Body body;
	protected final Light light;
	
	public SpreadShot() {
		body = BlocWorld.bodyFactoryBox(Vector2.Zero, HALF_WIDTH, HALF_HEIGHT, null, BodyType.KinematicBody);
		light = new PointLight(BlocWorld.rayHandler, 12);
		light.attachToBody(body, HALF_WIDTH, HALF_HEIGHT);
		light.setDistance(4.5f);
	}
		
	public static void add(float x, SpreadPaddle paddle) {
		final SpreadShot shot = POOL.obtain();
		PaddleShotParticle.clear(shot.particles);
		shot.x = x;
		shot.y = paddle.y;
		shot.hp = Math.min(MAX_HP, paddle.lvl);
		shot.body.setActive(true);
		shot.light.setColor(PrecalculatedParticles.getColor(paddle.lvl));
		shot.light.setActive(true);
		shot.dead = false;
		shot.spread = false;
		paddle.addShot(shot);
		Rubico.tmpPos.set(x + HALF_WIDTH, shot.y);
		SparklesColorOverTime.add(Rubico.tmpPos.x, Rubico.tmpPos.y, 90, PrecalculatedParticles.getColors(paddle.lvl), Stats.U20, 6, 90, 3);
		paddle.y -= paddle.getHeight();
		SoundMan.playBruitage(SoundMan.shotRocket);
	}
	
	public static void add(float x, SpreadPaddle paddle, float y) {
		final SpreadShot shot = POOL.obtain();
		PaddleShotParticle.clear(shot.particles);
		shot.x = x;
		shot.y = y;
		shot.hp = Math.min(MAX_HP, paddle.lvl);
		shot.body.setActive(true);
		shot.light.setColor(PrecalculatedParticles.getColor(paddle.lvl));
		shot.light.setActive(true);
		shot.dead = false;
		shot.spread = true;
		paddle.addShot(shot);
		Rubico.tmpPos.set(x + HALF_WIDTH, shot.y);
		SparklesColorOverTime.add(Rubico.tmpPos.x, Rubico.tmpPos.y, 90, PrecalculatedParticles.getColors(paddle.lvl), Stats.U20, 6, 90, 3);
		SoundMan.playBruitage(SoundMan.shotRocket);
	}
	
	public void act(SpriteBatch batch, SpreadPaddle paddle) {
		y += EndlessMode.delta * SPEED;
		body.setTransform(x, y, 0);
		batch.setColor(paddle.colors[0]);
		batch.draw(AssetMan.partBloc, x, y, StraightPaddle.TIER_WIDTH, StraightPaddle.HEIGHT);
		PaddleShotParticle.add(y, x, particles, paddle);
		if (y > Rubico.screenHeight) {
			dead = true;
		} else {
			if (y > 14 && !spread) {
				add(x + WIDTH * 2, paddle, y - 1f);
				add(x + WIDTH, paddle, y - 0.5f);
				add(x - WIDTH, paddle, y - 0.5f);
				add(x - WIDTH * 2, paddle, y - 1f);
				spread = true;
			}
			testCollision(paddle);
			PaddleShotParticle.act(particles, batch);
		}
	}

	private void testCollision(SpreadPaddle paddle) {
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
	public boolean testBloc(int lvl, SpreadPaddle paddle, Bloc B) {
		if (B.overlaps(R)) {
			removeHp(-1);
			B.removeHp(DIR, lvl);
			checkHp(lvl, paddle);
			if (B.hp <= 0)
				return true;
		}
		return false;
	}

	public boolean checkHp(int lvl, SpreadPaddle paddle) {
		if (hp <= 0) {
			dead = true;
			return true;
		}
		return false;
	}

	public void remove(SpreadPaddle paddle) {
		body.setActive(false);
		light.setActive(false);
		paddle.removeShot(this);
		POOL.free(this);
	}

	public static void clear(Array<SpreadShot> SHOTS) {
		for (SpreadShot shot : SHOTS) 
			clear(shot);
		POOL.freeAll(SHOTS);
		SHOTS.clear();
	}

	public static void clear(SpreadShot shot) {
		PaddleShotParticle.clear(shot.particles);
		shot.body.setActive(false);
		shot.light.setActive(false);
	}

	public void updateRectanglePos() {		R.setPosition(x, y);	}
	public boolean isDead() {				return dead;			}
	public void removeHp(int i) {			hp += i;				}

}
