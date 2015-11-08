package elements.generic.paddles;

import jeu.Rubico;
import jeu.mode.EndlessMode;
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
import elements.particles.individual.PaddleShotParticle;
import elements.world.BlocWorld;

public class Homming {

	private static final Pool<PaddleShot> POOL = new Pool<PaddleShot>() {
		protected PaddleShot newObject() {
			return new PaddleShot();		
			}
	};
	public final Array<PaddleShotParticle> particles = new Array<PaddleShotParticle>();
	public static final float WIDTH = StraightPaddle.TIER_WIDTH, HEIGHT = StraightPaddle.HEIGHT, SPEED = Rubico.screenHeight * 0.55f, HALF_WIDTH = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2;
	public static final Rectangle R = new Rectangle(0,0, WIDTH, HEIGHT);
	private static final int MAX_HP = 7;
	
	private final Vector2 dir = new Vector2(0, SPEED);
	private float x, y;
	private int hp;
	private boolean dead;
	private final Body body;
	protected final Light light;
	
	public Homming() {
		body = BlocWorld.bodyFactoryBox(Vector2.Zero, HALF_WIDTH, HALF_HEIGHT, null, BodyType.KinematicBody);
		light = new PointLight(BlocWorld.rayHandler, 12);
		light.attachToBody(body, HALF_WIDTH, HALF_HEIGHT);
		light.setDistance(4.5f);
	}
	
	public void act(SpriteBatch batch, StraightPaddle paddle) {
		y += EndlessMode.delta * SPEED;
		body.setTransform(x, y, 0);
		PaddleShotParticle.add(y, x, particles, paddle);
		if (y > Rubico.screenHeight) {
			dead = true;
		} else {
			//testCollision(paddle);
			PaddleShotParticle.act(particles, batch);
		}
	}
	
}
