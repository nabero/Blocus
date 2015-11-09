package menu.ui.button;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import menu.OnClick;
import menu.ui.Barre;

public class TextButton extends SimpleButton {
	
	private BitmapFont font;
	private String text = "";
	private TextBounds bounds;
	
	public TextButton(String text, BitmapFont font, float centerX, float y, OnClick click) {
		super(centerX - font.getBounds(text).width / 2, y, click, font.getBounds(text).width, font.getBounds(text).height);
		this.font = font;
		this.text = text;
		bounds = font.getBounds(text);
	}
	
	public TextButton(String s, BitmapFont font, float centerX, float y) {
		this(s, font, centerX, y, null);
	}

	@Override
	protected void insideDraw(SpriteBatch batch) {
		font.draw(batch, text, x + PADDING_BARRE + Barre.HEIGHT, y + bounds.height + PADDING_BARRE);
	}

	public void setTexte(String texte) {
		this.text = texte;
		updateWidth((font.getBounds(texte).width) + PADDING_BARRE * 2, x - PADDING_BARRE);		
	}
	
	public BitmapFont font() {
		return font;
	}
}