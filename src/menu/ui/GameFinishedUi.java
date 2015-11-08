package menu.ui;

import menu.screens.Menu;
import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import assets.SoundMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elements.particles.individual.PrecalculatedParticles;

public class GameFinishedUi {
	
	protected final Array<Barre> barres = new Array<Barre>();
	protected float place, balance, xtxt = 10;
	private String txtUpgrade = "Upgrade paddle : " + Rubico.profile.getCoutUpArme();
	private static final float BOTTOM = 2;
	private final Rectangle
		BACK 		= new Rectangle(1, 							BOTTOM, 1.5f, 1.5f),
		DIAMONDS 	= new Rectangle(
				Rubico.screenWidth - (3.5f + Rubico.menuFontSmall.getBounds(Rubico.profile.txtDiamonds).width * 1.2f),
				BOTTOM,
				2.5f +  Rubico.menuFontSmall.getBounds(Rubico.profile.txtDiamonds).width * 1.2f,
				1.5f),
		ADS_FREE	= new Rectangle(Rubico.screenWidth * 0.30f, BOTTOM + 13, Rubico.screenWidth * 0.4f, 1.5f),
		PLAY 		= new Rectangle(Rubico.screenWidth * 0.35f, BOTTOM + 10, Rubico.screenWidth * 0.3f, 1.5f),
		UPGRADE		= new Rectangle(
				Rubico.halfWidth - (Rubico.menuFontSmall.getBounds(txtUpgrade).width + (1.5f + PADDING)) / 2,
				BOTTOM + 7, 
				Rubico.menuFontSmall.getBounds(txtUpgrade).width + (1.5f + PADDING), 
				1.5f);
	private static final float PADDING = 0.3f;
	private static final String TXT_UPGRADE = "Upgrades will make the projectiles more powerful !     Upgrades will make the projectiles more powerful !     Seriously, it's really helpful..      And thanks for playing ! I hope you're enjoying this game";
	
	public GameFinishedUi() {
		addBarres(BACK);
		addBarres(DIAMONDS);
		addBarres(PLAY);
		addBarres(UPGRADE);
		if (!Rubico.profile.isAdsFree())
			addBarres(ADS_FREE);
	}
	
	private void addBarres(Rectangle r) {
		verticalBarre(r.x - Barre.HALF_HEIGHT, 	r.y, 			r.height);
		verticalBarre(r.x + r.width, 			r.y, 			r.height);
		horizontalBarre(r.x, 					r.y, 			Math.abs(r.width));
		horizontalBarre(r.x, 					r.y + r.height, Math.abs(r.width) + Barre.HEIGHT);
	}

