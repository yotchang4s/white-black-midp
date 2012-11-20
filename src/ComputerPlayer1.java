import java.util.*;

public class ComputerPlayer1 implements Player {
	protected int myColor;
	private final int[][] PVAL = { { 100, -20, 10, 5, 5, 10, -20, 100 },
			{ -20, -50, -2, -2, -2, -2, -50, -20 },
			{ 10, -2, -1, -1, -1, -1, -2, 10 },
			{ 5, -2, -1, -1, -1, -1, -2, 5 }, { 5, -2, -1, -1, -1, -1, -2, 5 },
			{ 10, -2, -1, -1, -1, -1, -2, 10 },
			{ -20, -50, -2, -2, -2, -2, -50, -20 },
			{ 100, -20, 10, 5, 5, 10, -20, 100 } };
	private Kyokumen node;
	static Random random = new Random();

	public ComputerPlayer1(int myColor) {
		this.myColor = myColor;
		node = new Kyokumen();
	}

	public Point where(Kyokumen kyokumen) {
		int max = Integer.MIN_VALUE;
		int mx = 0;
		int my = 0;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (kyokumen.isPosibleToPlace(x, y)) {
					kyokumen.copyKyokumen(node);
					node.placeStone(x, y);
					int est = estimate(node);
					if (est > max) {
						max = est;
						mx = x;
						my = y;
					}
				}
			}
		}
		return new Point(mx, my);
	}

	private int checkConer(Kyokumen k, int x, int y) {
		if (((x == 0 && y == 1) || (x == 1 && y == 0) || (x == 1 && y == 1))
				&& k.getState(0, 0) != Kyokumen.NONE) {
			return 30;
		}
		if (((x == 7 && y == 6) || (x == 6 && y == 7) || (x == 6 && y == 6))
				&& k.getState(7, 7) != Kyokumen.NONE) {
			return 30;
		}
		if (((x == 0 && y == 6) || (x == 1 && y == 7) || (x == 1 && y == 6))
				&& k.getState(0, 7) != Kyokumen.NONE) {
			return 30;
		}
		if (((x == 6 && y == 0) || (x == 7 && y == 1) || (x == 6 && y == 1))
				&& k.getState(7, 0) != Kyokumen.NONE) {
			return 30;
		}
		return 0;
	}

	protected int estFunc1(Kyokumen k) {
		int col = k.prev();
		int sum = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (k.getState(i, j) == col) {
					sum += PVAL[i][j] + checkConer(k, i, j);
				} else if (k.getState(i, j) == k.otherSide(col)) {
					sum -= PVAL[i][j] + checkConer(k, i, j);
				}
			}
		}
		return sum;
	}

	protected int estimate(Kyokumen kyokumen) {
		return estFunc1(kyokumen) + (int) (Kyokumen.random(3));
	}
}
