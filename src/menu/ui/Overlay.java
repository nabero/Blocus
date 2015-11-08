package menu.ui;

import menu.OnClick;
import menu.screens.Choice;
import menu.screens.Menu;
import menu.ui.diamond.DiamondDisplayer;
import menu.ui.diamond.DiamondDisplayerTemporary;
import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import elements.particles.individual.PrecalculatedParticles;

public class Overlay {
	
	private static Overlay paddle, restart, shopOverlay;
	private static Array<Overlay> overlays = new Array<Overlay>(); 
	
	private float x, y, width, height;
	private Array<UiElements> ui = new Array<UiElements>();
	private String id;
	

	public void addPaddleUpgrade() {
		if (paddle == null) {
			paddle = new Overlay();
			paddle.x = Rubico.screenWidth * 0.2f;
			paddle.width = Rubico.screenWidth * 0.6f;
			paddle.y = Rubico.screenHeight * 0.25f;
			paddle.height = Rubico.screenHeight * 0.5f;
			paddle.id = "paddle";
			UiElements arrow = new UiElements();
			arrow.setTexture(AssetMan.arrow);
			arrow.width = paddle.width * 0.5f;
			arrow.x = (paddle.x + (paddle.width / 2)) - arrow.width / 2; 
		}
		add(paddle);
	}

	public static void add(Overlay overlay) {
		if (!overlays.contains(overlay, true))
			overlays.add(overlay);
	}

	public static void act(SpriteBatch batch) {
		for (int i = 0; i < overlays.size; i++)
			overlays.get(i).draw(batch);
	}

	private void draw(SpriteBatch batch) {
		for (int i = 0; i < ui.size; i++)
			ui.get(i).draw(batch);
	}

	public static void gameFinished() {
		reset();
		addRestart();
	}

	private static void addRestart() {
		restart = new Overlay();
		restart.x = Rubico.screenWidth * 0.2f;
		restart.width = Rubico.screenWidth * 0.6f;
		restart.y = Rubico.screenHeight * 0.25f;
		restart.height = Rubico.screenHeight * 0.5f;
		restart.id = "paddle";
		
		UiElements backArrow = new UiElements();
		backArrow.setTexture(AssetMan.arrow);
		backArrow.width = restart.width * 0.2f;
		backArrow.x = (restart.x + (restart.width / 2)) - backArrow.width / 2;
		backArrow.x -= 4; 
		backArrow.y = restart.y;
		backArrow.angle = 90;
		backArrow.height = backArrow.width;
		backArrow.onClick = new OnClick() {
			public void onClick() {
				overlays.clear();
				Rubico.profilManager.persist();
				EndlessMode.getGame().setScreen(new Menu(EndlessMode.getGame()));
			}
		};
		UiElements play = new UiElements();
		play.width = restart.width * 0.2f;
		play.x = (restart.x + (restart.width / 2)) - play.width / 2;
		play.x -= 0; 
		play.y = restart.y;
		play.height = play.width;
		play.setTexture(AssetMan.play);
		play.onClick = new OnClick() {
			public void onClick() {
				if (EndlessMode.difficulty < Rubico.profile.lvl)
					EndlessMode.difficulty++;
				EndlessMode.init();
				overlays.removeValue(restart, true);
			}
		};
		
		DiamondDisplayer diamondDisplayer = new DiamondDisplayer();
		diamondDisplayer.width = play.width;
		diamondDisplayer.height = diamondDisplayer.width;
		diamondDisplayer.x = (Rubico.screenWidth - diamondDisplayer.width * 1.5f) - 0.5f;
		diamondDisplayer.y = (diamondDisplayer.height * 1f) + 0.5f;
		
		UiElements shopButton = shopButton(((restart.x + (restart.width / 2)) - play.width / 2) + 4, restart.y); 
		restart.ui.addAll(
				play, 
				shopButton, 
				backArrow,
				diamondDisplayer);
		
		backArrow.setBarres();
		play.setBarres();
		shopButton.setBarres();
		add(restart);
	}

	private static UiElements shopButton(float x, float y) {
		UiElements shop = new UiElements();
		shop.setTexture(AssetMan.up);
		shop.width = restart.width * 0.2f;
		shop.x = x;
		shop.y = y;
		shop.height = shop.width;
		shop.onClick = new OnClick() {
			public void onClick() {
				changeStateAll(false);
				addShop();
			}
		};
		return shop;
	}

