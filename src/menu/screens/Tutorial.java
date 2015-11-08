package menu.screens;

import menu.OnClick;
import menu.ui.TutoButton;
import jeu.Rubico;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import elements.particles.Particles;

public class Tutorial extends AbstractScreen {
	
	private final Texture step1, step2, step3;
	private static final float WIDTH = Rubico.screenWidth * 0.4f, HEIGHT = WIDTH, X = Rubico.halfWidth - (HEIGHT / 2), Y1 = (Rubico.screenHeight - HEIGHT) - Rubico.screenHeight * 0.05f, Y2 = (Rubico.screenHeight - HEIGHT * 2) - Rubico.screenHeight * 0.05f * 2, Y3 = (Rubico.screenHeight - HEIGHT * 3) - Rubico.screenHeight * 0.05f * 3;
	private final TutoButton button1, button2, button3;
	
	public Tutorial(final Game game) {
		super(game);
		EndlessMode.endless = true;
		Rubico.talkToTheWorld.showAds(false);
		step1 = new Texture(Gdx.files.internal("atlas/step1.png"));
		step2 = new Texture(Gdx.files.internal("atlas/step2.png"));
		step3 = new Texture(Gdx.files.internal("atlas/step3.png"));
		
		button1 = new TutoButton(WIDTH, HEIGHT, X, Y1, new OnClick() {
			public void onClick() {				game.setScreen(new TutoStep1(game));			}
		}, step1);
		button2 = new TutoButton(WIDTH, HEIGHT, X, Y2, new OnClick() {
			public void onClick() {				game.setScreen(new TutoStep2(game));			}
		}, step2);
		button3 = new TutoButton(WIDTH, HEIGHT, X, Y3, new OnClick() {
			public void onClick() {				game.setScreen(new TutoStep3(game));			}
		}, step3);
		ajout(buttonBack);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		Rubico.batch.begin();
		Rubico.begin(delta);
		Particles.background(Rubico.batch);
		button1.draw(Rubico.batch);
		button2.draw(Rubico.batch);
		button3.draw(Rubico.batch);
		buttonBack.draw(Rubico.batch);
		Rubico.end();
	}

	@Override	public void show() {						}
	@Override	public void hide() {						}
	@Override	public void pause() {						}
	@Override	public void resume() {						}
	@Override	public void dispose() {
		step1.dispose();
		step2.dispose();
		step3.dispose();
	}
	@Override	public void resize(int width, int height) {	}

}
