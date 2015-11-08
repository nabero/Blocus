package jeu;

public interface TalkToTheWorld {

	void Login();
    void LogOut();
   
    /**
     * get if client is signed in to Google+
     */
    boolean getSignedIn();
   
    /**
     * submit a score to a leaderboard
     */
    void submitScore(String leaderboard, int score);
   
    /**
     * gets the scores and displays them threw googles default widget
     */
    void getAchievements();
    /**
     * gets the achievements and displays them threw googles default widget
     */
    void getScores();
   
    void unlockAchievementGPGS(String achievementId);
    void displayToast(String s);
    void bragTwitter(String s);
    void bragFacebook(String s);
    void followTwitter();
//	void buyUsABeer();
	void toast(String string);
//	void buyXp();
	void showAds(boolean show);
	void buyXp();
	void buyRemoveAds();
	void displayInterstitial();
	void loadInterstitial();
	void otherGames(String url);
}
