package jeu;

import java.util.Random;

import jeu.mode.EndlessMode;
import menu.screens.Loading;
import shaders.Bloom;
import assets.AssetMan;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import elements.particles.Particles;

public class Rubico extends Game implements ApplicationListener {

	// malus qui enl�ve une partie du paddle
	// explosive bloc. Regenerate bloc, falling bloc
	
	public static float halfWidth, tierWidth, halfHeight, screenWidth, screenWidth2Thirds, screenHeight, widthDiv10, heightDiv10, heightDiv100, heightDiv50, heightDiv20, heightPlus4, heightDiv8, widthDiv5, widthDiv5Mul2, widthDiv5Mul3, widthDiv5Mul4, heightDiv4, heightDiv10Mul9, heightDiv10Mul8, heightDiv10Mul7, HEIGHT_ECRAN_PALLIER_3 = 0, HEIGHT_ECRAN_PALLIER_7;
	public static ProfilManager profilManager;
	public static Profil profile;
	public static BitmapFont menuFont, menuFontSmall, scoreFont, effectFont, outlineFont, blocFont;
	public static AssetMan assetMan;
	public static SpriteBatch batch;
	public static TalkToTheWorld talkToTheWorld;
	public static final Random R = new Random();
	public static final float mulSCORE = 1.1f;
	public static float originalScoreFontScale;
	public static final Vector2 vecteurPosition = new Vector2(), tmpPos = new Vector2(), tmpDir = new Vector2(), tmp2 = new Vector2();
	public static int tmpInt;
	public static OrthographicCamera cam;
	public static Matrix4 tmpCombined; 
	public static float originalBlocFont;
	
	public Rubico(TalkToTheWorld google) {
		Rubico.talkToTheWorld = google;
		log("begin");
	}

	@Override
	public void create() {
		log("Create");
		batch = new SpriteBatch(5460);
		assetMan = new AssetMan();
		if (Gdx.app.getVersion() != 0)
			Rubico.talkToTheWorld.showAds(true);
		dimensions();
		profilManager = new ProfilManager();
		profile = profilManager.retrieveProfile();
		initFonts();
		// ***** Une fois que toutes les variables globales sont chargees on lance le loading pour charger les assets
		final Loading loading = new Loading(this);
		setScreen(loading);
	}

	public static void log(String s ) {
//		Gdx.app.log("Blocus", s);
	}