	protected static void changeStateAll(boolean state) {
		for (Overlay o : overlays)
			o.noClick(state);
	}

	private void noClick(boolean state) {
		for (UiElements u : ui)
			u.noClick(state);
	}

	protected static void addShop() {
		shopOverlay = new Overlay();
		shopOverlay.x = 1;
		shopOverlay.y = 1;
		shopOverlay.width = Rubico.screenWidth - 2;
		shopOverlay.height = Rubico.screenHeight - 4;
		
		Background darkBackground = new Background();
		darkBackground.tr = AssetMan.debris;
		darkBackground.width = shopOverlay.width;
		darkBackground.height = shopOverlay.height;
		darkBackground.x = shopOverlay.x;
		darkBackground.y = shopOverlay.y;
		
		UiElements close = new UiElements();
		close.width = 1;
		close.height = 1;
		close.angle = 45;
		close.x = shopOverlay.x + shopOverlay.width - 2;
		close.y = shopOverlay.y + shopOverlay.height - 2;
		close.tr = AssetMan.cross;
		close.onClick = new OnClick() {
			@Override
			public void onClick() {
				remove(shopOverlay);
				changeStateAll(true);
			}
		};
		close.setBarres();
		
		Background updateBackground = new Background();
		updateBackground.tr = AssetMan.debris;
		updateBackground.width = shopOverlay.width - 2;
		updateBackground.height = shopOverlay.height - 9;
		updateBackground.x = shopOverlay.x + 1;
		updateBackground.y = shopOverlay.y + 5.5f;
		
		
		final PreviewPaddleUpgrade previewPaddleUpgrade = new PreviewPaddleUpgrade();
		final UiElements updatePaddleButton = new UiElements();
		updatePaddleButton.width = 3.5f;
		updatePaddleButton.height = 3.5f;
		updatePaddleButton.x = (shopOverlay.x + shopOverlay.width / 2) - updatePaddleButton.width / 2 ;
		updatePaddleButton.y = shopOverlay.y + 0.75f;
		updatePaddleButton.tr = AssetMan.cross;
		updatePaddleButton.onClick = new OnClick() {
			@Override
			public void onClick() {
				if (Gdx.input.justTouched()) {
					if (Rubico.profile.canUpdatePaddle()) {
						Rubico.profile.upPaddle();
						previewPaddleUpgrade.reset();
					} else {
						Rubico.talkToTheWorld.buyXp();
					}
				}
			}
			
		};
		updatePaddleButton.setBarres();
		
		previewPaddleUpgrade.x = updateBackground.x;
		previewPaddleUpgrade.y = updateBackground.y;
		previewPaddleUpgrade.width = updateBackground.width;
		previewPaddleUpgrade.height = updateBackground.height;
		previewPaddleUpgrade.add(updatePaddleButton);
		
		DiamondDisplayer diamondDisplayer = new DiamondDisplayer();
		diamondDisplayer.width = 2;
		diamondDisplayer.height = diamondDisplayer.width;
		diamondDisplayer.x = (Rubico.screenWidth - diamondDisplayer.width * 1.5f) - 0.5f;
		diamondDisplayer.y = (diamondDisplayer.height * 1f) + 0.5f;
		
		shopOverlay.ui.addAll(darkBackground, close, updateBackground, previewPaddleUpgrade, diamondDisplayer);
		
		overlays.add(shopOverlay);
	}

	protected static void remove(Overlay shopOverlay) {
		overlays.removeValue(shopOverlay, true);
		for (UiElements uie : shopOverlay.ui)
			uie.remove();
	}

	
	public static void addTemporaryDiamond() {
		DiamondDisplayerTemporary diamondDisplayer = new DiamondDisplayerTemporary();
		diamondDisplayer.width = 2;
		diamondDisplayer.height = diamondDisplayer.width;
		diamondDisplayer.x = (Rubico.screenWidth - diamondDisplayer.width * 1f) - 0.5f;
		diamondDisplayer.y = (diamondDisplayer.height * 1f);
		Overlay o = new Overlay();
		o.ui.add(diamondDisplayer);
		overlays.add(o);
	}

	public static void remove(UiElements diamondDisplayerTemporary) {
		for (Overlay o : overlays) { 
			o.ui.removeValue(diamondDisplayerTemporary, true);
			if (o.ui.size == 0)
				overlays.removeValue(o, true);
		}
	}

	public static void reset() {
		overlays.clear();
	}

}
