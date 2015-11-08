package jeu.mode.extensions;

import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.Strings;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.screens.LevelChooser;
import menu.screens.Menu;
import menu.ui.Button;
import menu.ui.Overlay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Buttons {

	private static Button boutonBack = null, boutonTwitter = null, boutonRestart = null;
	private static final int BACK_HEIGHT = 18;
	
	public static void init() {
		boutonTwitter = null;
		boutonRestart = null;
		boutonBack = null;
	}
	
	public static void backButton(SpriteBatch batch, Game game) {
		if (boutonBack == null) {
			boutonBack = new Button(Strings.BACK, Rubico.menuFont, 
				(int) Rubico.menuFont.getBounds(Strings.BACK).width, 
				(int) Rubico.menuFont.getBounds(Strings.BACK).height, 
				Rubico.menuFont.getBounds(Strings.BACK).width / 2,
				Menu.BUTTON_HEIGHT * BACK_HEIGHT,
				new OnClick() {					public void onClick() {					}			});
		} else {
			boutonBack.draw(batch);
			if (Gdx.input.justTouched()) {
				if (Physic.isPointInRect(Physic.getXClic(), Physic.getYClic(), boutonBack.sprite.getX() - Button.PADDING_BARRE, (Menu.BUTTON_HEIGHT * BACK_HEIGHT) - Menu.BUTTON_HEIGHT, boutonBack.sprite.getWidth() + Stats.UU, Menu.BUTTON_HEIGHT * 2)) {
					goToMenu(game);
					boutonBack = null;
				} else 
					EndlessMode.pause = false;
			}
		}
	}
	
	
	public static void initAndDrawButtons(SpriteBatch batch, final Game game) {
		if (boutonRestart == null)	firstTime(game);
		else						boutonRestart.draw(batch);
	}

	private static void firstTime(final Game game) {
		if (EndlessMode.endless) {
			boutonRestart = new Button(Strings.RESTART_BUTTON, Rubico.menuFont, 
					(int) Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).width, 
					(int) Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).height, 
					(int) ((EndlessMode.getCam().position.x) - Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).width / 2),
					(int) (Menu.BUTTON_HEIGHT * 3.5f),
					new OnClick() {
					public void onClick() {
						EndlessMode.init();
					}
			});
		} else {
			boutonRestart = new Button("Next level", Rubico.menuFont, 
					(int) Rubico.menuFont.getBounds("Next level").width, 
					(int) Rubico.menuFont.getBounds("Next level").height, 
					(int) ((EndlessMode.getCam().position.x) - Rubico.menuFont.getBounds("Next level").width / 2),
					(int) (Menu.BUTTON_HEIGHT * 3.5f),
					new OnClick() {
				public void onClick() {
					Rubico.profilManager.persist();
					game.setScreen(new LevelChooser(game, EndlessMode.surprise));
				}
			});
		}
	}
	
	public static void goToMenu(Game game) {
		EndlessMode.ship.clear();
		Rubico.profilManager.persist();
		game.setScreen(new Menu(game));
	}

	public static void drawUpgradeAndTwitter(SpriteBatch batch) {
		if (boutonTwitter != null)
			boutonTwitter.draw(batch);
	}

	public static void testClick() {
		if (Gdx.input.justTouched()) {
			Button.testClick(boutonTwitter, EndlessMode.getCam().position.x / 2);
		}
	}

	public static void removeBack() {
		boutonBack = null;
	}

}