	public static void initFonts() {
		log("init fonts");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PolenticalNeonRegular.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		
//		originalScoreFontScale = Rubico.screenHeight + Rubico.screenWidth;
//		originalScoreFontScale /= 900;
//		if (originalScoreFontScale < 1f)
//			originalScoreFontScale = 1f;
		originalScoreFontScale = 2;
//		originalScoreFontScale = (Gdx.graphics.getWidth() + Gdx.graphics.getHeight()) / 450;
//		System.out.println(originalScoreFontScale);
		
		scoreFont = setFont((int) (13 * originalScoreFontScale), 		generator, param, AssetMan.convertARGB(1,  	0.32f, .9f, 1f));
//		scoreFont = setFont(3, 		generator, param, AssetMan.convertARGB(1,  	0.32f, .9f, 1f));
//		scoreFont.setScale(0.5f);
		scoreFont.setScale(0.03f);
		
		menuFontSmall = setFont((int) (12 * originalScoreFontScale), 	generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		menuFontSmall.setScale(0.03f);
		
		blocFont = setFont((int) (12 * originalScoreFontScale), 	generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		blocFont.setScale(0.03f);
		Rubico.blocFont.setColor(AssetMan.BLACK);
		originalBlocFont = 0.03f;
		originalBlocFont = 0.03f;
		
		menuFont = setFont((int) (17 * originalScoreFontScale), 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		menuFont.setScale(0.03f);
//		menuFont = setFont(4, 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		outlineFont = setFont((int) (19 * originalScoreFontScale), 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		outlineFont.setScale(0.03f);
//		outlineFont = setFont(5, 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		
		effectFont = setFont((int) (27 * originalScoreFontScale), 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		effectFont.setScale(0.03f);
//		effectFont = setFont(8, 		generator, param, AssetMan.convertARGB(1, 	0.32f, .9f, 1f));
		
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
	}

	private static BitmapFont setFont(int size, FreeTypeFontGenerator generator, FreeTypeFontParameter param, float color) {
		BitmapFont font = new BitmapFont();
		param.size = size;
		font = generator.generateFont(param);
		font.setColor(color);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return font;
	}

	public static void dimensions() {
		log("dimensions");
		screenWidth = 36 / 2;
		screenHeight = 64 / 2;
		cam = new OrthographicCamera(screenWidth, screenHeight);
		cam.position.set(Rubico.screenWidth/2, Rubico.screenHeight / 2, 0);
//		cam.position.z = 1;
//		screenWidth = Gdx.graphics.getWidth();
//		screenHeight = Gdx.graphics.getHeight();
		halfWidth = screenWidth / 2;
		halfHeight = screenHeight / 2;
		tierWidth = screenWidth / 3;
		widthDiv10 = screenWidth / 10;
		heightDiv10 = screenHeight / 10;
		heightDiv100 = screenHeight / 100;
		heightDiv50 = screenHeight / 50;
		heightDiv8 = screenHeight / 8;
		heightDiv20 = screenHeight / 20;
		widthDiv5 = widthDiv10 * 2;
		widthDiv5Mul2 = widthDiv5 * 2;
		widthDiv5Mul3 = widthDiv5 * 3;
		widthDiv5Mul4 = widthDiv5 * 4;
		heightDiv4 = screenHeight / 4;
		heightPlus4 = screenHeight + 4;
		heightDiv10Mul9 = screenHeight - heightDiv10;
		heightDiv10Mul8 = screenHeight - (heightDiv10 * 2);
		heightDiv10Mul7 = screenHeight - (heightDiv10 * 3);
		HEIGHT_ECRAN_PALLIER_3 = screenHeight - (heightDiv10 * 3);
		HEIGHT_ECRAN_PALLIER_7 = screenHeight - (heightDiv10 * 7);
		screenWidth2Thirds = (screenWidth / 2) * 3;
	}
	
	public static void reset(){
        Particles.clear();
        EndlessMode.reset();
        Rubico.profile.reset();
	}

	public static AssetMan getAssetMan() {
		return assetMan;
	}

	public static Bloom bloom;
	public static boolean alternateGraphics = false;
	
	public static void initBloom() {
		try {
			bloom = new Bloom();
			bloom.setBloomIntesity(Rubico.profile.intensiteBloom);
		} catch (Exception e) {
			System.err.println("INIT BLOOM FAILED, PROFILE : " + Rubico.profile);
			e.printStackTrace();
		}
	}

	public static void begin(float delta) {
		EndlessMode.delta = delta;
		EndlessMode.majDeltas();
		EndlessMode.now += delta;
		bloom.capture();
		cam();
	}

	public static void cam() {
		cam.update();
		tmpCombined = cam.combined;
		if (tmpCombined != null) {
			batch.setProjectionMatrix(tmpCombined);
//			CSG.rayHandler.setCombinedMatrix(cam.combined);
		}
	}
	
	public static void end() {
		batch.end();
		bloom.render();
	}
	
	public static float[] convert(Array<Float> tmp) {
		float[] array2 = new float[tmp.size];
	    int i=0;
	    for (Float f : tmp) {
	        array2[i] = f.floatValue();
	        i++;
	    }
		return array2;
	}
	
	public static float[] getDifferences(float[] array) {
		final Array<Float> tmp = new Array<Float>();
		tmp.add(0f);
		for (int i = 1; i < array.length; i++)
			tmp.add( (array[i]-array[i-1])/2);
		return Rubico.convert(tmp);
	}

	public static Vector2[] convert(Array<Vector2> positions) {
		Vector2[] array2 = new Vector2[positions.size];
	    int i=0;
	    for (Vector2 f : positions) {
	        array2[i] = f;
	        i++;
	    }
		return array2;
	}

	public static float[] getDouble(float[] array) {
		final Array<Float> tmp = new Array<Float>();
		tmp.add(0f);
		for (int i = 1; i < array.length; i++)
			tmp.add(array[i]*2);
		return Rubico.convert(tmp);
	}
}
