package elements.generic.bonus;

import jeu.Rubico;
import jeu.Stats;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.generic.blocs.Bloc;
import elements.generic.paddles.StraightPaddle;
import elements.world.BlocWorld;
import elements.world.Collidable;

public abstract class Bonus {
	
	protected static final float WIDTH = Stats.UU, HALF_WIDTH = WIDTH / 2;
	protected float x, y, place, balance;
	public static Array<Bonus> BONUSES = new Array<Bonus>();
	public static float noWalls;
	private static final float SPEED = Rubico.screenHeight * 0.1f;
	private final Body body;
	protected final Light light = new PointLight(BlocWorld.rayHandler, 6);
	private float angle;
	
	public Bonus() {
		body = BlocWorld.bodyFactoryBox(new Vector2(Rubico.halfWidth - HALF_WIDTH, y), HALF_WIDTH, HALF_WIDTH, null, BodyType.KinematicBody);
		light.attachToBody(body, 0, 0);
		light.setDistance(3);
	}
	
	public static void draw(SpriteBatch batch) {
		if (noWalls >= 0)
			noWalls -= EndlessMode.delta;
		for (Bonus b : BONUSES) {
			b.color();
			batch.setColor(b.getColors()[(int) b.place]);
			b.angle += EndlessMode.delta * 60;
			batch.draw(AssetMan.bonus, b.x - b.getHalfWidth(), b.y - b.getHalfHeight(), HALF_WIDTH, HALF_WIDTH, WIDTH, WIDTH, 1f, 1f, b.angle);
//			batch.draw(AssetMan.bonus, b.x - b.getHalfWidth(), b.y - b.getHalfHeight(), b.getWidth(), b.getHeight());
			b.drawMe(batch);
			b.y -= SPEED * EndlessMode.delta;
			b.body.setTransform(b.x, b.y, b.angle + 90);
			if (b.y < EndlessMode.ship.getTop() && b.y > EndlessMode.ship.y && b.x + b.getWidth() > EndlessMode.ship.x && b.x < EndlessMode.ship.x + StraightPaddle.WIDTH) {
				b.taken();
				b.remove();
			} else if (b.y + b.getHeight() < 0)
				b.remove();
		}
		Diamond.draw(batch);
	}
	
	private void color() {
		place += balance;
		if (place > getColors().length/2)	balance -= 0.01f;
		else								balance += 0.01f;
		
		if (place < 0)						place = 0;
		else if (place >= 30)				place = 30;
	}

	abstract float[] getColors();

	public Bonus init(float x, float y) {
		body.setActive(true);
		body.setTransform(x, y, 90);
		this.x = x;
		this.y = y;
		light.setActive(true);
		return this;
	}
	
	float getWidth() {			return WIDTH;							}
	float getHeight() {			return WIDTH;							}
	float getHalfWidth() {		return HALF_WIDTH;						}
	float getHalfHeight() {		return HALF_WIDTH;						}
	abstract void drawMe(SpriteBatch batch);
	abstract void taken();
	
	void remove() {
		BONUSES.removeValue(this, true);
		body.setActive(false);
		light.setActive(false);
	}

	public static void add(Bonus init) {
		BONUSES.add(init);
	}

	public static void clear() {
		for (Bonus b : BONUSES) {
			b.remove();
			b.body.setActive(false);
			b.light.setActive(false);
		}
		BONUSES.clear();
		noWalls = 0;
		Diamond.clear();
	}

	public static void blocTaken(Bloc b) {
		if (Ball.BALLS.size == 0)
			return;
		if (rollForBonus(15))
			Bonus.add(BonusBall.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
//		if (rollForBonus(15))
//			Bonus.add(BonusNoWalls.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
//		if (rollForBonus(1))
//			Bonus.add(BonusMagnet.POOL.obtain().init(x + HALF_WIDTH, y));
		if (rollForBonus(30))
			Bonus.add(BonusExplosive.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
		if (EndlessMode.endless) {
			if (Rubico.R.nextInt(60) == 1 && Diamond.diamonds.size < 2)
				Diamond.add(b.x + b.WIDTH, b.y + b.HEIGHT);
		} else {
			if (Rubico.R.nextInt(10) == 1 && Builder.nbrOfDiamondsPerLelve > 0 && Diamond.diamonds.size < 2) {
				Builder.nbrOfDiamondsPerLelve--;
				Diamond.add(b.x + b.WIDTH, b.y + b.HEIGHT);
			}
		}
	}

	protected static boolean rollForBonus(int chance) {
		if (Bloc.first) {
			Bloc.first = false;
			return true;
		}
		return Rubico.R.nextInt((int) (1 + (chance * (Ball.BALLS.size * 2.5f + Bonus.BONUSES.size)))) == 0;		
	}
}
