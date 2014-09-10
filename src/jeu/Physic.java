package jeu;

import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import elements.generic.Paddle;

public class Physic {
	
	public static boolean pointIn(final Sprite s) {
		final int x = Gdx.input.getX();
		final int y = Rubico.screenHeight - Gdx.input.getY();
        return s.getX() <= x && s.getX() + s.getWidth() >= x && s.getY() <= y && s.getY() + s.getHeight() >= y;
	}
	
	public static boolean isOnScreen(final Vector2 position, final float hauteur, final float largeur) {
		return !(position.y + hauteur < -Rubico.heightDiv10 || position.x + largeur < 0 || position.x > Rubico.screenWidth  || position.y > Rubico.heightPlus4 + hauteur);
	}
	
	public static boolean isOnScreenWithTolerance(final Vector2 position, final float hauteur, final float largeur) {
		return !(position.y  + largeur + hauteur < -Rubico.heightDiv10 || position.x + largeur + hauteur < -Stats.WIDTH_DIV_10 || position.x > Stats.GAME_ZONE_W_PLUS_WIDTH_DIV_10  + largeur + hauteur || position.y > Rubico.heightPlus4 + largeur + hauteur );
	}
	
	public static boolean isOnScreen(final float x, final float y, final float width) {
		return !(y + width < 0 || x + width < 0 || x > Rubico.screenWidth || y > Rubico.screenHeight + width);
	}
	
	/**
	 * @return way
	 */
	public static boolean goToZigZagCentre(final Vector2 pos, final Vector2 dir, final int halfWidth, boolean way, final float amplitude, final float height, final int width){
		if (pos.x + halfWidth < Rubico.halfWidth)
			way = false;
		else
			way = true;
		if (way)
			dir.x -= amplitude * EndlessMode.delta;
		else
			dir.x += amplitude * EndlessMode.delta;
		mvtNoCheck(pos, dir);
		return way;
	}
	
	public static boolean isPointInRect(final float x, final float y, final float rectX, final float rectY, final float rectWidth, final float rectHeight) {
		 return rectX <= x && rectX + rectWidth >= x && rectY <= y && rectY + rectHeight >= y;
	}
	public static boolean isPointInRect(final float x, final float y, final Vector2 pos, final float rectWidth, final float rectHeight) {
		return pos.x <= x && pos.x + rectWidth >= x && pos.y <= y && pos.y + rectHeight >= y;
	}
	public static boolean isPointInRect(final float x, final float y, final Vector2 pos, final float rectWidth, final float rectHeight, SpriteBatch batch) {
		batch.setColor(1, 0, 0, 0.5f);
		batch.draw(AssetMan.debris, x - 2, y - 2, 4, 4);
		batch.setColor(1, 0, 0, 0.5f);
		batch.draw(AssetMan.debris, pos.x, pos.y, rectWidth, rectHeight);
		return pos.x <= x && pos.x + rectWidth >= x && pos.y <= y && pos.y + rectHeight >= y;
	}
	
	public static boolean isPointInSquare(final float x, final float y, final float rectX, final float rectY, final float rectWidth) {
		 return rectX <= x && rectX + rectWidth >= x && rectY <= y && rectY + rectWidth >= y;
	}

	private static final Vector2 DESIRED = new Vector2();
	private static final Vector2 STEER = new Vector2();

	public static float setDirTo(final Vector2 dir, final Vector2 pos, final float maxSpeed, final float width, final float halfWidth, float rotation, Vector2 target) {
		mvtToTarget(dir, pos, maxSpeed, width, halfWidth, rotation, target);
		return dir.angle();
	}
	
	
	public static void mvtToTarget(final Vector2 dir, final Vector2 pos, final float maxSpeed, final float width, final float halfWidth, float rotation, Vector2 target) {
		DESIRED.x = pos.x - target.x;
		DESIRED.y = pos.y - target.y;
		DESIRED.nor();
		dir.nor();
		STEER.x = dir.x - DESIRED.x;
		STEER.y = dir.y - DESIRED.y;
		STEER.nor();
		STEER.scl(rotation);
		dir.add(STEER);
		dir.scl(maxSpeed);
	}
	
	public static void mvtNoCheck(final Vector2 pos, final Vector2 dir) {
		pos.x += dir.x * EndlessMode.delta;
		pos.y += dir.y * EndlessMode.delta;
	}
	
	public static boolean mvt(final Vector2 dir, final Vector2 pos, final float width) {
		pos.x += dir.x * EndlessMode.delta;
		pos.y += dir.y * EndlessMode.delta;
		return isOnScreen(pos, width, width);
	}
	
	public static boolean mvt(final float height, final float width, final Vector2 dir, final Vector2 pos) {
		pos.x += dir.x * EndlessMode.delta;
		pos.y += dir.y * EndlessMode.delta;
		return isOnScreen(pos, width, height);
	}
	
	private static final Vector2 tmpPos = new Vector2();
	
	public static float getAngleWithPlayer(final Vector2 pos, final float halfWidth, final float halfHeight) {
		tmpPos.x = Paddle.xCenter;
		tmpPos.y = Paddle.yCenter;
		return tmpPos.sub(pos.x + halfWidth, pos.y + halfHeight).angle();
	}

	public static boolean isNotDisplayed(Vector2 pos, float width, float height) {
		return pos.x + width < 0 || pos.x > Rubico.screenWidth || pos.y > Rubico.screenHeight || pos.y + height < 0;
	}

	public static boolean isLeft(Vector2 pos, float halfWidth) {
		return (pos.x + halfWidth < Rubico.halfWidth); 
	}

	public static boolean isOnTop(Vector2 e, float halfHeight) {
		return e.y + halfHeight > Rubico.halfHeight;
	}

	public static float rand(float factor) {
		return (Rubico.R.nextFloat() - 0.5f) * factor;
	}
}