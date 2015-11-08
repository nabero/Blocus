package menu.screens;

import jeu.Profil;
import jeu.Rubico;
import jeu.Strings;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.ui.Button;
import assets.AssetMan;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import elements.generic.blocs.Bloc;
import elements.generic.paddles.AbstractPaddle;
import elements.generic.paddles.ShipPaddle;
import elements.generic.paddles.SpreadPaddle;
import elements.generic.paddles.StraightPaddle;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.ExplodingBloc;

public class Choice extends AbstractScreen {

	private SpriteBatch batch = Rubico.batch;
	private boolean surprise = false;
	private float[] colors;
	protected int index1, index2, index3;
	public final static Array<Bloc> BLOCS = new Array<Bloc>();
	private static final float 
	ECART = 0.70f,
	LINE_CHOOSE_MODE = 1.7f,
	LIGNE_ENDLESS = LINE_CHOOSE_MODE + ECART * 1.1f,
	LIGNE_LEVEL = LIGNE_ENDLESS + ECART,
	LINE_CHOOSE_PADDLE = LIGNE_LEVEL + ECART * 1.6f,
	LINE_PADDLES = LINE_CHOOSE_PADDLE + ECART * 3.5f;

	public Choice(Game game) {
		super(game);
		Particles.initBackground();
		setUpScreenElements();
		Particles.clear();
	}
	public void setUpScreenElements() {
		Gdx.input.setCatchBackKey(true);

		ajout(new Button(" Choose your mode : ", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_CHOOSE_MODE), new OnClick() {
			public void onClick() {				changeMenu(new EndlessMode(game, Rubico.batch, 1, true, surprise));			}
		}));
		ajout(new Button(" Endless ", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_ENDLESS), new OnClick() {
			public void onClick() {				changeMenu(new EndlessMode(game, Rubico.batch, 1, true, surprise));			}
		}));
		ajout(new Button(" Levels ", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_LEVEL), new OnClick() {
			public void onClick() {				changeMenu(new LevelChooser(game, surprise));			}
		}));
		
		ajout(new Button(" Choose your paddle : ", Rubico.menuFont, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_CHOOSE_PADDLE), new OnClick() {
			public void onClick() {				changeMenu(new EndlessMode(game, Rubico.batch, 1, true, surprise));			}
		}));
		
		
		ajout(new Button(Rubico.screenWidth * 0.4f, Rubico.screenHeight * 0.18f, Rubico.screenWidth * 0.05f, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES), new OnClick() {
			public void onClick() {				Rubico.profile.setSelectedPaddle(StraightPaddle.ID);			}
		}));
		ajout(new Button(Rubico.screenWidth * 0.4f, Rubico.screenHeight * 0.18f, Rubico.screenWidth * 0.55f, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES), new OnClick() {
			public void onClick() {				Rubico.profile.setSelectedPaddle(SpreadPaddle.ID);			}
		}));
		ajout(new Button(Rubico.screenWidth * 0.4f, Rubico.screenHeight * 0.18f, Rubico.screenWidth * 0.29f, (Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES)) - (Rubico.screenHeight*0.24f), new OnClick() {
			public void onClick() {
				if (Rubico.profile.isShipUnlocked())
					Rubico.profile.setSelectedPaddle(ShipPaddle.ID);
				else
					blink = 1;
			}
		}));
		
		ajout(new Button("Be the hero!", Rubico.menuFontSmall, BUTTON_WIDTH, BUTTON_HEIGHT, Rubico.screenWidth / PADDING, Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_LEVEL) - ECART * 2.5f, new OnClick() {
			public void onClick() {				surprise = !surprise;			}
		}));
		
