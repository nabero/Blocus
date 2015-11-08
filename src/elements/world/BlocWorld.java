package elements.world;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BlocWorld {
	
	public static final World world = new World(Vector2.Zero, true);
	public static final RayHandler rayHandler = new RayHandler(world);
//	private static final DirectionalLight dirLight = new DirectionalLight(rayHandler, 64, Color.BLACK, 45);
//	private static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public static void act() {
//		if (Gdx.app.getVersion() == 0)
//			debugRenderer.render(world, Rubico.tmpCombined);
//		world.step(EndlessMode.delta, 6, 2);
//		
//		world.setContactListener(new ContactListener() {
//			
//			@Override
//			public void preSolve(Contact arg0, Manifold arg1) {
//			}
//			
//			@Override
//			public void postSolve(Contact arg0, ContactImpulse arg1) {
//			}
//			
//			@Override
//			public void endContact(Contact arg0) {
//				System.out.println("                         BlocWorld.act().new ContactListener() {...}.endContact()");
//				Collidable a = (Collidable) arg0.getFixtureA().getBody().getUserData();
//				Collidable b = (Collidable) arg0.getFixtureB().getBody().getUserData();
//				if (a != null) 
//					a.collideWith(b);
//				if (b != null)
//					b.collideWith(a);
//			}
//			
//			@Override
//			public void beginContact(Contact arg0) {
//			}
//		});
//		
		if (Rubico.profile.lights()) {
			rayHandler.setCombinedMatrix(Rubico.tmpCombined);
			rayHandler.updateAndRender();
		}
	}

	private float accumulator = 0;
	private static final float TIME_STEP = 1f / 45f;
	private void doPhysicsStep(float deltaTime) {
	    // fixed time step max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(deltaTime, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= TIME_STEP) {
	        world.step(TIME_STEP, 6, 2);
	        accumulator -= TIME_STEP;
	    }
	}
	public static Body bodyFactoryBox(Vector2 pos, float halfWidth, float halfHeight, Collidable collidable, BodyType bodyType) {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.KinematicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(100, 300);

		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		PolygonShape polygon = new PolygonShape();
		polygon.setAsBox(halfWidth, halfHeight);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = polygon;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		body.setActive(false);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		polygon.dispose();
		return body;
	}
	public static void setDirLight(float r, float g, float b, float a) {
//		dirLight.setColor(r, g, b, a);
	}
	public static void initWalls() {
		Wall.RIGHT.getBody().setTransform(new Vector2(Rubico.screenWidth + 0.95f, 5), 0);
		Wall.LEFT.getBody().setTransform(new Vector2(-0.95f, 5), 0);
		Wall.UP.getBody().setTransform(new Vector2(0, Rubico.screenHeight + 0.95f), 0);
	}
}
