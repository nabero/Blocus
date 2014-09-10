package menu.screens;

import jeu.Rubico;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.Buttons;
import jeu.mode.extensions.FlickerText;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import elements.generic.Ball;
import elements.generic.Paddle;
import elements.generic.PaddleShot;
import elements.generic.blocs.Column;
import elements.particles.Particles;

public class TutoStep3 extends AbstractScreen {
	
	private final Paddle paddle = new Paddle();
	private static final FlickerText MOVE_TT = new FlickerText("Use a ball to \nwarm your paddle \nand make it shoot");
	private boolean ok = false, pause = false;

	public TutoStep3(Game game) {
		super(game);
		paddle.initialiser();
		FlickerText.clear();
		FlickerText.add(MOVE_TT);
		Builder.init();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
			
		if (Ball.BALLS.size <= 3)					
			Ball.addBall(Rubico.screenWidth * Rubico.R.nextFloat(), Rubico.screenHeight * 0.8f, Rubico.R.nextFloat() - 0.5f, -0.5f);
		
		if (PaddleShot.SHOTS.size > 0) {
			ok = true;
			FlickerText.remove(MOVE_TT);
			FlickerText.congrats();
		}
		for (Column c : Column.COLUMNS) {
			c.nextPop++;
		}
		if (pause)
			delta = 0;
		EndlessMode.delta = delta;
		EndlessMode.majDeltas();
		EndlessMode.now += EndlessMode.delta;
		EndlessMode.fps = Gdx.graphics.getFramesPerSecond();
		EndlessMode.alternate = !EndlessMode.alternate;
		Rubico.begin(Gdx.graphics.getDeltaTime());
		Rubico.batch.begin();
		Particles.background(Rubico.batch);
		FlickerText.draw(Rubico.batch);
		Column.draw(Rubico.batch);
		Ball.draw(Rubico.batch);
		Particles.draw(Rubico.batch);
		PaddleShot.act(Rubico.batch);
		paddle.draw(Rubico.batch);
		paddle.move();
		ScreenShake.act();
		if (ok && (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.justTouched()))
			game.setScreen(new Menu(game));
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			pause = true;
		}
		if (pause)
			Buttons.backButton(Rubico.batch, game);
		Rubico.end();
	}

}
