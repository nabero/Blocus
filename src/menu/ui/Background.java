package menu.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background extends UiElements {

	@Override
	public void draw(SpriteBatch batch) {
		place();
		if (tr != null) {
			batch.setColor(colors[(int) place]);
			batch.draw(tr, x - 0.5f, y - 0.5f, width + 1, height + 1);
			batch.setColor(0, 0, 0, 1);
			batch.draw(tr, x, y, width, height);
		} 
		
	}
}
