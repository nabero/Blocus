package menu.screens;

import jeu.Rubico;
import menu.OnClick;
import menu.ui.Button;
import assets.SoundMan;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MenuOptions extends AbstractScreen {

	private static final int LINE2 = 4;
	private static final int LINE3 = 11;
	private static final int LINE4 = 18;

	public MenuOptions(final Game game) {
		super(game);
		
		Gdx.input.setCatchBackKey(true);
		ajout(buttonBack);

		// ************************ B R U I T A G E S ********************************************************
		final Button bruit = new Button(BRUITAGE_VOL + (int) (Rubico.profile.effectsVolume * 10), Rubico.menuFontSmall, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT, (Rubico.screenWidth / 2) - Menu.SMALL_BUTTON_WIDTH / 2, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE2, new OnClick() {
			public void onClick() {}
		});
		ajout(bruit);
		ajout(new Button(MOINS, Rubico.menuFont, MINI_BOUTON_WIDTH, MINI_BOUTON_HEIGHT, Rubico.screenWidth / Menu.PADDING, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE2 + Menu.MINI_BOUTON_HEIGHT / 2, new OnClick() {
			public void onClick() {
				Rubico.profile.diminuerVolumeBruitage();
				bruit.setTexte(BRUITAGE_VOL + (int) (Rubico.profile.effectsVolume * 10));
				SoundMan.playBruitage(SoundMan.shotRocket);
			}
		}));
		ajout(new Button(PLUS, Rubico.menuFont, MINI_BOUTON_WIDTH, MINI_BOUTON_HEIGHT, Rubico.screenWidth - (Rubico.screenWidth / Menu.PADDING) - Menu.MINI_BOUTON_WIDTH, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE2 + Menu.MINI_BOUTON_HEIGHT / 2, new OnClick() {
			public void onClick() {
				Rubico.profile.augmenterVolumeBruitage();
				bruit.setTexte(BRUITAGE_VOL + (int) (Rubico.profile.effectsVolume * 10));
				SoundMan.playBruitage(SoundMan.shotRocket);
			}
		}));
		// ************************ M U S I Q U E S ************************************************************
		final Button musique = new Button(MUSIQUE_VOL + (int) (Rubico.profile.musicVolume * 10), Rubico.menuFontSmall, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT, (Rubico.screenWidth / 2) - Menu.SMALL_BUTTON_WIDTH / 2, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE3, new OnClick() {
			public void onClick() {
			}
		});
		ajout(musique);
		ajout(new Button(MOINS, Rubico.menuFont, MINI_BOUTON_WIDTH, MINI_BOUTON_HEIGHT, Rubico.screenWidth / Menu.PADDING, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE3 + Menu.MINI_BOUTON_HEIGHT / 2, new OnClick() {
			public void onClick() {
				Rubico.profile.diminuerVolumeMusique();
				musique.setTexte(MUSIQUE_VOL + (int) (Rubico.profile.musicVolume * 10));
				SoundMan.playMusic();
				if (Rubico.profile.musicVolume < 0.1f) {
					SoundMan.stopMusic();
				}
			}
		}));
		ajout(new Button(PLUS, Rubico.menuFont, MINI_BOUTON_WIDTH, MINI_BOUTON_HEIGHT, Rubico.screenWidth - (Rubico.screenWidth / Menu.PADDING) - Menu.MINI_BOUTON_WIDTH, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE3 + Menu.MINI_BOUTON_HEIGHT / 2, new OnClick() {
			public void onClick() {
				Rubico.profile.augmenterVolumeMusique();
				musique.setTexte(MUSIQUE_VOL + (int) (Rubico.profile.musicVolume * 10));
				SoundMan.playMusic();
			}
		}));

		
		String screenshaketxt = "Screenshake : on";
		if (Rubico.profile.screenshake == false)
			screenshaketxt = "Screenshake : off";
		final Button screenshake = new Button(screenshaketxt, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, (Rubico.screenWidth / 2) - Menu.BUTTON_WIDTH / 2, -Menu.yOffset + Rubico.screenHeight - Menu.BUTTON_HEIGHT * LINE4);
		screenshake.setClick(new OnClick() {
			@Override
			public void onClick() {
				Rubico.profile.screenshake = !Rubico.profile.screenshake;
				if (Rubico.profile.screenshake)
					screenshake.setTexte("Screenshake : on");
				else 
					screenshake.setTexte("Screenshake : off");
			}
		});
		ajout(screenshake);
	}

}
