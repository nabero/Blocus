package jeu;

import java.text.DecimalFormat;

public class Strings {
	
	private static final String[] NUMBERS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10!", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
		"21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
		"31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
		"41", "42", "43", "44", "45", "46", "47", "48", "49", "50!!"};
	public static final String CREATE = "Create", FILE = "File", SELECT_FRAME = "Let's pick a frame";
	
	public static final String UPGRADE_BUTTON = "Upgrade paddle",
		RESTART_BUTTON = "Restart",
		BACK = "Back to menu",
		DEAD = "Thanks for playing !",
		BRAG_TWITTER = "Brag on Twitter";
	
	public static final String SENSITIVITY = "SENSITIVITY  ",
		TWITTER = "Twitter",
		TUTO_TOUCH = "When you don't touch the screen,\n the game slows down",
		GET_READY = "Get ready !",
		BUY_XP_BUTTON = "Get more xp !";
	
	public static final DecimalFormat DF = initDf();

	public static final String ACH_4BALLS = "CgkI4KHwmMwaEAIQAA";
	public static final String ACH_7BALLS = "CgkI4KHwmMwaEAIQAQ";
	public static final String ACH_COLUMN_LVL2 = "CgkI4KHwmMwaEAIQAg";
	public static final String ACH_COLUMN_LVL7 = "CgkI4KHwmMwaEAIQAw";
	public static final String ACH_BALL_LVL_MAX = "CgkI4KHwmMwaEAIQBA",
			ACH_LVL100 = "CgkI4KHwmMwaEAIQCA",
			ACH_9000 = "CgkI4KHwmMwaEAIQCQ";

	public static final String LEADERBOARD_KEY = "CgkI4KHwmMwaEAIQBQ";
	public static final String LEADERBOARD_LEVEL_KEY = "CgkI4KHwmMwaEAIQBw";

	private static DecimalFormat initDf() {
		DecimalFormat DF = new DecimalFormat();
		DF.setMaximumFractionDigits(1);
		DF.setMinimumFractionDigits(1);	
		DF.setDecimalSeparatorAlwaysShown(true);
		return DF;
	}

	public static CharSequence getNumber(int lvlSpreadPaddle) {
		if (lvlSpreadPaddle >= 0 && lvlSpreadPaddle < 50)
			return NUMBERS[lvlSpreadPaddle];
		return "?";
	}
	
}
