package menu.screens;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.Buttons;
import jeu.mode.extensions.FlickerText;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import elements.generic.Ball;
import elements.generic.Paddle;
import elements.generic.PaddleShot;
import elements.particles.Particles;

public class TutoStep1 extends AbstractScreen {
	
	private final Paddle paddle = new Paddle();
	private static final FlickerText MOVE_TT = new FlickerText("Catch the ball \nwith your paddle");
	private boolean ok = false, pause = false;

	public TutoStep1(Game game) {
		super(game);
		paddle.initialiser();
		FlickerText.clear();
		FlickerText.add(MOVE_TT);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		for (Ball b : Ball.BALLS)
			if (b.dir.y > 0) {
				ok = true;
				FlickerText.remove(MOVE_TT);
				FlickerText.congrats();
			}
		if (Ball.BALLS.size <= 0)					
			Ball.addBall(Rubico.screenWidth * Rubico.R.nextFloat(), Rubico.screenHeight * 0.8f, Rubico.R.nextFloat() - 0.5f, -0.5f);
		
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