	public void draw(SpriteBatch batch) {
		place += balance;
		if (place > PrecalculatedParticles.HALF_LENGTH)		balance -= 0.002f;
		else												balance += 0.002f;
		if (place < 0)										place = 0;
		else if (place >= 30)								place = 30;
		
		for (Barre b : barres)
			b.draw(batch);
		
		batch.setColor(PrecalculatedParticles.uiColor[(int) place]);
		// BACK
		batch.draw(AssetMan.play,
				BACK.x + PADDING, BACK.y + PADDING,
				(BACK.width - PADDING * 2) / 2, (BACK.height - PADDING * 2) / 2,
				BACK.width - PADDING * 2, BACK.height - PADDING * 2,
				1f, 1f, 180);
		
		// DIAMONDS
		batch.draw(AssetMan.diamond,
				DIAMONDS.x + (DIAMONDS.width - DIAMONDS.height + PADDING), DIAMONDS.y + PADDING,
				(DIAMONDS.height - PADDING * 2) / 2, (DIAMONDS.height - PADDING * 2) / 2,
				DIAMONDS.height - PADDING * 2, DIAMONDS.height - PADDING * 2,
				1f, 1f, 0);
		Rubico.menuFontSmall.draw(batch, Rubico.profile.txtDiamonds, DIAMONDS.x + PADDING, DIAMONDS.y + Rubico.menuFontSmall.getBounds(Rubico.profile.txtDiamonds).height * 2);
		
		// NEXT
		batch.draw(AssetMan.play,
				PLAY.x + PLAY.width + PADDING - PLAY.height, PLAY.y + PADDING,
				PLAY.height - PADDING * 2, PLAY.height - PADDING * 2);
		Rubico.menuFont.draw(batch, "Next", PLAY.x + PADDING * 2, PLAY.y + Rubico.menuFont.getBounds("Next").height * 1.6f);
		
		// UPGRADE PADDLE
		batch.draw(AssetMan.diamond,
				UPGRADE.x + Rubico.menuFontSmall.getBounds(txtUpgrade).width + UPGRADE.height - PADDING * 2.5f, UPGRADE.y + PADDING,
				UPGRADE.height - PADDING * 2, UPGRADE.height - PADDING * 2);
		Rubico.menuFontSmall.draw(batch, txtUpgrade, UPGRADE.x + PADDING * 2, UPGRADE.y + Rubico.menuFontSmall.getBounds(txtUpgrade).height * 2f);

		// UPGRADE EXPLICATIONS
		xtxt -= Stats.UUU * EndlessMode.delta;
		Rubico.menuFontSmall.draw(batch, TXT_UPGRADE, xtxt, Rubico.menuFontSmall.getBounds(TXT_UPGRADE).height + 0.5f);
		
		// ADS FREE
		if (!Rubico.profile.isAdsFree())
			Rubico.menuFontSmall.draw(batch, "Remove ads", Rubico.halfWidth - Rubico.menuFontSmall.getBounds("Remove ads").width / 2, ADS_FREE.y + Rubico.menuFontSmall.getBounds(txtUpgrade).height * 2f);
		
		
		if (Gdx.input.justTouched()) {
			final float x = Physic.getXClic();
			final float y = Physic.getYClic();
			
			if (PLAY.contains(x, y)) {
				EndlessMode.ship.clear();
				if (EndlessMode.difficulty < Rubico.profile.lvl)
					EndlessMode.difficulty++;
				EndlessMode.init();
			} else if (BACK.contains(x, y)) {
				Rubico.profilManager.persist();
				EndlessMode.ship.clear();
				EndlessMode.getGame().setScreen(new Menu(EndlessMode.getGame()));	
			} else if (UPGRADE.contains(x, y)) {
				if (Rubico.profile.canUpdatePaddle()) {
					Rubico.profile.upPaddle();
					txtUpgrade = "Upgrade paddle : " + Rubico.profile.getCoutUpArme();
					Rubico.tmpPos.x = x;
					Rubico.tmpPos.y = y;
					for (Barre b : barres)
						b.impulse(Rubico.tmpPos);
					SoundMan.playBruitage(SoundMan.bigExplosion);
					EndlessMode.ship.clear();
					EndlessMode.ship.initialiser(Rubico.profile.getSelectedPaddleLvl());
				} else
					Rubico.talkToTheWorld.buyXp();
			} else if (DIAMONDS.contains(x, y)) {
				Rubico.talkToTheWorld.buyXp();
			} else if (ADS_FREE.contains(x, y)) {
				Rubico.talkToTheWorld.buyRemoveAds();
			}
		}
	}
	
	
	protected void verticalBarre(float x, float y, float heightToCover) {
		final int nbrBarre = (int) (((heightToCover * 0.8f) / Barre.HEIGHT));
		final float distanceCouverte = nbrBarre * Barre.HEIGHT;
		final float ecartTotal = heightToCover - distanceCouverte;
		final float ecart = ecartTotal / (nbrBarre-1);
		float tmpX = 0;
		for (int i = 0; i < nbrBarre; i++) {
			barres.add(Barre.POOL.obtain().init(x, y + tmpX));
			tmpX += (Barre.HEIGHT + ecart);
		}
	}
	
	protected void horizontalBarre(float x, float y, float widthToCover) {
		final int nbrBarre = (int) (((widthToCover * 0.7f) / Barre.HEIGHT));
		final float distanceCouverte = nbrBarre * Barre.HEIGHT;
		final float ecartTotal = widthToCover - distanceCouverte;
		final float ecart = ecartTotal / (nbrBarre-1);
		float tmpX = -Barre.HALF_HEIGHT;
		for (int i = 0; i < nbrBarre; i++) {
			barres.add(Barre.POOL.obtain().init(x + tmpX, y));
			tmpX += (Barre.HEIGHT + ecart);
		}
	}

}
