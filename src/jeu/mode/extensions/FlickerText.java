package jeu.mode.extensions;

import jeu.Rubico;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class FlickerText {

	private static final Array<FlickerText> TEXTS = new Array<FlickerText>();
	private static final FlickerText CONGRATS = new FlickerText("Congratulations !");
	private static final float Y = Rubico.screenHeight * 0.8f, OFFSET = Rubico.screenHeight * 0.1f;
	private float alpha = 0.5f, add = 0.1f;
	private final String text;
	
	public FlickerText(String text) {
		this.text = text;
	}
	
	public static void draw(SpriteBatch batch) {
		for (FlickerText t : TEXTS)
			t.act(batch);
	}

	public void act(SpriteBatch batch) {
		Rubico.effectFont.setColor(.2f, alpha, 1, alpha);
		Rubico.effectFont.drawMultiLine(batch, text, (Rubico.halfWidth) - Rubico.effectFont.getMultiLineBounds(text).width / 2, Y - (alpha * OFFSET));
		alpha += add;
		if (alpha > 0.5f) {
			add -= 0.0002f;
		} else {
			add += 0.0002f;
		}
		if (alpha > 1)
			alpha = 1;
		if (alpha < 0)
			alpha = 0;
	}
	

	public void reset() {
		alpha = 1;
	}

	public static void add(FlickerText text) {
		text.reset();
		if (!TEXTS.contains(text, true))
			TEXTS.add(text);
	}

	public static void remove(FlickerText moveTt) {
		TEXTS.removeValue(moveTt, true);
	}
	
	public static void congrats() {
		if (!TEXTS.contains(CONGRATS, true)) {
			TEXTS.add(CONGRATS);
		}
	}

	public static void clear() {
		TEXTS.clear();
	}
}

