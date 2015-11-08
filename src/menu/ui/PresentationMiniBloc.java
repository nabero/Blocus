package menu.ui;

import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import elements.generic.blocs.MiniBloc;

public class PresentationMiniBloc extends MiniBloc {
	
	private static final float WIDTH = MiniBloc.WIDTH / PresentationBloc.DIV, HEIGHT = MiniBloc.HEIGHT / PresentationBloc.DIV;
	public boolean display = true;
	
	@Override
	public void draw(SpriteBatch batch) {
		if (!display) {
			x += (ancreX - x) * EndlessMode.delta4;
			y += (ancreY - y) * EndlessMode.delta4;
			return;
		}
		super.draw(batch);
	}
	@Override
	public void drawSimple(SpriteBatch batch) {
		if (!display)
			return;
		super.drawSimple(batch);
	}
	
	@Override	protected float getWidth() {		return WIDTH;	}
	@Override	protected float getHeight() {		return HEIGHT;	}
	
	private static final Rectangle miniBloc = new Rectangle(), frame = new Rectangle();
	public boolean isInside(float x, float y, float width, float height) {
		miniBloc.set(this.x, this.y, WIDTH, HEIGHT);
		frame.set(x, y, width, height);
		return frame.contains(miniBloc);
	}
	
	public void moveY(float delta) {
		y += delta;
		ancreY += delta;
	}
	

}
