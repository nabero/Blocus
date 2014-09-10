package jeu;

import java.text.DecimalFormat;

public class Strings {
	
	
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
	public static final String ACH_BALL_LVL_MAX = "CgkI4KHwmMwaEAIQBA";

	public static final String LEADERBOARD_KEY = "CgkI4KHwmMwaEAIQBQ";

	private static DecimalFormat initDf() {
		DecimalFormat DF = new DecimalFormat();
		DF.setMaximumFractionDigits(1);
		DF.setMinimumFractionDigits(1);	
		DF.setDecimalSeparatorAlwaysShown(true);
		return DF;
	}
	
}
