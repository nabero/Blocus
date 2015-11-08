package jeu.mode.extensions;

import jeu.Rubico;
import jeu.CharSeq;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import elements.generic.paddles.StraightPaddle;
import elements.particles.individual.PrecalculatedParticles;

public class Score {

	private final static char[] mockStrScore = {'S', 'c', 'o', 'r', 'e', ' ', ':', ' ', '0','0','0','.','0','0','0'};
	public static CharSeq strScore = new CharSeq(mockStrScore);
	private static float rScore;
	private static final float MIN_SCALE = getMinScale();
	private static int scoreToProcess, currentDigit;
	public static int score = 0, lvl = 1;
	public static float[] colors = PrecalculatedParticles.getColors(lvl);
	private static final float FONT_HEIGHT = Rubico.heightDiv10/5;
	
	public static void init() {
		score = 0;
		updateStringScore();
		rScore = 0;
		lvl = 1;
		colors = PrecalculatedParticles.getColors(lvl);
	}
	
	private static float getMinScale() {
		float originalScoreFontScale = Rubico.screenHeight + Rubico.screenWidth;
		originalScoreFontScale /= 2000;
//		if (originalScoreFontScale < 1f)
//			originalScoreFontScale = 1f;
		return originalScoreFontScale;
	}

	public static void up(int score) {
		if (EndlessMode.lost)
			return;
		rScore += 0.1f;
		if (rScore > 1)
			rScore = 1;
		Score.score += score;
		updateScore();
	}

	protected static void updateScore() {
		updateFont();
		updateStringScore();
		lvl = (Score.score / 500) + 1;
		if (lvl > 7)
			lvl = 7;
		colors = PrecalculatedParticles.getColors(lvl);
	}

	private static void updateFont() {
//		Rubico.scoreFont.setColor(0.1f,  Math.max(rScore, 0.3f), Math.max(rScore, 0.55f), 1);
		Rubico.scoreFont.setColor(colors[
		                                 colors.length - (int) (colors.length * rScore)
		                                 ]);
		Rubico.scoreFont.setScale(MIN_SCALE + rScore/40);
	}

	private static void updateStringScore() {
		scoreToProcess = (int) score;
		// M
		processDigit(100000, 8);
		processDigit(10000, 9);
		processDigit(1000, 10);
		// K
		processDigit(100, 12);
		processDigit(10, 13);
		processDigit(1, 14);
	}

	protected static void processDigit(int digitFactor, int digitPlace) {
		currentDigit = scoreToProcess / digitFactor;
		strScore.charSet(digitPlace, currentDigit);
		scoreToProcess -= currentDigit * digitFactor;
	}

	public static int getRoundScore() {
		return (int)score;
	}

	public static void draw(SpriteBatch batch, boolean lost) {
		if (!lost)
			// bottom score
			Rubico.scoreFont.draw(batch, strScore, Rubico.halfWidth - Rubico.scoreFont.getBounds(strScore).width/2, FONT_HEIGHT + Rubico.scoreFont.getBounds(strScore).height);
//		if (!Gdx.input.isTouched()) {
//			score--;
//		}
	}

	public static void lost(boolean persist) {
		if (Rubico.profile.highscore < Score.score)
			Rubico.profile.updateHighscore(Score.score);
	}

	public static void decrease() {
		rScore -= EndlessMode.delta / 4;
		if (rScore < 0.1f)
			rScore = 0.1f;
		updateFont();
	}
}

