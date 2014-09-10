package elements.generic.blocs;

import java.util.Random;

import jeu.Physic;
import jeu.Rubico;
import jeu.Strings;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.Score;
import assets.AssetMan;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.particles.individual.explosions.ExplodingBloc;

public class Column {
	
	public static final Array<Column> COLUMNS = getCol();

	protected static Array<Column> getCol() {
		Array<Column> tmp = new Array<Column>(true, Builder.COL);
		for (int i = 0; i < Builder.COL; i++) {
			tmp.add(new Column());
		}
		return tmp;
	}
	private static final float COOLDOWN = 10;
//	private static final int MAX_BLOCS = 29;
	public static int COLLIDED_COLUMN, ROW;
	public static float nextCheck = 0;
	public boolean activated = false;
	private final Array<Bloc> BLOCS = new Array<Bloc>();
	public float x, nextPop;
	public int lvl = 1;
	private int column;
	public static boolean blip = false, blocRemoved;
	
	public void setX(float x, int column) {
		this.x = x;
		lvl = 1;
		nextPop = EndlessMode.now + COOLDOWN + column / 3f;
		this.column = column;
		activated = true;
	}
	
	public void generateBloc(int row, Random random) {
		if (column != 0) {
			Array<Integer> possibleLvl = new Array<Integer>();
			for (Bloc b : COLUMNS.get(column -1).BLOCS) {
//			for (Bloc b : COLUMNS[column -1].BLOCS) {
				if (Math.abs(row - b.row) <= 1)
					possibleLvl.add(b.hp);
			}
			if (possibleLvl.size > 0)
				BLOCS.add(Bloc.addBlock(x, row, possibleLvl.get(random.nextInt(possibleLvl.size))));
			else
				BLOCS.add(Bloc.addBlock(x, row, random.nextInt(6) + 1));
		} else {
			BLOCS.add(Bloc.addBlock(x, row, random.nextInt(6) + 1));
		}
		nextCheck = 0;
	}
	
	public void generateBloc(int row, int lvl) {
		nextCheck = 0;
		BLOCS.add(Bloc.addBlock(x, row, lvl));
	}
	
	public void fillColumn(int nbBlocs) {
		nbBlocs += (lvl) * 2;
		if (nbBlocs > 14)
			nbBlocs = 14;
		for (int i = 0; i < nbBlocs; i++)
			BLOCS.add(Bloc.addBlock(x, i, lvl));
	}
	
	public static boolean draw(SpriteBatch batch) {
//		if (COLUMNS.size == 0)
//			return false;
		for (Column c : COLUMNS) {
			if (c.nextPop < EndlessMode.now )
				c.addBlock();
			for (Bloc b : c.BLOCS)
				b.display(batch);
			c.testBalls();
		}
		
		if (blip) {
			for (Column c : COLUMNS)
				c.brightAdjacentBlocs();
			if (Ball.CHRONO_EXPLOSIVE > 0)
				Ball.CHRONO_EXPLOSIVE--;
		}
		for (Column c: COLUMNS)
			for (Bloc b : c.BLOCS)
				if (b.hp <= 0)
					c.removeBloc(b, null);
		
		if (!EndlessMode.endless && nextCheck < EndlessMode.now && !EndlessMode.finished && !EndlessMode.lost) {
			boolean finished = true;
			for (Column c : COLUMNS) {
				if (c.BLOCS.size > 0)
					finished = false;
			}
			if (finished)
				EndlessMode.finished();
			nextCheck = EndlessMode.now + 1;
		}
		ExplodingBloc.draw(batch, Bloc.EXPLOSIONS);
		blip = false;

		batch.setColor(AssetMan.WHITE);
		return blocRemoved;
	}

	protected void brightAdjacentBlocs() {
		if (Math.abs(column - COLLIDED_COLUMN) <= 1) 
			for (Bloc b : BLOCS) {
				if (Math.abs(b.row - ROW) <= 1) {
					if (Ball.CHRONO_EXPLOSIVE > 0)		b.removeHp(Vector2.Zero, 10);
					else								b.initPlace();
				}
			}
	}

	protected void testBalls() {
		for (Ball ball : Ball.BALLS)
			if (ball.x > x && ball.x < x + Bloc.WIDTH)
				for (Bloc b : BLOCS)
					testCollisionBloc(this, ball, b);
	}

	protected static void testCollisionBloc(Column c, Ball ball, Bloc b) {
		if (Physic.isPointInRect((float)ball.x, (float)ball.y, b.x, b.y, Bloc.WIDTH, Bloc.HEIGHT)) {
			b.collision(ball);
			COLLIDED_COLUMN = c.column; 
			if (b.hp <= 0)
				c.removeBloc(b, ball);
		}
	}

	private void addBlock() {
//		if (BLOCS.size > MAX_BLOCS)
//			return;
		if (lvl == 1 || !activated || !EndlessMode.endless)
			return;
		boolean pop = false;
        int row = 0, oldRow = 0;
        do {
            for (Bloc b : BLOCS)
                if (b.row == row)
                    row++;
            if (oldRow == row) {
            	BLOCS.add(Bloc.addBlock(x, row, lvl));
                pop = true;
            }
            oldRow = row;
        } while (!pop);
        nextPop = EndlessMode.now + (COOLDOWN - (Score.lvl - 1)); 
	}

	public void removeBloc(Bloc b, Ball ball) {
		blocRemoved = true;
		b.free(BLOCS);
		if (BLOCS.size == 0)
			cleared(ball);
	}

	private void cleared(Ball ball) {
		lvl++;
		SoundMan.playBruitage(SoundMan.column);
		if (EndlessMode.endless)
			Builder.isEmpty(this);
		if (ball != null)			ball.lvlUp();
		else 						Ball.lvlUpOneBall();
		if (lvl == 2)				Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_COLUMN_LVL2);
		else if (lvl == 7)			Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_COLUMN_LVL7);
	}
	
	public static void clear() {
		for (Column c : COLUMNS) {
			Bloc.freeAll(c.BLOCS);
			c.BLOCS.clear();
		}
//		POOL.freeAll(COLUMNS);
//		COLUMNS.clear();
		Bloc.clear();
		nextDepop = 0;
	}


	private static float nextDepop = 0;
	public static void explode(int i) {
//		if (COLUMNS.size == 0 || nextDepop > EndlessMode.now)
//			return;
		if (nextDepop > EndlessMode.now)
			return;
		nextDepop = EndlessMode.now + 0.01f;
//		Column c = COLUMNS[Rubico.R.nextInt(COLUMNS.length)];
		Column c = COLUMNS.get(Rubico.R.nextInt(COLUMNS.size));
		if (c.BLOCS.size == 0)
			return;
		Bloc b = c.BLOCS.get(Rubico.R.nextInt(c.BLOCS.size));
		b.removeHp(Vector2.Zero, 100);
		b.free(c.BLOCS);
		if (c.BLOCS.size == 0) {
//			COLUMNS.removeValue(c, true);
//			POOL.free(c);
			c.activated = false;
		}
	}

	private static final Rectangle R = new Rectangle();
	public static boolean isCollidingWithABloc(float x, float y, float width, float height) {
		for (Column c : COLUMNS)
			if (c.x < x + width && c.x + Bloc.WIDTH > x) {
				R.set(x, y, width, height);
				for (Bloc b : c.BLOCS) 
					if (b.overlaps(R))
						return true;
			}
		return false;
	}

	public boolean overlaps(float x, float width) {		return x + width > this.x && x < this.x + Bloc.WIDTH;	}
	public Array<Bloc> getBlocs() {						return BLOCS;											}

}