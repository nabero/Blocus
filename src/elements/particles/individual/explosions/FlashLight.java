package elements.particles.individual.explosions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import box2dLight.Light;
import box2dLight.PointLight;
import elements.world.BlocWorld;

public class FlashLight {
	
	private static final Array<FlashLight> explosions = new Array<FlashLight>();
	private final Light light = new PointLight(BlocWorld.rayHandler, 5);
//	private int ttl;
	public static final Pool<FlashLight> POOL = new Pool<FlashLight>() {
		protected FlashLight newObject() {			return new FlashLight();		}
	};
	private float diminish;
	
	public static final void act() {
		for (FlashLight f : explosions) {
			f.light.setDistance(f.light.getDistance() * f.diminish);
//			if (++f.ttl > 60) {
			if (f.light.getDistance() < 1) {
				f.light.setActive(false);
				POOL.free(f);
				explosions.removeValue(f, true);
			}
		}
	}
	
	public static final void clear() {
		POOL.freeAll(explosions);
		for (FlashLight f: explosions)
			f.light.setActive(false);
	}
	
	public static FlashLight add(float x, float y, Color color, float originalDistance) {
		FlashLight f = POOL.obtain();
		f.light.setPosition(x, y);
		f.light.setColor(color);
		f.light.setActive(true);
		f.light.setDistance(originalDistance);
//		f.ttl = 0;
		explosions.add(f);
		f.diminish = 0.9f;
		return f;
	}

	public static void add(float x, float y, Color color) {
		add(x, y, color, 25).diminish = 0.96f;
	}

}
