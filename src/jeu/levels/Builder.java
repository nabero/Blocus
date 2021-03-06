package jeu.levels;

import java.util.Random;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import elements.generic.blocs.Bloc;
import elements.generic.blocs.Column;
import elements.generic.paddles.StraightPaddle;

public class Builder {
	
	public static final int COL = 11, LINE = 2;
	public static final float MIN_Y = Rubico.screenHeight * 0.95f, MAX_Y = (Rubico.screenHeight * 0.5f), PADDING_Y = 0, MULTI_Y = PADDING_Y - Bloc.HEIGHT, MIN_X = Rubico.screenWidth * 0.065f, PADDING_X = 0, MULTI_X = PADDING_X + Bloc.WIDTH;
	public static long seed = System.currentTimeMillis();
	public static final Random RAND = new Random(seed);
	public static int nbrOfDiamondsPerLelve = 5;
	//	1	0	1
	//	0	1	0
	//	1	0	1
	
	// [1][1]	: 	1
	// [0][0]	:	1
	// [0][1]	:	0
	// [0][0]	:	1
	// [row][col]
	public static void generateLevel(int lvl) {
		nbrOfDiamondsPerLelve = 5;
		Column.clear();
		Shape shape = new Shape(lvl);
		
		for (int col = 0; col < COL; col++)
			Column.COLUMNS.get(col).setX(MIN_X + (col * MULTI_X), col);
		
		for (int col = 0; col < COL; col++) {
			for (int row = 0; row < shape.shape.length; row++) {
				if (shape.shape[row][col] != 0) {
					Column c = Column.COLUMNS.get(col);
					c.generateBloc(row, shape.shape[row][col]);
				}
			}
		}
	}
	
	public static void init() {
		if (!EndlessMode.endless)
			generateLevel(EndlessMode.difficulty);
		else {
			Column.clear();
			for (int col = 0; col < COL; col++) {
//				Column c = Column.POOL.obtain();
				Column c = Column.COLUMNS.get(col);
				c.setX(MIN_X + (col * MULTI_X), col);
				c.fillColumn(LINE);
	//			Column.COLUMNS.add(c);
			}
		}
	}

	public static void isEmpty(Column column) {
		if (EndlessMode.endless)
			column.fillColumn(LINE);
	}

	public static void nextSeed() {
		seed = System.currentTimeMillis();
	}

}
