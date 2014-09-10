package elements.particles.individual;

import assets.AssetMan;

import com.badlogic.gdx.utils.Array;

import jeu.Rubico;
import jeu.Stats;

public class PrecalculatedParticles {

	public static final float INITIAL_WIDTH = ((Stats.U * 2.7f) / 8), INITIAL_HALF_WIDTH = INITIAL_WIDTH / 2;
	public static final int BLUE = 1, GREEN = 2, PINK = 3, FIRE = 4, YELLOW = 5, BRIGHT_PINK = 6, BRIGHT_YELLOW = 7;
	
	private static final float stepColorsOverTime = 0.065f;
	public static final float[]
			blocBlue = initBloc(stepColorsOverTime * 0.4f, 0.15f, BLUE),
			blocGreen = initBloc(stepColorsOverTime * 0.4f, 0.15f, GREEN),
			blocPink = initBloc(stepColorsOverTime * 0.4f, 0.15f, PINK),
			blocFire = initBloc(stepColorsOverTime * 0.4f, 0.15f, FIRE),
			blocYellow = initBloc(stepColorsOverTime * 0.4f, 0.15f, YELLOW),
			brightPink = initBloc(stepColorsOverTime * 0.4f, 0.15f, BRIGHT_PINK),
			brightYellow = initBloc(stepColorsOverTime * 0.4f, 0.15f, BRIGHT_YELLOW),
			
			paddleYellowToGreen = initAlphasYellowToGreen(stepColorsOverTime * 5, 0.15f, true),
			paddleCyanToGreen = initAlphasCyanToGreen(stepColorsOverTime * 10, 0.15f, true);
	public static final int MAX = blocBlue.length, SMALLL_MAX = (int) (MAX * 0.65f), HALF_LENGTH = blocBlue.length / 2, LENGTH_DIV3_MUL2 = (int) (blocBlue.length * 0.66f), LENGTH_DIV4_MUL3 = (int) (blocBlue.length * 0.75f);
	
	private static float[] initAlphasCyanToGreen(float step, float min, boolean white) {
		float alpha = 1;
		Array<Float> tmp = new Array<Float>();
		if (white) {
			tmp.add(AssetMan.convertARGB(1, 0.95f, 1, 1));
			tmp.add(AssetMan.convertARGB(1, 0.55f, 1, 1));
			tmp.add(AssetMan.convertARGB(1, 0.25f, 1, 1));
		}
		while (alpha > min) {
			tmp.add(AssetMan.convertARGB(alpha, 0.25f, 1, alpha));
			alpha -= step;
		}
		return Rubico.convert(tmp);
	}
	
	private static float[] initAlphasYellowToGreen(float step, float min, boolean white) {
		float alpha = 1;
		Array<Float> tmp = new Array<Float>();
		if (white) {
			tmp.add(AssetMan.convertARGB(1, 1, 1, 0.95f));
			tmp.add(AssetMan.convertARGB(1, 1, 1, 0.55f));
			tmp.add(AssetMan.convertARGB(1, 1, 1, 0.25f));
		}
		while (alpha > min) {
			tmp.add(AssetMan.convertARGB(alpha, Math.min(1, alpha*2), 1, 0.25f));
			alpha -= step;
		}
		return Rubico.convert(tmp);
	}
	
	private static float[] initBloc(float step, float min, int color) {
		float alpha = 1f;
		Array<Float> tmp = new Array<Float>();
		while (alpha > min) {
			tmp.add(getColor(alpha, color));
			alpha -= step;
		}
		return Rubico.convert(tmp);
	}

	private static float getColor(float alpha, int color) {
		switch (color) {
		case BRIGHT_PINK:	return AssetMan.convertARGB(		getHalfMin(alpha), 	0.95f,									get09Max(alpha), 					minAlphaPlusAlphaMul2(alpha) );
		case BRIGHT_YELLOW:	return AssetMan.convertARGB(		getHalfMin(alpha), 	minAlphaPlusAlphaMul2(alpha), 			0.95f,								get09Max(alpha) );
		case BLUE:			return AssetMan.convertARGB(		getHalfMin(alpha), 	quarter(), 								get09Max(alpha), 					minAlphaPlusAlphaMul2(alpha) );
		case GREEN:			return AssetMan.convertARGB(		getHalfMin(alpha), 	quarter(), 								minAlphaPlusAlphaMul2(alpha), 		get09Max(alpha) );
		case PINK:			return AssetMan.convertARGB(		getHalfMin(alpha), 	minAlphaPlusAlphaMul2(alpha), 			quarter(),							get09Max(alpha) );
		case FIRE:			return AssetMan.convertARGB(		getHalfMin(alpha), 	minAlphaPlusAlphaMul2(alpha), 			get09Max(alpha), 					quarter());
		case YELLOW:		return AssetMan.convertARGB(		getHalfMin(alpha), 	Math.min(1, (alpha)), 					Math.min(1, (alpha) + 0.5f),		0.05f );
		}
		return AssetMan.convertARGB(					getHalfMin(alpha), 	quarter(), 								get09Max(alpha),					minAlphaPlusAlphaMul2(alpha) );
	}

	protected static float minAlphaPlusAlphaMul2(float alpha) {			return Math.min(1, (alpha * 2) + 0.5f);		}
	protected static float getHalfMin(float alpha) {					return Math.max(alpha, 0.35f);				}
	protected static float get09Max(float alpha) {						return Math.min(0.9f, alpha);				}
	protected static float quarter() {									return 0.25f;								}
	
	public static float[] getColor(int lvl) {
		switch (lvl) {
		case 1:			return blocBlue;
		case 2:			return blocPink;
		case 3:			return blocGreen;
		case 4:			return blocFire;
		case 5:			return blocYellow;
		case 6:			return brightPink;
		default:		return brightYellow;
		}
	}
	
}
