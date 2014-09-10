package menu.screens;

import jeu.Rubico;
import jeu.Strings;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.ui.Button;
import assets.SoundMan;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

import elements.generic.blocs.Bloc;
import elements.particles.Particles;
import elements.particles.individual.explosions.ExplodingBloc;

public class Menu extends AbstractScreen {

	public final static Array<Bloc> BLOCS = new Array<Bloc>();
	private float temps = 0, nextPop;
	private int etapeCode = 0, cptBloc;
	private Button highscores, achievements;
	private static final float ECART = 0.85f, LIGNE_PLAY = 3.8f, LIGNE_TUTO = LIGNE_PLAY + ECART, LIGNE_OPTION = LIGNE_TUTO + ECART, LIGNE_HIGHSCORE = LIGNE_OPTION + ECART, LIGNE_ACHIEVEMENT = LIGNE_HIGHSCORE + ECART, LIGNE_EXIT = LIGNE_ACHIEVEMENT + ECART * 1.3f;

	public Menu(Game game) {
		super(game);
		Particles.initBackground();
		setUpScreenElements();
		Particles.clear();
		blocus();
		cptBloc = 0;
	}

	private static final float
		X0 = 0, //Rubico.screenWidth * 0.05f,
		X1 = X0 + Bloc.WIDTH,
		X2 = X1 + Bloc.HALF_WIDTH,
		X3 = X2 + Bloc.WIDTH,
		X4 = X3 + Bloc.HALF_WIDTH,
		X5 = X4 + Bloc.HALF_WIDTH,
		X6 = X5 + Bloc.WIDTH * 0.75f,
		X7 = X6 + Bloc.WIDTH * 0.75f,
		X8 = X7 + Bloc.WIDTH,
		X9 = X8 + Bloc.HALF_WIDTH,
		X10 = X9 + Bloc.WIDTH,
		X11 = X10 + Bloc.WIDTH * 0.75f,
		X12 = X11 + Bloc.WIDTH * 0.75f,
		X125 = X12 + Bloc.WIDTH * 0.5f,
		X13 = X12 + Bloc.WIDTH,
		X14 = X13 + Bloc.HALF_WIDTH,
		X15 = X14 + Bloc.HALF_WIDTH,
		X16 = X15 + Bloc.HALF_WIDTH
			; 
	private static final int MIN_Y = 3;
	
	private void blocus() {
		Bloc.freeAll(BLOCS);
		BLOCS.clear();
		int lvl = 1;
		// BBBBBBBBBBBBBBBBBBBBB
		// Barre B
		for (int i = 0; i < 6; i++)
			BLOCS.add(Bloc.addBlock(X0, MIN_Y + i, lvl));
		
		BLOCS.add(Bloc.addBlock(X1, MIN_Y + 0, lvl));
		BLOCS.add(Bloc.addBlock(X1, MIN_Y + 2, lvl));
		BLOCS.add(Bloc.addBlock(X1, MIN_Y + 5, lvl));
		
		BLOCS.add(Bloc.addBlock(X2, MIN_Y + 1, lvl));
		BLOCS.add(Bloc.addBlock(X2, MIN_Y + 3, lvl));
		BLOCS.add(Bloc.addBlock(X2, MIN_Y + 4, lvl));
		
		// LLLLLLLLLLLLLLLLLLLLLLLLLLL
		// barre L
		lvl++;
		for (int i = 0; i < 5; i++)
			BLOCS.add(Bloc.addBlock(X3, MIN_Y + i, lvl));
		BLOCS.add(Bloc.addBlock(X4 + Bloc.WIDTH * 0.25f, MIN_Y + 5, lvl));
		
		// OOOOOOOOOOOOOOOOOOO
		lvl++;
		for (int i = 1; i < 5; i++) {
			BLOCS.add(Bloc.addBlock(X5, MIN_Y + i, lvl));
			BLOCS.add(Bloc.addBlock(X7, MIN_Y + i, lvl));
		}
		BLOCS.add(Bloc.addBlock(X6, MIN_Y + 0, lvl));
		BLOCS.add(Bloc.addBlock(X6, MIN_Y + 5, lvl));
		
		// CCCCCCCCCCCCCCCCCCCCCCCCCCCCC
		lvl++;
		for (int i = 1; i < 5; i++) {
			BLOCS.add(Bloc.addBlock(X8, MIN_Y + i, lvl));
		}
		BLOCS.add(Bloc.addBlock(X9, MIN_Y + 0, lvl));
		BLOCS.add(Bloc.addBlock(X9, MIN_Y + 5, lvl));
		
		// UUUUUUUUUUUUUUUUU
		lvl++;
		for (int i = 0; i < 5; i++) {
			BLOCS.add(Bloc.addBlock(X10, MIN_Y + i, lvl));
			BLOCS.add(Bloc.addBlock(X12, MIN_Y + i, lvl));
		}
		BLOCS.add(Bloc.addBlock(X11, MIN_Y + 5, lvl));

		// SSSSSSSSSSSSSSSSSSSSS
		lvl++;
		BLOCS.add(Bloc.addBlock(X14, MIN_Y + 0, lvl));
		BLOCS.add(Bloc.addBlock(X16, MIN_Y + 0, lvl));
		
		BLOCS.add(Bloc.addBlock(X13, MIN_Y + 1, lvl));
		
		BLOCS.add(Bloc.addBlock(X14, MIN_Y + 2, lvl));
		BLOCS.add(Bloc.addBlock(X15, MIN_Y + 3, lvl));
		BLOCS.add(Bloc.addBlock(X15, MIN_Y + 4, lvl));
		
		BLOCS.add(Bloc.addBlock(X125, MIN_Y + 5, lvl));
		BLOCS.add(Bloc.addBlock(X14, MIN_Y + 5, lvl));
		
		lvl++;
		for (int i = 0; i < 14; i++) {
			BLOCS.add(Bloc.addBlock(i * Bloc.WIDTH, MIN_Y + 6, lvl));
		}
	}

