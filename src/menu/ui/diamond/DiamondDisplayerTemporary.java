package menu.ui.diamond;

import jeu.mode.EndlessMode;
import menu.ui.Overlay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DiamondDisplayerTemporary extends DiamondDisplayer {
	
	@Override
	public void draw(SpriteBatch batch) {
		time += EndlessMode.delta;
		batch.setColor(0.05f, 1, 1, Math.min(2 - time, 1));
		if (time > 2f)
			Overlay.remove(this);
		super.draw(batch);
	}

}
