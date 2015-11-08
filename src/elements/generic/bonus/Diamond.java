package elements.generic.bonus;

import menu.ui.Overlay;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import assets.SoundMan;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.paddles.StraightPaddle;
import elements.particles.DiamondParticles;
import elements.particles.individual.PrecalculatedParticles;
import elements.world.BlocWorld;

public class Diamond {
	
	public static final Pool<Diamond> POOL = new Pool<Diamond>() {
		protected Diamond newObject() {			return new Diamond();		}
	};
	private static final float SPEED = Rubico.screenHeight * 0.05f;
	protected static final float WIDTH = Stats.UUU, HALF_WIDTH = WIDTH / 2;
	protected float x, y, place, balance;
	private final Body body;
	protected final Light light = new PointLight(BlocWorld.rayHandler, 6);
	private static final float[] colors = PrecalculatedParticles.blocBlue;
	public static final Array<Diamond> diamonds = new Array<Diamond>();
	private static final Array<DiamondParticles> particles = new Array<DiamondParticles>();
	
	public Diamond() {
		super();
		body = BlocWorld.bodyFactoryBox(new Vector2(Rubico.halfWidth - HALF_WIDTH, y), HALF_WIDTH, HALF_WIDTH, null, BodyType.KinematicBody);
		light.attachToBody(body, 0, 0);
		light.setDistance(5);
		light.setColor(PrecalculatedParticles.COLORS[3]);
	}
	
	public static void draw(SpriteBatch batch) {
		for (Diamond b : diamonds) {
			b.color();
			batch.setColor(colors[0]);
			if (EndlessMode.oneToFour == 1) {
				batch.draw(AssetMan.diamonds[Rubico.R.nextInt(6)], b.x, b.y, WIDTH, WIDTH);
			} else
				batch.draw(AssetMan.diamond, b.x, b.y, WIDTH, WIDTH);
			b.y -= SPEED * EndlessMode.delta;
			b.body.setTransform(b.x + HALF_WIDTH, b.y + HALF_WIDTH, 90);
			if (b.y < EndlessMode.ship.getTop() && b.y + WIDTH > EndlessMode.ship.y && b.x + WIDTH > EndlessMode.ship.x && b.x < EndlessMode.ship.x + StraightPaddle.WIDTH) {
				b.taken();
				b.remove();
			} else if (b.y + WIDTH < 0)
				b.remove();
		}
		DiamondParticles.act(particles, batch);
	}
	
	private void remove() {
		diamonds.removeValue(this, true);
		body.setActive(false);
		light.setActive(false);
	}

	public void taken() {
		Overlay.addTemporaryDiamond();
		SoundMan.playBruitage(SoundMan.diamondTaken);
		Rubico.profile.addDiamond(1);
		for (int i = 0; i < 10; i++) {
			DiamondParticles diamondParticles = DiamondParticles.POOL.obtain();
			diamondParticles.set(x, y);
			particles.add(diamondParticles);
		}
	}

	private void color() {
		place += balance;	
		if (place > colors.length/2)		balance -= 0.01f;
		else								balance += 0.01f;
		
		if (place < 0)						place = 0;
		else if (place >= 30)				place = 30;
	}

	public static void add(float x, float y) {
		Diamond d = POOL.obtain();
		d.x = x - HALF_WIDTH;
		d.y = y - HALF_WIDTH;
		d.body.setActive(true);
		d.light.setActive(true);
		diamonds.add(d);
	}

	public static void clear() {
		for (Diamond b : diamonds) {
			b.remove();
			b.body.setActive(false);
			b.light.setActive(false);
		}
		diamonds.clear();
		DiamondParticles.clear(particles);
	}

}
