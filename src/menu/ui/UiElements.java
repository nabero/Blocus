package menu.ui;

import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import menu.OnClick;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import elements.particles.individual.PrecalculatedParticles;

public class UiElements extends AbstractButton {
	
	private Array<UiElements> subElements = new Array<UiElements>(); 
	protected float width, height, angle, place, balance, time;
	private static final float MIN = 0, MAX = 30, OFFSET = Stats.U;
	protected TextureRegion tr;
	private String s;
	public float[] colors = PrecalculatedParticles.uiColor;
	public OnClick onClick;
	private boolean clickable = true;
	
	public void setTexture(TextureRegion arrow) {
		tr = arrow;
	}

	public void setBarres() {
		// Bottom
		horizontalBarre(x - OFFSET, 					y - OFFSET, 			width + Barre.HEIGHT + OFFSET * 2);
		// Top
		horizontalBarre(x - OFFSET, 					getYOffset() + height, 	width + Barre.HEIGHT + OFFSET * 2);
		// Left
		verticalBarre((x - OFFSET) - Barre.HALF_HEIGHT, y - OFFSET, 			height + OFFSET * 2);
		verticalBarre((x + OFFSET) + width - Barre.HALF_HEIGHT, y - OFFSET, 			height + OFFSET * 2);
	}
	
	public float getYOffset() {
		return y + OFFSET;
	}
	public float getXOffset() {
		return x + OFFSET;
	}

	public void draw(SpriteBatch batch) {
		time += Gdx.graphics.getDeltaTime();
		place();
		if (tr != null) {
			
//			batch.setColor(colors[(int) (MAX - place)]);
			batch.setColor(AssetMan.BLACK);
			batch.draw(AssetMan.buttonBackground, x - OFFSET, y - OFFSET, width + OFFSET * 2, height + OFFSET * 2);
//			batch.draw(AssetMan.buttonBackground, x - OFFSET, y - OFFSET, (width + OFFSET * 2) / 2, (height + OFFSET * 2) / 2, width + OFFSET * 2, height + OFFSET * 2, 1f, 1f, angle);
			batch.setColor(colors[(int) place]);
			batch.draw(tr, x, y, width / 2, height / 2, width, height, 0.6f + getPercentage(40), 0.6f + getPercentage(40), angle);
		} else if (s != null && s.length() > 0)
			Rubico.menuFont.draw(batch, s, x, y);
		click();
		drawSubElements(batch);
		for (Barre b : barres)
			b.draw(batch);
	}

	protected void drawSubElements(SpriteBatch batch) {
		for (UiElements uie : subElements)
			uie.draw(batch);
	}

	protected void click() {
		if (clickable && Gdx.input.justTouched() && Physic.isPointInRect(Physic.getXClic(), Physic.getYClic(), x, y, width, height)) {
			if (onClick != null)
				onClick.onClick();
		}
	}

	protected void place() {
		place += balance;
		if (place > PrecalculatedParticles.HALF_LENGTH)		balance -= 0.002f;
		else												balance += 0.002f;
		if (place < MIN)									place = MIN;
		else if (place >= MAX)								place = MAX;
	}

	protected float getPercentage(float f) {
		return ((MAX - MIN) / place) / f;
	}

	public void noClick(boolean state) {
		clickable = state;
	}

	public void add(UiElements updatePaddle) {
		subElements.add(updatePaddle);
	}

	public void remove() {
		
	}

}
