package jeu.mode.extensions;

import jeu.Rubico;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TemporaryText {

	private static final Array<TemporaryText> TEXTS = new Array<TemporaryText>();
	private static final float Y = Rubico.screenHeight * 0.35f, OFFSET = Rubico.screenHeight * 0.1f;
	public float alpha = 0;
	private final String text;
	
	public TemporaryText(String text) {
		this.text = text;
	}
	
	public static void draw(SpriteBatch batch) {
		for (TemporaryText t : TEXTS)
			if (t.act(batch))
				TEXTS.removeValue(t, true);
	}

	public boolean act(SpriteBatch batch) {
		Rubico.effectFont.setColor(.2f, alpha, 1, alpha);
		Rubico.effectFont.drawMultiLine(batch, text, (Rubico.halfWidth) - Rubico.effectFont.getMultiLineBounds(text).width / 2, Y - (alpha * OFFSET));
		alpha -= EndlessMode.deltaDiv2;
		return alpha <= 0;
	}
	

	public void reset() {
		alpha = 1;
	}

	public static void add(TemporaryText text) {
		text.reset();
		if (!TEXTS.contains(text, true))
			TEXTS.add(text);
	}
}