//		ajout(buttonBack);
	}
	
	static float blink = 0;

	@Override
	public void render(float delta) {
		if (blink > 0)
			blink -= delta;
		else
			blink = 0;
		cam.update();
		Rubico.batch.setProjectionMatrix(cam.combined);
		super.render(delta);
		Rubico.batch.begin();
		EndlessMode.delta = delta;
		EndlessMode.delta4 = delta * 4;
		EndlessMode.delta15 = delta * 15;
		Particles.draw(Rubico.batch);
		ExplodingBloc.draw(Rubico.batch, Bloc.EXPLOSIONS);
		colors = PrecalculatedParticles.getColors(Rubico.profile.lvlPaddle);
		
		batch.setColor(0, 1, 0, 0.15f);
		if (Rubico.profile.selectedPaddle == StraightPaddle.ID) 
			batch.draw(AssetMan.debris, Rubico.screenWidth * 0.02f, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES) - 0.4f, Rubico.screenWidth * 0.46f, Rubico.screenHeight * 0.205f);
		else if (Rubico.profile.selectedPaddle == SpreadPaddle.ID) {
			batch.draw(AssetMan.debris, Rubico.screenWidth * 0.52f, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES) - 0.4f, Rubico.screenWidth * 0.46f, Rubico.screenHeight * 0.205f);
		} else
			batch.draw(AssetMan.debris, Rubico.screenWidth * 0.27f, Rubico.screenHeight - (Rubico.heightDiv10 * LINE_PADDLES) - 8.0f, Rubico.screenWidth * 0.46f, Rubico.screenHeight * 0.205f);
		float paddleBottom = 0.33f;
		// left
		batch.setColor(colors[20]);
		batch.draw(AbstractPaddle.SIDE, 	2.4f, 									Rubico.screenHeight * paddleBottom, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
			// mid
		batch.setColor(colors[1]);
		batch.draw(AbstractPaddle.MID, 		2.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * paddleBottom, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		
		shotTrail(2.4f, paddleBottom + .06f);
		batch.setColor(colors[1]);
		batch.draw(AssetMan.partBloc, 		2.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * (paddleBottom + .06f), AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		shotTrail(2.4f, paddleBottom + .12f);
		batch.setColor(colors[1]);
		batch.draw(AssetMan.partBloc, 		2.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * (paddleBottom + .12f), AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		shotTrail(2.4f, paddleBottom + .18f);
		batch.setColor(colors[1]);
		batch.draw(AssetMan.partBloc, 		2.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * (paddleBottom + .18f), AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
			// right
		batch.setColor(colors[20]);
		batch.draw(AbstractPaddle.SIDE, 	2.4f + AbstractPaddle.WIDTH, 			Rubico.screenHeight * paddleBottom, -AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		
		
		
		colors = PrecalculatedParticles.getColors(Rubico.profile.lvlSpreadPaddle);
		// left
		batch.setColor(colors[20]);
		batch.draw(AbstractPaddle.SIDE, 	11.4f, 									Rubico.screenHeight * paddleBottom, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
			// mid
		batch.setColor(colors[1]);
		batch.draw(AbstractPaddle.MID, 		11.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * paddleBottom, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		
		shotTrail(11.4f, 					paddleBottom + .06f);
		batch.setColor(colors[1]);
		batch.draw(AssetMan.partBloc, 		11.4f + AbstractPaddle.TIER_WIDTH, 		Rubico.screenHeight * (paddleBottom + .06f), AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		
		shotTrail(11.4f, 					paddleBottom + .18f);
		shotTrail(11.4f - Bloc.WIDTH, 		paddleBottom + .165f);
		shotTrail(11.4f - Bloc.WIDTH * 2, 	paddleBottom + .15f);
		shotTrail(11.4f + Bloc.WIDTH, 		paddleBottom + .165f);
		shotTrail(11.4f + Bloc.WIDTH * 2, 	paddleBottom + .15f);
		batch.setColor(colors[1]);
		batch.draw(AssetMan.partBloc, 		11.4f + AbstractPaddle.TIER_WIDTH, 						Rubico.screenHeight * (paddleBottom + .18f), AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		batch.draw(AssetMan.partBloc, 		(11.4f + AbstractPaddle.TIER_WIDTH) - Bloc.WIDTH, 		Rubico.screenHeight * (paddleBottom + .18f) - 0.5f, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		batch.draw(AssetMan.partBloc, 		(11.4f + AbstractPaddle.TIER_WIDTH) - Bloc.WIDTH * 2, 	Rubico.screenHeight * (paddleBottom + .18f) - 1, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		batch.draw(AssetMan.partBloc, 		(11.4f + AbstractPaddle.TIER_WIDTH) + Bloc.WIDTH, 		Rubico.screenHeight * (paddleBottom + .18f) - 0.5f, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		batch.draw(AssetMan.partBloc, 		(11.4f + AbstractPaddle.TIER_WIDTH) + Bloc.WIDTH * 2, 	Rubico.screenHeight * (paddleBottom + .18f) - 1, AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
			// right
		batch.setColor(colors[20]);
		batch.draw(AbstractPaddle.SIDE, 	11.4f + AbstractPaddle.WIDTH, 			Rubico.screenHeight * paddleBottom, -AbstractPaddle.TIER_WIDTH, AbstractPaddle.HEIGHT);
		
		batch.setColor(1, 1, 1, 1);
		batch.draw(AssetMan.shipPaddle, 6.9f, paddleBottom + 1.2f, AbstractPaddle.WIDTH, AbstractPaddle.HEIGHT * 2.5f);
		
		
		colors = PrecalculatedParticles.getColors(Rubico.profile.lvlShipPaddle);
		
		float WIDTH = 0.3f, HALF_WIDTH = 0.15f, x = 7.5f, y = 3.5f;
		drawBullet(WIDTH, HALF_WIDTH, x + 0.05f, y);
		drawBullet(WIDTH, HALF_WIDTH, (x + 0.05f) - 1.44f, y + 1.44f);
		
		drawBullet(WIDTH, HALF_WIDTH, x + 3.3f, y);
		drawBullet(WIDTH, HALF_WIDTH, (x + 3.3f) + 1.44f, y + 1.44f);
		
		drawBullet(WIDTH, HALF_WIDTH, x + 1.68f, y + 0.2f);
		drawBullet(WIDTH, HALF_WIDTH, x + 1.68f, y + 2.2f);

		// connectors
		if (Rubico.profile.lvlPaddle >= Profil.LVL_TO_UNLOCK_SHIP) {
			batch.setColor(0, .75f, .95f, 1);
			batch.draw(AssetMan.debris, 1.45f, 7, .1f, 3f);
			batch.draw(AssetMan.debris, 1.45f, 7, 3f, .1f);
			batch.draw(AssetMan.debris, 1.45f, 5.7f, 0.5f, 0.5f, 1, 1, 0.1f, 1f, -30);
		} else {
			batch.setColor(.95f, .25f, .05f, 1);
			batch.draw(AssetMan.debris, 1.45f, 7, .1f + blink, 3f);
			batch.draw(AssetMan.debris, 1.45f, 7, 3f, .1f + blink);
			batch.draw(AssetMan.debris, 1.45f, 5.7f, 0.5f, 0.5f, 1, 1, 0.1f, 1f, -30);
		}
		if (Rubico.profile.lvlSpreadPaddle >= Profil.LVL_TO_UNLOCK_SHIP) {
			batch.setColor(0, .75f, .95f, 1);
			batch.draw(AssetMan.debris, 16.35f, 7, .1f, 3f);
			batch.draw(AssetMan.debris, 13.35f, 7, 3f, .1f);
			batch.draw(AssetMan.debris, 14.45f, 5.7f, 0.5f, 0.5f, 1, 1, 0.1f, 1f, -30);
		} else {
			batch.setColor(.95f, .25f, .05f, 1);
			batch.draw(AssetMan.debris, 16.35f, 7, .1f + blink, 3f);
			batch.draw(AssetMan.debris, 13.35f, 7, 3f, .1f + blink);
			batch.draw(AssetMan.debris, 14.45f, 5.7f, 0.5f, 0.5f, 1, 1, 0.1f, 1f, -30);
		}
		
		Rubico.menuFont.draw(batch, Strings.getNumber(Rubico.profile.lvlPaddle), 0.75f, 6.6f);
		Rubico.menuFont.draw(batch, "5", 2.25f, 6.4f);
		
		
		Rubico.menuFont.draw(batch, Strings.getNumber(Rubico.profile.lvlSpreadPaddle), 13.75f, 6.6f);
		Rubico.menuFont.draw(batch, "5", 15.25f, 6.4f);
		
		if (surprise) {
			batch.setColor(0, .75f, .25f, .25f);
			batch.draw(AssetMan.debris, 6.66f, (Rubico.screenHeight - (Rubico.heightDiv10 * LIGNE_LEVEL) - ECART * 2.5f) - .35f, 4.78f, 1.33f);
		}
		Rubico.batch.end();
	}
	private void drawBullet(float WIDTH, float HALF_WIDTH, float x, float y) {
		batch.setColor(colors[colors.length / 2 + Rubico.R.nextInt(colors.length / 2)]);
		batch.draw(AssetMan.dust, x - (WIDTH * 2), y - (WIDTH * 2), WIDTH * 4, WIDTH * 4);
		batch.setColor(colors[Rubico.R.nextInt(colors.length / 2)]);
		batch.draw(AssetMan.dust, x - WIDTH, y - WIDTH, WIDTH * 2, WIDTH * 2);
		batch.setColor(colors[0]);
		batch.draw(AssetMan.dust, x - HALF_WIDTH, y - HALF_WIDTH, WIDTH, WIDTH);
		batch.setColor(colors[Rubico.R.nextInt(colors.length/2)]);
		batch.draw(AssetMan.dust, x - HALF_WIDTH, y - HALF_WIDTH, WIDTH, WIDTH);
	}
	
	
	private void shotTrail(float x, float y) {
		for (float i = 8; i > 1; i -= 0.6f) {
			batch.setColor(colors[(int) (3 + i * 2)]);
			batch.draw(AssetMan.partBloc, 		
					(float) (x + AbstractPaddle.TIER_WIDTH + (AbstractPaddle.TIER_WIDTH - (AbstractPaddle.TIER_WIDTH / i)) / 2 + (Rubico.R.nextGaussian() / 8) / ((8 - i) * 2)),
					Rubico.screenHeight * y - i/10f,
					AbstractPaddle.TIER_WIDTH / i, AbstractPaddle.HEIGHT / 1);
		}
	}

}

