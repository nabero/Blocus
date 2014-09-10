package menu.screens;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.ui.Button;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import elements.generic.blocs.Bloc;
import elements.particles.Particles;
import elements.particles.individual.explosions.ExplodingBloc;

public class Choice extends AbstractScreen {

	public final static Array<Bloc> BLOCS = new Array<Bloc>();
	private static final float ECART = 0.85f, LIGNE_ENDLESS = 3.8f, LIGNE_LEVEL = LIGNE_ENDLESS + ECART;

	public Choice(Game game) {
		super(game);
		Particles.initBackground();
		setUpScreenElements();
		Particles.clear();
	}
	public void setUpScreenElements() {
		Gdx.input.setCatchBackKey(true);

		ajout(new Button("Endless", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_ENDLESS)), new OnClick() {
			public void onClick() {				changeMenu(new EndlessMode(game, Rubico.batch, 1, true));			}
		}));
		ajout(new Button("Levels", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, (int) (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_LEVEL)), new OnClick() {
			public void onClick() {				changeMenu(new EndlessMode(game, Rubico.batch, 1, false));			}
		}));
		ajout(buttonBack);
	}

	@Override
	public void render(float delta) {
		cam.update();
		Rubico.batch.setProjectionMatrix(cam.combined);
		super.render(delta);
		Rubico.batch.begin();
		EndlessMode.delta = delta;
		EndlessMode.delta4 = delta * 4;
		EndlessMode.delta15 = delta * 15;
		Particles.draw(Rubico.batch);
		ExplodingBloc.draw(Rubico.batch, Bloc.EXPLOSIONS);
		Rubico.batch.end();
		
	}

}
