package elements.particles;

import jeu.Stats;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import elements.particles.individual.Dot;

public class BorderParticlesGenerator {
	
	private float width, height, done, x, y;
	private static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
	private float SPEED = Stats.U10;
	private int side;
	private final Array<Dot> particles = new Array<Dot>();
	private float[] colors;
	
	public BorderParticlesGenerator(float width, float height) {
		super();
		this.width = width;
		this.height = height;
	}

	public void act(SpriteBatch batch) {
		done += SPEED * EndlessMode.delta;
		switch (side) {
		case UP:
			if (done > width - Dot.WIDTH) {
				side = RIGHT;
				done = 0;
			} else
				particles.add(Dot.POOL.obtain().set(	x + done, 	y + height - Dot.WIDTH, 	0, colors));
			break;
		case DOWN:
			if (done > width - Dot.WIDTH) {
				side = LEFT;
				done = 0;
			} else
				particles.add(Dot.POOL.obtain().set(	(x + width) - (done + Dot.WIDTH), 	y, 							0, colors));
			break;
		case RIGHT:
			if (done > height - Dot.WIDTH) {
				side = DOWN;
				done = 0;
			} else
				particles.add(Dot.POOL.obtain().set(	x + width - Dot.WIDTH, 			(y + height) - (done + Dot.WIDTH), 0, colors));
			break;
		case LEFT:
			if (done > height - Dot.WIDTH) {
				side = UP;
				done = 0;
			} else
				particles.add(Dot.POOL.obtain().set(	x, 			y + done, 0, colors));
			break;
		}
		
		Dot.act(particles, batch);
	}
	
	public void updatePos(float x, float y) {
		for (Dot d : particles) {
			d.x += x - this.x;
			d.y += y - this.y;
		}
		this.x = x;
		this.y = y;
	}
	public void updatePosY(float y) {
		for (Dot d : particles) {
			d.y += y - this.y;
		}
		this.y = y;
	}
	
	public void updatePos(float x) {
		for (Dot d : particles) {
			d.x += x - this.x;
		}
		this.x = x;
	}
	
	public void clear() {
		Dot.clear(particles);
	}

	public void setColor(float[] colors2) {
		this.colors = colors2;
	}

	public void setPos(float x2, float y2) {
		x = x2;
		y = y2;
	}

	float percent;
	public void setSpeed(int index) {
		percent = (float)colors.length / (float)index;
		SPEED = (percent + 0.5f) * Stats.U6;
	}

}