	public void setUpScreenElements() {
		temps = 0;
		Gdx.input.setCatchBackKey(true);

		ajout(new Button(PLAY, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_PLAY)), new OnClick() {
			public void onClick() {				changeMenu(new Choice(game));			}
		}));
		ajout(new Button("Tutorial", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_TUTO)), new OnClick() {
			public void onClick() {				changeMenu(new Tutorial(game));									}
		}));
		ajout(new Button(OPTION, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_OPTION)), new OnClick() {
			public void onClick() {				changeMenu(new MenuOptions(game));								}
		}));
		highscores = new Button(HIGHSCORE, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_HIGHSCORE)));
		ajout(highscores);
		
		achievements = new Button(ACHIEVEMENT, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_ACHIEVEMENT)));
		ajout(achievements);

		ajout(new Button(EXIT, Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_EXIT)), new OnClick() {
			public void onClick() {				Gdx.app.exit();													}
		}));
		
		ajout(new Button(Strings.TWITTER, Rubico.menuFontSmall, MINI_BOUTON_WIDTH, MINI_BOUTON_HEIGHT / 2, (int) (Rubico.screenWidth - ((Rubico.menuFontSmall.getBounds(Strings.TWITTER).width * 2)) - PADDING * 3),  (int) (Rubico.screenHeight - (Rubico.heightDiv10 * (LIGNE_EXIT + ECART * 0.8f ))), new OnClick() {
			public void onClick() {				Rubico.talkToTheWorld.followTwitter();						}
		}));
		Bloc.clear();
		if (Gdx.app.getVersion() != 0)
			Rubico.talkToTheWorld.showAds(true);
	}

	@Override
	public void render(float delta) {
		cam.update();
		Rubico.batch.setProjectionMatrix(cam.combined);
		if (Gdx.input.isTouched()
				&& Rubico.screenHeight - Gdx.input.getY() > highscores.sprite.getY() 
				&& Rubico.screenHeight - Gdx.input.getY() < highscores.sprite.getY() + highscores.sprite.getHeight()) {
			
				Rubico.talkToTheWorld.getScores();
		}
		if (Gdx.input.isTouched() && Rubico.screenHeight - Gdx.input.getY() > achievements.sprite.getY() && Rubico.screenHeight - Gdx.input.getY() < achievements.sprite.getY() + achievements.sprite.getHeight()) {
			Rubico.talkToTheWorld.getAchievements();
		}
		temps += delta;
		etapeCode = detectiopnKonamiCode(etapeCode);
		if (etapeCode == 8) {
			SoundMan.playBruitage(SoundMan.bigExplosion);
			Rubico.profilManager.persist();
			etapeCode++;
			Rubico.alternateGraphics = true;
		}
		
		super.render(delta);
		
		if (temps > nextPop) {
			cptBloc++;
			nextPop = temps + 0.07f;
		}
		Rubico.batch.begin();
		EndlessMode.delta = delta;
		EndlessMode.delta4 = delta * 4;
		EndlessMode.delta15 = delta * 15;
		int i = 0;
		for (Bloc b : BLOCS) {
			if (++i > cptBloc)
				break;
			b.display(Rubico.batch);
		}
		Particles.draw(Rubico.batch);
		ExplodingBloc.draw(Rubico.batch, Bloc.EXPLOSIONS);
		Rubico.batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.BACK) && temps > 2)
			Gdx.app.exit();
	}

}
