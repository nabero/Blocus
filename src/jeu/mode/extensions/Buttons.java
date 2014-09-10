package jeu.mode.extensions;

import jeu.Physic;
import jeu.Rubico;
import jeu.Strings;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.screens.Menu;
import menu.ui.Button;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import elements.generic.Paddle;

public class Buttons {

	private static Button boutonBack = null, boutonUpgrade = null, boutonTwitter = null, boutonRestart = null;
	
	public static void init() {
		boutonUpgrade = null;
		boutonTwitter = null;
		boutonRestart = null;
		boutonBack = null;
	}
	
	public static void backButton(SpriteBatch batch, Game game) {
		if (boutonBack == null) {
			boutonBack = new Button(Strings.BACK, Rubico.menuFont, 
				(int) Rubico.menuFont.getBounds(Strings.BACK).width, 
				(int) Rubico.menuFont.getBounds(Strings.BACK).height, 
				(int) ((EndlessMode.cam.position.x) - Rubico.menuFont.getBounds(Strings.BACK).width / 2),
				Menu.BUTTON_HEIGHT * 6,
				new OnClick() {					public void onClick() {					}			});
		}
		if (Gdx.input.justTouched() && boutonBack != null && Physic.isPointInRect(Gdx.input.getX() + EndlessMode.cam.position.x / 2, Rubico.screenHeight - Gdx.input.getY(), 0, Menu.BUTTON_HEIGHT * 3f, Rubico.screenWidth, Menu.BUTTON_HEIGHT * 6)) {
			goToMenu(game);
			boutonBack = null;
		}
		if (boutonBack != null)
			boutonBack.draw(batch);
	}
	
	public static void initAndDrawButtons(SpriteBatch batch) {
		if (boutonRestart == null)	firstTime();
		else						boutonRestart.draw(batch);
		
		if (Rubico.profile.getCoutUpArme(Paddle.lvl) <= Rubico.profile.xpDispo) {
			if (boutonUpgrade == null) {
				boutonUpgrade = new Button(Strings.UPGRADE_BUTTON, Rubico.menuFont,
						(int) Rubico.menuFont.getBounds(Strings.UPGRADE_BUTTON).width,
						(int) Rubico.menuFont.getBounds(Strings.UPGRADE_BUTTON).height,
						(int) ((EndlessMode.cam.position.x) - Rubico.menuFont.getBounds(Strings.UPGRADE_BUTTON).width / 2),
						(int) (Menu.BUTTON_HEIGHT * 7f),
						new OnClick() {
					public void onClick() {
						if (Rubico.profile.getCoutUpArme(Paddle.lvl) <= Rubico.profile.xpDispo) {
							Rubico.profile.upPaddle();
							Rubico.profilManager.persist();
							EndlessMode.init();
						} else 
							Rubico.talkToTheWorld.buyXp();
					}
				});
			}
		} else {
			if (boutonUpgrade == null) {
				boutonUpgrade = new Button(Strings.BUY_XP_BUTTON, Rubico.menuFont, 
						(int) Rubico.menuFont.getBounds(Strings.BUY_XP_BUTTON).width, 
						(int) Rubico.menuFont.getBounds(Strings.BUY_XP_BUTTON).height, 
						(int) ((EndlessMode.cam.position.x) - Rubico.menuFont.getBounds(Strings.BUY_XP_BUTTON).width / 2),
						(int) (Menu.BUTTON_HEIGHT * 7f),
						new OnClick() {
					public void onClick() {
						if (Rubico.profile.getCoutUpArme(Paddle.lvl) <= Rubico.profile.xpDispo) {
							Rubico.profile.upPaddle();
							Rubico.profilManager.persist();
							EndlessMode.init();
						} else
							Rubico.talkToTheWorld.buyXp();
					}
				});
			}
		}
		if (boutonUpgrade != null) 
				boutonUpgrade.draw(batch);
		// won
	}
	
	private static void firstTime() {
		boutonRestart = new Button(Strings.RESTART_BUTTON, Rubico.menuFont, 
				(int) Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).width, 
				(int) Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).height, 
				(int) ((EndlessMode.cam.position.x) - Rubico.menuFont.getBounds(Strings.RESTART_BUTTON).width / 2),
				(int) (Menu.BUTTON_HEIGHT * 3.5f),
				new OnClick() {
				public void onClick() {
					EndlessMode.init();
				}
		});
	}
	
	public static void goToMenu(Game game) {
		Rubico.profilManager.persist();
		game.setScreen(new Menu(game));
	}

	public static void drawUpgradeAndTwitter(SpriteBatch batch) {
		if (boutonUpgrade != null) {
			boutonUpgrade.draw(batch);
			if (boutonTwitter != null)
				boutonTwitter.draw(batch);
		}
	}

	public static void testClick() {
		if (Gdx.input.justTouched()) {
			Button.testClick(boutonUpgrade, EndlessMode.cam.position.x / 2);
			Button.testClick(boutonTwitter, EndlessMode.cam.position.x / 2);
		}
	}

	public static void removeBack() {
		boutonBack = null;
	}

}